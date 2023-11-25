package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.AttendanceDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class AttendanceService {
	AttendanceDAO dao = AttendanceDAO.getInstance();
	String stdId;

	// 현재 날짜
	LocalDate nowDate = LocalDate.now();
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	String formatedNowDate = nowDate.format(dateFormatter);

	// 현재 시간
	LocalTime nowTime = LocalTime.now();
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	String formatedNowTime = nowTime.format(timeFormatter);

	// 싱글톤 패턴
	private static AttendanceService instance = null;

	private AttendanceService() {
	}

	public static AttendanceService getInstance() {
		if (instance == null)
			instance = new AttendanceService();
		return instance;
	}

	/** 출석 조건 여부 확인 */
	public int enable() {
		stdId = (String) Controller.sessionStorage.get("STD_ID");
		String atdCode = null; // 출결 코드
		String atdDate = null; // 출석 일자
		String inStatus = null;// 입실 상태

		List<Map<String, Object>> result = dao.getAtdStatus(stdId);

		// 값이 존재하지않으면 첫 수강인 사람
		if (result == null) {
			in();
			return View.STUDENT_LECTURE_CONTENT;
		}

		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> res = result.get(i);

			atdCode = (String) res.get("ATD_CODE");
			atdDate = atdCode.substring(0, atdCode.length() - 3);

			// 입실 기록 입력 받음
			inStatus = (String) res.get("INTIME");
		}

		if (atdDate.equals(formatedNowDate)) {
			if (inStatus != null) {
				// 날이 같고 입실 기록이 있으면 퇴실 입력
				out(atdCode);
			}
		} else {
			in();
		}

		return View.STUDENT_LECTURE_CONTENT;
	}

	/** 학생의 입실 관리 */
	public int in() {
		Object maxCode = null;
		String atdCode = "";
		
		stdId = (String) Controller.sessionStorage.get("STD_ID");

		Map<String, Object> maxCodeReturn = dao.getMaxValue();
		
		try {
			maxCode = maxCodeReturn.get("MAXVALUE");
			atdCode = formatedNowDate + maxCode;
		} catch (NullPointerException e) {
			atdCode = formatedNowDate + "001";
		}
		
		Map<String, Object> getAcdID = dao.getAcademy(stdId);

		if (getAcdID == null) {
			PrintUtil.printOne("수강하는 강좌가 존재하지않습니다.");
			PrintUtil.delay();
			return View.STUDENT_LECTURE_CONTENT;
		}

		Object acdID = getAcdID.get("ACD_ID");

		List<Object> param = new ArrayList<Object>();
		param.add(atdCode); // 오늘 날짜
		param.add(formatedNowTime); // 현재 시간
		param.add(stdId); // 학생 ID
		param.add(acdID); // 학원 ID

		int result = dao.inTime(param);
		if (result > 0) {
			PrintUtil.printTwo("입실 완료.", "입실 시간: " + formatedNowTime);
		} else {
			PrintUtil.printOne("입실 실패.");
		}

		PrintUtil.delay();
		return View.STUDENT_LECTURE_CONTENT;
	}

	/** 학생의 퇴실 관리 */
	public int out(String atdCode) {
		stdId = (String) Controller.sessionStorage.get("STD_ID");

		List<Object> param = new ArrayList<Object>();
		param.add(formatedNowTime);
		param.add(atdCode);

		int result = dao.outTime(param);
		if (result > 0) {
			PrintUtil.printTwo("퇴실 완료.", "퇴실 시간: " + formatedNowTime);
		} else {
			PrintUtil.printOne("퇴실 실패.");
		}

		dateStatusRead(atdCode);
		PrintUtil.delay();
		return View.STUDENT_LECTURE_CONTENT;
	}

	/** 당일 입실 시간과 퇴실 시간을 불러 정수형으로 저장하는 메서드 */
	public void dateStatusRead(String atdCode) {
		stdId = (String) Controller.sessionStorage.get("STD_ID");
		String date = atdCode.substring(0, atdCode.length() - 3);

		List<Object> param = new ArrayList<Object>();
		param.add(stdId);
		param.add(date);

		Map<String, Object> res = dao.getInOutStatus(param);

		String inTime = (String) res.get("INTIME");
		String outTime = (String) res.get("OUTTIME");

		inTime = inTime.replace(":", "");
		int intInTime = Integer.parseInt(inTime);

		outTime = outTime.replace(":", "");
		int intOutTime = Integer.parseInt(outTime);

		statusChange(atdCode, intInTime, intOutTime);
	}

	/** 입실 퇴실 상황에따라 출결 상태를 변경하는 메서드 */
	public void statusChange(String atdCode, int inTime, int outTime) {
		String status = "";

		// 9시 이전 입실, 18시 이후 퇴실은 출석
		if (inTime < 90000 && outTime >= 180000) {
			status = "출석";
		} else {
			status = "지각 또는 조퇴";
		}

		List<Object> param = new ArrayList<Object>();
		param.add(status);
		param.add(atdCode);

		dao.statusChange(param);
	}

	/** 학생 출결 확인 */
	public int status() {
		int atdCount = 0; // 출석
		int tardyCount = 0; // 지각 또는 조퇴
		int absCount = 0; // 결석

		stdId = (String) Controller.sessionStorage.get("STD_ID");
		List<Map<String, Object>> result = dao.getAtdStatus(stdId);

		if (result == null) {
			PrintUtil.printOne("출결 상태가 존재하지 않습니다.");
			PrintUtil.delay();

			return View.STUDENT_LECTURE_CONTENT;
		}

		PrintUtil.printOne("출결 확인");

		PrintUtil.statusHead();

		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> res = result.get(i);

			String getDate = getDate((String) res.get("ATD_CODE"));
			String status = (String) res.get("ATD_STATUS");

			PrintUtil.statusData(getDate, res.get("INTIME"), res.get("OUTTIME"), status);

			if (status.equals("출석")) {
				atdCount++;
			} else if (status.equals("지각 또는 조퇴")) {
				tardyCount++;
			} else {
				absCount++;
			}
		}
		PrintUtil.listLine(result.size(), 10);

		PrintUtil.atdDetailHead();
		PrintUtil.atdDetailData(atdCount, tardyCount, absCount);

		ScanUtil.nextLine("<< 뒤로가기");
		return View.STUDENT_LECTURE_CONTENT;
	}

	/** 출석 코드를 받아와 yyyyMMdd 값을 yyyy-MM-dd 값으로 변환 */
	public String getDate(String atdCode) {
		String date = "";
		String atdDate = atdCode.substring(0, atdCode.length() - 3);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date formatDate = dateFormat.parse(atdDate);
			date = new SimpleDateFormat("yyyy-MM-dd").format(formatDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}
}

package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.AttendanceDAO;
import dao.SupportSystemDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class SupportSystemService {
	SupportSystemDAO dao = SupportSystemDAO.getInstance();
	AttendanceDAO atdDao = AttendanceDAO.getInstance();

	LocalDate now = LocalDate.now();
	int monthValue = now.getMonthValue();

	String id = "";

	// 싱글톤 패턴
	private static SupportSystemService instance = null;

	private SupportSystemService() {
	}

	public static SupportSystemService getInstance() {
		if (instance == null)
			instance = new SupportSystemService();
		return instance;
	}

	/** 학생 수당 조회 */
	public int benefit() {
		id = (String) Controller.sessionStorage.get("STD_ID");
		String resStr = "님의 수당 조회 결과\n\n\n";
		int pay = 0; // 수당
		int atdRate = 0; // 출석률

		
		Map<String, Object> stdLecCheck = dao.getStdLecInfo(id);

		resStr += "국비 교육 훈련 수당 지급 ";
		if (stdLecCheck.get("LEC_CODE") == null) {
			resStr += "미대상 + ";
		} else {
			resStr += "대상(30만원) + ";
			pay += 30;
		}

		resStr += "국민취업제도 ";
		int sptCode = Integer.parseInt(String.valueOf(stdLecCheck.get("SPT_CODE")));

		if (sptCode == 2 || sptCode == 4 || stdLecCheck.get("SPT_CODE") == null) {
			resStr += "미참여\n";
		} else {
			resStr += "참여(50만원)\n";
			pay += 50;
		}

		resStr = resStr + "따라서, " + pay + "만원 지급 예정 입니다.\n";

		atdRate = atdRateCheck(id);

		if (atdRate < 80) {
			resStr += "하지만, 출석률이 80%미만인 " + atdRate + "%이므로 수당 지급이 취소되었습니다.\n";
		} else {
			resStr += "\n";
		}

		PrintUtil.printThinOne("[ " + stdLecCheck.get("STD_NAME") + " ]" + resStr);

		ScanUtil.nextLine("<< 뒤로 가기");
		return View.STUDENT_LECTURE_CONTENT;
	}

	/** 출석률 계산 */
	public int atdRateCheck(String std_id) {
		int atdRate = 0; // 출석률
		int atdCount = 0; // 출석
		int tardyCount = 0; // 지각 또는 조퇴

		List<Map<String, Object>> atdCheck = atdDao.getAtdStatus(std_id);

		if (atdCheck == null) {
			// 수업을 듣지않는 사람
			return 100;
		}

		for (int i = 0; i < atdCheck.size(); i++) {
			Map<String, Object> res = atdCheck.get(i);
			String status = (String) res.get("ATD_STATUS");
			if (status.equals("출석")) {
				atdCount++;
			}
			if (status.equals("지각 또는 조퇴")) {
				tardyCount++;
			}
		}

		// 지각 또는 조퇴가 3번 이상이면 결석 1번으로 처리
		atdCount -= tardyCount / 3;

		// 출석일로 나눠서 계산
		Map<String, Object> resDate = dao.getAtdStatus(std_id);
		int date = Integer.parseInt(String.valueOf(resDate.get("COUNT(*)")));
		atdRate = atdCount / date ;
		
		if(atdCount == date) {
			atdRate = 100;
		}

		return atdRate;
	}
}

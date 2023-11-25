package service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import controller.Controller;
import dao.AcademyDAO;
import dao.LectureDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class LectureService {
	LectureDAO dao = LectureDAO.getInstance();
	AcademyDAO acdDAO = AcademyDAO.getInstance();
	AcademyService acdService = AcademyService.getInstance();
	LectureSortService lecSService = LectureSortService.getInstance();

	// 강좌의 수강료를 세자리 단위마다 ,로 구분하여 출력
	DecimalFormat decFormat = new DecimalFormat("###,###");

	// 날짜 패턴
	String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";

	String stdId;
	String acdId;

	// 싱글톤 패턴
	private static LectureService instance = null;

	private LectureService() {
	}

	public static LectureService getInstance() {
		if (instance == null)
			instance = new LectureService();
		return instance;
	}

	/** 강의 비용 조회 */
	public int lectureFee() {
		stdId = (String) Controller.sessionStorage.get("STD_ID");

		Map<String, Object> res = dao.getLecFee(stdId);

		if (res == null) {
			PrintUtil.printOne("수강하는 강좌가 존재하지않습니다.");
			PrintUtil.delay();

			return View.STUDENT_INFO;
		}

		String stdName = (String) res.get("STD_NAME");
		int sptCode = Integer.parseInt(String.valueOf(res.get("SPT_CODE")));
		int lecFee = Integer.parseInt(String.valueOf(res.get("LEC_FEE")));
		int preCount = Integer.parseInt(String.valueOf(res.get("STD_PRECOUNT")));
		String strFee = decFormat.format(lecFee) + "";
		String strSpt = "";

		if (sptCode == 1 || sptCode == 2) {
			if (preCount == 0) {
				strFee += " -> 0";
				strSpt += "전액 지원";
			} else if (preCount == 1) {
				strFee += " -> " + decFormat.format(Math.round(lecFee * 0.7));
				strSpt += "70% 지원";
			} else if (preCount == 2) {
				strFee += " -> " + decFormat.format(Math.round(lecFee * 0.5));
				strSpt += "50% 지원";
			} else
				strSpt += "";

			PrintUtil.lecFeeHead();
			PrintUtil.lecFeeData(res.get("LEC_CODE"), res.get("LEC_NAME"), strFee);
			PrintUtil.feePrint(stdName, preCount, strSpt);
		} else {
			PrintUtil.printOne("내일 배움 카드를 발급해주세요.");
			PrintUtil.delay();

			return View.STUDENT_INFO;
		}

		ScanUtil.nextLine("<< 뒤로 가기 ");
		return View.STUDENT_INFO;
	}

	/** 강좌 등록 */
	public int enrollLecture() {
		PrintUtil.printOne("[ 강좌 등록  ]");
		acdId = (String) Controller.sessionStorage.get("ACD_ID");

		Map<String, Object> lecCodeReturn = dao.lecCodeReturn();
		Object lecCode = lecCodeReturn.get("MAXVALUE");

		PrintUtil.littleSort();
		String sort = lecSService.lecSortChoice(ScanUtil.nextInt("번호 선택 >> "));

		// sort를 1.IT 형식으로 받아와 .을 기준으로 나눔
		String sortAry[] = sort.split(". ");
		int sortNum = Integer.parseInt(sortAry[0]);

		String lecName = ScanUtil.nextLine("강좌명 >> ");
		while (ScanUtil.strLengthCheck(lecName, 120) == false) {
			lecName = ScanUtil.nextLine("강의명 >> ");
		}

		int lecFee = ScanUtil.nextInt("수강비 >> ");
		String strLecFee = String.valueOf(lecFee);

		while (ScanUtil.inputCheck(strLecFee, "i", 10) == false) {
			System.out.println("10 글자 이하의 숫자만 가능합니다.");
			lecFee = ScanUtil.nextInt("수강비 >> ");
			strLecFee = String.valueOf(lecFee);
		}

		while (ScanUtil.intLengthCheck(lecFee, 10) == false) {
			System.out.println("10 글자 이하의 숫자만 가능합니다.");
			lecFee = ScanUtil.nextInt("수강비 >> ");
		}

		String startDate = ScanUtil.nextLine("강좌 시작일 >> ");
		while (!Pattern.matches(datePattern, startDate)) {
			System.out.println("년도(4자리)-월(2자리)-일(2자리) 형식을 지켜주세요");
			startDate = ScanUtil.nextLine("강좌 시작일 >> ");
		}

		String endDate = ScanUtil.nextLine("강좌 종료일 >> ");
		while (!Pattern.matches(datePattern, endDate)) {
			System.out.println("년도(4자리)-월(2자리)-일(2자리) 형식을 지켜주세요");
			endDate = ScanUtil.nextLine("강좌 종료일 >> ");
		}

		List<Object> param = new ArrayList<Object>();
		param.add(lecCode);
		param.add(sortNum);
		param.add(lecName);
		param.add(lecFee);
		param.add(startDate);
		param.add(endDate);
		param.add(acdId);

		int result = dao.enrollLecture(param);
		if (result > 0) {
			PrintUtil.printOne("강좌 등록이 완료되었습니다.");
		} else {
			PrintUtil.printOne("강좌 등록을 실패하였습니다.");
		}

		PrintUtil.delay();
		return View.ACADEMY_AFTERLOGIN;
	}

	/** 등록한 강좌 수정 */
	public int lectureUpdate() {
		String lecCode = "";
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("ACD_ID");

		lecCode = ScanUtil.nextLine("수정할 강좌의 코드 입력 >> ");
		Map<String, Object> getLecCode = dao.getLecCode(lecCode);

		if (getLecCode == null || !getLecCode.get("ACD_ID").equals(sessionId)) {
			System.out.println("해당 강좌가 존재하지 않거나 수정 권한이 없습니다.");

			return lectureUpdate();
		}

		if (lecCode.equals(getLecCode.get("LEC_CODE"))) {
			System.out.println("분류를 수정하시겠습니까?(y/n)");
			yesNo = ScanUtil.nextLine("입력 >> ");

			if (yesNo.equalsIgnoreCase("y")) {
				PrintUtil.littleSort();
				String sort = lecSService.lecSortChoice(ScanUtil.nextInt("번호 입력 >> "));

				// sort를 1.IT 형식으로 받아와 .을 기준으로 나눔
				String sortAry[] = sort.split(". ");
				int sortNum = Integer.parseInt(sortAry[0]);
				
				setString = setString + " SORT_CODE = '" + sortNum + "', ";
			}

			System.out.println("강의명을 수정하시겠습니까?(y/n)");
			yesNo = ScanUtil.nextLine("입력 >> ");

			if (yesNo.equalsIgnoreCase("y")) {
				String lecName = ScanUtil.nextLine("강의명 >> ");

				while (ScanUtil.strLengthCheck(lecName, 120) == false) {
					lecName = ScanUtil.nextLine("강의명 >> ");
				}

				setString = setString + " LEC_NAME = '" + lecName + "', ";
			}

			System.out.println("수강비를 수정하시겠습니까?(y/n)");
			System.out.print("입력 >> ");
			yesNo = ScanUtil.nextLine();
			if (yesNo.equalsIgnoreCase("y")) {
				int lecFee = ScanUtil.nextInt("수강비 >> ");
				String strLecFee = String.valueOf(lecFee);

				while (ScanUtil.inputCheck(strLecFee, "i", 10) == false) {
					System.out.println("10 글자 이하의 숫자만 가능합니다.");
					lecFee = ScanUtil.nextInt("수강비 >> ");
					strLecFee = String.valueOf(lecFee);
				}

				setString = setString + " LEC_FEE = " + lecFee + ", ";
			}

			System.out.println("시작일을 수정하시겠습니까?(y/n)");
			System.out.print("입력 >> ");
			yesNo = ScanUtil.nextLine();
			if (yesNo.equalsIgnoreCase("y")) {
				String startDate = ScanUtil.nextLine("강좌 시작일 >> ");
				while (!Pattern.matches(datePattern, startDate)) {
					System.out.println("년도(4자리)-월(2자리)-일(2자리) 형식을 지켜주세요");
					startDate = ScanUtil.nextLine("강좌 시작일 >> ");
				}

				setString = setString + " STARTDATE = '" + startDate + "', ";
			}

			System.out.println("종료일을 수정하시겠습니까?(y/n)");
			System.out.print("입력 >> ");
			yesNo = ScanUtil.nextLine();
			if (yesNo.equalsIgnoreCase("y")) {
				String endDate = ScanUtil.nextLine("강좌 종료일 >> ");
				while (!Pattern.matches(datePattern, endDate)) {
					System.out.println("년도(4자리)-월(2자리)-일(2자리) 형식을 지켜주세요");
					endDate = ScanUtil.nextLine("강좌 종료일 >> ");
				}
				setString = setString + " ENDDATE = '" + endDate + "', ";
			}
		}

		if (setString.equals("")) {
			PrintUtil.printOne("정보 수정을 취소하였습니다.");
			PrintUtil.delay();

			return View.ACADEMY_AFTERLOGIN;
		}

		setString = setString.substring(0, setString.length() - 2);

		List<Object> param = new ArrayList<Object>();
		param.add(lecCode);

		int result = dao.update(setString, param);

		if (result > 0) {
			PrintUtil.printOne("정보가 수정되었습니다.");
		} else {
			PrintUtil.printOne("정보 수정에 실패했습니다.");
		}

		PrintUtil.delay();
		return View.ACADEMY_AFTERLOGIN;
	}

	/** 등록한 강좌 삭제 */
	public int lectureDelete() {
		String lecCode = "";
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("ACD_ID");

		lecCode = ScanUtil.nextLine("삭제할 강좌의 코드 입력 >> ");
		Map<String, Object> getLecCode = dao.getLecCode(lecCode);

		if (getLecCode == null || !getLecCode.get("ACD_ID").equals(sessionId)) {
			System.out.println("해당 강좌가 존재하지 않거나 삭제 권한이 없습니다.");

			return lectureDelete();
		}

		if (lecCode.equals(getLecCode.get("LEC_CODE"))) {
			System.out.println("정말 삭제하시겠습니까?(y/n)");
			yesNo = ScanUtil.nextLine("입력 >> ");

			if (yesNo.equalsIgnoreCase("y")) {
				setString = setString + " LEC_CODE, SORT_CODE, LEC_NAME, LEC_FEE, STARTDATE, ENDDATE, ACD_ID ";
			} else {
				PrintUtil.printOne("강좌 삭제를 취소하였습니다.");
				return View.ACADEMY_AFTERLOGIN;
			}

			List<Object> param = new ArrayList<Object>();
			param.add(lecCode);

			int result = dao.delete(param);
			if (result > 0) {
				PrintUtil.printOne("강좌 삭제가 완료되었습니다.");
				Controller.sessionStorage.put("login", false);
			} else {
				PrintUtil.printOne("강좌 삭제에 실패했습니다.");
			}
		}
		return View.ACADEMY_AFTERLOGIN;
	}
}

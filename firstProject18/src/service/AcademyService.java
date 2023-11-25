package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import controller.Controller;
import dao.AcademyDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class AcademyService {
	AcademyDAO dao = AcademyDAO.getInstance();
	String acdId;

	// 전화번호 패턴
	// ^: 시작, \d: 0~9 사이의 숫자, {n}:n개, {n,m}:n개 이상 m개 이하 $:앞의 문자열로 문자가 끝나는지 검사
	String telPattern = "^\\d{2,3}-\\d{3,4}-\\d{4}$";

	// 싱글톤 패턴
	private static AcademyService instance = null;

	private AcademyService() {
	}

	public static AcademyService getInstance() {
		if (instance == null)
			instance = new AcademyService();
		return instance;
	}

	/** 아이디 중복 체크 */
	public String acdIdDuplicate() {
		while (true) {
			acdId = ScanUtil.nextLine("아이디 >> ");
			// 아이디 중복체크
			Map<String, Object> res = dao.getAcademyInfo(acdId);
			if (res != null) {
				System.out.println("이미 사용 중인 아이디입니다.");
			} else {
				break;
			}
		}

		return acdId;
	}

	/** 아이디 존재 여부 체크 */
	public String acdIdExist() {
		while (true) {
			acdId = ScanUtil.nextLine("아이디 >> ");
			// 아이디 중복체크
			Map<String, Object> res = dao.getAcademyInfo(acdId);
			if (res == null) {
				System.out.println("존재하지않는 회원 id입니다.");
			} else {
				break;
			}
		}

		return acdId;
	}

	/** 학원 로그인 */
	public int login() {
		if ((boolean) Controller.sessionStorage.get("login")) {
			PrintUtil.printOne("이미 로그인되어있습니다.");
			PrintUtil.delay();

			return View.ACADEMY_AFTERLOGIN;
		}

		PrintUtil.printOne("로그인");

		acdId = ScanUtil.nextLine("아이디 >> ");
		String pass = ScanUtil.nextLine("비밀번호 >> ");

		Map<String, Object> row = dao.login(acdId, pass);

		if (row == null) {
			PrintUtil.printOne("아이디가 없거나 비밀번호가 틀렸습니다.");
			PrintUtil.delay();

			return View.ACADEMY;
		} else {
			Controller.sessionStorage.put("login", true);
			Controller.sessionStorage.put("ACD_ID", acdId);

			return View.ACADEMY_AFTERLOGIN;
		}
	}

	/** 학원 회원가입 */
	public int signUp() {
		PrintUtil.printOne("회원가입");

		acdIdDuplicate();

		while (ScanUtil.inputCheck(acdId, "s", 25) == false) {
			System.out.println("25 글자 이하의 영어, 숫자만 입력 가능합니다.");
			acdId = ScanUtil.nextLine("아이디>> ");
		}

		String pass = ScanUtil.nextLine("비밀번호 >> ");
		while (ScanUtil.inputCheck(pass, "p", 30) == false) {
			System.out.println("30글자 이하의 영문자와 숫자, 특수문자(*, &, _)만 입력 가능합니다.");
			pass = ScanUtil.nextLine("비밀번호>> ");
		}

		String acdName = ScanUtil.nextLine("학원명 >> ");
		while (ScanUtil.inputCheck(acdName, "k", 20) == false) {
			System.out.println("한글 20글자 이하만 입력 가능합니다.");
			acdName = ScanUtil.nextLine("학원명 >> ");
		}

		String telNum = ScanUtil.nextLine("전화번호 >> ");
		while (!Pattern.matches(telPattern, telNum)) {
			System.out.println("올바른 전화 형식이 아닙니다.");
			System.out.println("2~3자리-3~4자리-4자리 형식을 지켜주세요.");
			telNum = ScanUtil.nextLine("전화번호 >>");
		}

		PrintUtil.littleLoc();
		String location = locChoice(ScanUtil.nextInt("번호 선택>> "));

		List<Object> param = new ArrayList<Object>();
		param.add(acdId);
		param.add(pass);
		param.add(acdName);
		param.add(telNum);
		param.add(location);

		int result = dao.signUp(param);
		if (result > 0) {
			PrintUtil.printOne("회원 가입이 완료되었습니다.");
		} else {
			PrintUtil.printOne("회원 가입을 실패하였습니다.");
		}
		
		PrintUtil.delay();
		return View.ACADEMY;
	}

	/** 학원 정보 출력 */
	public int academyInfoCheck() {
		String acd_id = (String) Controller.sessionStorage.get("ACD_ID");
		Map<String, Object> academyInfo = dao.getAcademyInfo(acd_id);

		PrintUtil.acdInfoCheck();
		PrintUtil.acdInfoData(academyInfo.get("ACD_ID"), academyInfo.get("ACD_NAME"), academyInfo.get("ACD_LOCATION"),
				academyInfo.get("ACD_TELNUM"));

		return View.ACADEMY_INFO;
	}

	/** 학원 정보 수정 */
	public int update() {
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("ACD_ID");

		acdIdExist();

		if (!acdId.equals(sessionId)) {
			System.out.println("아이디가 일치하지 않습니다.");
			PrintUtil.delay();
			return academyInfoCheck();
		}

		System.out.println("비밀번호를 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			String pass = ScanUtil.nextLine("비밀번호 >> ");
			while (ScanUtil.inputCheck(pass, "p", 30) == false) {
				System.out.println("30글자 이하의 영문자와 숫자, 특수문자(*, &, _)만 입력 가능합니다.");
				pass = ScanUtil.nextLine("비밀번호>> ");
			}
			setString = setString + " ACD_PASS = '" + pass + "', ";
		}

		System.out.println("학원명을 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			String acdName = ScanUtil.nextLine("학원명 >> ");
			while (ScanUtil.inputCheck(acdName, "k", 60) == false) {
				System.out.println("20 글자 이하의 한글만 입력 가능합니다.");
				acdName = ScanUtil.nextLine("학원명 >> ");
			}
			setString = setString + " ACD_NAME = '" + acdName + "', ";
		}

		System.out.println("지역을 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			PrintUtil.littleLoc();
			String loc = locChoice(ScanUtil.nextInt("선택 >> "));
			setString = setString + " ACD_LOCATION = '" + loc + "', ";
		}

		System.out.println("전화번호를 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			String acdTelNum = ScanUtil.nextLine("전화번호 >>");
			while (!Pattern.matches(acdTelNum, telPattern)) {
				System.out.println("올바른 전화 형식이 아닙니다.");
				System.out.println("2~3자리-3~4자리-4자리 형식을 지켜주세요.");
				acdTelNum = ScanUtil.nextLine("전화번호 >>");
			}

			setString = setString + " ACD_TELNUM = '" + acdTelNum + "', ";
		}

		if (setString.equals("")) {
			PrintUtil.printOne("정보 수정을 취소하였습니다.");
			PrintUtil.delay();

			return academyInfoCheck();
		}

		setString = setString.substring(0, setString.length() - 2);

		List<Object> param = new ArrayList<Object>();
		param.add(acdId);

		int result = dao.update(setString, param);

		if (result >= 0) {
			PrintUtil.printOne("정보가 수정되었습니다.");
		} else {
			PrintUtil.printOne("정보 수정에 실패했습니다.");
		}

		PrintUtil.delay();
		return academyInfoCheck();
	}

	/** 학원 정보 삭제(탈퇴) */
	public int delete() {
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("ACD_ID");

		PrintUtil.printOne("[ 회원 탈퇴  ]");

		acdIdExist();

		if (!acdId.equals(sessionId)) {
			System.out.println("아이디가 일치하지 않습니다.");
			PrintUtil.delay();
			return delete();
		}

		System.out.print("정말 탈퇴하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");

		if (yesNo.equalsIgnoreCase("y")) {
			setString = setString + " ACD_ID, ACD_PASS, ACD_NAME, ACD_LOCATION, ACD_TELNUM ";
		} else {
			PrintUtil.printOne("탈퇴를 취소하였습니다.");
			PrintUtil.delay();

			return academyInfoCheck();
		}

		List<Object> param = new ArrayList<Object>();
		param.add(acdId);

		int result = dao.delete(param);
		if (result > 0) {
			PrintUtil.printOne("회원 탈퇴가 완료되었습니다.");
		} else {
			PrintUtil.printOne("회원 탈퇴가 실패하였습니다.");
		}
		Controller.sessionStorage.put("login", false);

		PrintUtil.delay();
		return View.HOME;
	}

	/** 학원 지역 번호 선택 */
	public String locChoice(int locChoice) {
		while (ScanUtil.intLengthCheck(locChoice, 1) == false) {
			locChoice = ScanUtil.nextInt("번호 선택>> ");
		}

		while (true) {
			switch (locChoice) {
			case 1:
				return "서울";
			case 2:
				return "대전";
			case 3:
				return "광주";
			case 4:
				return "부산";
			case 5:
				return "대구";
			case 6:
				return "울산";
			default:
				System.out.println("1~6까지의 숫자를 입력해주세요.");
			}

			locChoice = ScanUtil.nextInt("번호 선택>> ");
		}
	}
}

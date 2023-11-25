package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.StudentDAO;
import dao.SupportSystemDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class StudentService {
	StudentDAO dao = StudentDAO.getInstance();
	SupportSystemDAO sptDAO = SupportSystemDAO.getInstance();
	String stdId;

	// 싱글톤 패턴
	private static StudentService instance = null;

	private StudentService() {
	}

	public static StudentService getInstance() {
		if (instance == null)
			instance = new StudentService();
		return instance;
	}

	/** 아이디 중복 체크 */
	public String idDuplicate() {
		while (true) {
			stdId = ScanUtil.nextLine("아이디>> ");
			// 아이디 중복체크
			Map<String, Object> res = dao.getStdInfoOne(stdId);
			if (res != null) {
				PrintUtil.printOne("이미 사용 중인 아이디입니다.");
			} else {
				break;
			}
		}

		return stdId;
	}

	/** 아이디 존재 여부 확인 */
	public String idExist() {
		while (true) {
			System.out.print("아이디 >> ");
			stdId = ScanUtil.nextLine();
			Map<String, Object> res = dao.getStdInfoOne(stdId);
			// 아이디 존재하는지 확인
			if (res == null) {
				PrintUtil.printOne("존재하지 않는 아이디입니다.");
			} else {
				break;
			}
		}

		return stdId;
	}

	/** 학생 로그인 */
	public int login() {
		PrintUtil.printOne("[ 로그인 ]");

		if ((boolean) Controller.sessionStorage.get("login")) {
			PrintUtil.printOne("이미 로그인되어있습니다.");
			PrintUtil.delay();

			return View.STUDENT_AFTERLOGIN;
		}

		stdId = ScanUtil.nextLine("아이디 >> ");
		String pass = ScanUtil.nextLine("비밀번호 >> ");

		Map<String, Object> row = dao.login(stdId, pass);

		if (row == null) {
			System.out.println("아이디가 없거나 비밀번호가 틀렸습니다.");
			PrintUtil.delay();

			return View.STUDENT;
		} else {
			Controller.sessionStorage.put("login", true);
			Controller.sessionStorage.put("STD_ID", stdId);

			PrintUtil.clearConsole();
			PrintUtil.var();

			return View.STUDENT_AFTERLOGIN;
		}
	}

	/** 학생 회원가입 */
	public int signUp() {
		PrintUtil.printOne("[ 회원가입 ]");

		idDuplicate();

		while (ScanUtil.inputCheck(stdId, "s", 25) == false) {
			System.out.println("25 글자 이하의 영어, 숫자만 입력 가능합니다.");
			stdId = ScanUtil.nextLine("아이디>> ");
		}

		String name = ScanUtil.nextLine("이름 >> ");
		while (ScanUtil.inputCheck(name, "k", 5) == false) {
			System.out.println("5글자 이하의 한글만 입력 가능합니다.");
			name = ScanUtil.nextLine("이름 >> ");
		}

		String pass = ScanUtil.nextLine("비밀번호 >> ");
		while (ScanUtil.inputCheck(pass, "p", 30) == false) {
			System.out.println("30글자 이하의 영문자와 숫자, 특수문자(*, &, _)만 입력 가능합니다.");
			pass = ScanUtil.nextLine("비밀번호>> ");
		}

		int preCount = preCountChoice(ScanUtil.nextInt("이전 수강 횟수>> "));

		PrintUtil.supportSystemList();
		int sptChoice = supportChoice(ScanUtil.nextInt("번호 선택>> "));

		List<Object> param = new ArrayList<Object>();
		param.add(stdId);
		param.add(name);
		param.add(pass);
		param.add(preCount);
		param.add(sptChoice);

		int result = dao.signUp(param);
		if (result > 0) {
			PrintUtil.printOne("회원 가입이 완료되었습니다.");
		} else {
			PrintUtil.printOne("회원 가입을 실패하였습니다.");
		}

		PrintUtil.delay();
		return View.STUDENT;
	}

	/** 학생 정보 확인 */
	public int studentInfoCheck() {
		stdId = (String) Controller.sessionStorage.get("STD_ID");
		Map<String, Object> studentInfo = dao.getStdInfoOne(stdId);
		List<Map<String, Object>> getSptName = sptDAO.getSptName();

		int sptCode = Integer.parseInt(String.valueOf(studentInfo.get("SPT_CODE")));
		// 오라클은 1부터 시작하기 때문에 -1을 해줘야 맞는 값이 나옴
		Map<String, Object> res = getSptName.get(sptCode - 1);

		PrintUtil.studentInfoCheck();
		PrintUtil.stdInfoData(studentInfo.get("STD_ID"), studentInfo.get("STD_NAME"), studentInfo.get("STD_PRECOUNT"),
				res.get("SPT_NAME"));

		return View.STUDENT_INFO_CHECK;
	}

	/** 학생 정보 수정 public int update(String setString, List<Object> param) 사용 */
	public int update() {
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("STD_ID");

		idExist();

		if (!stdId.equals(sessionId)) {
			System.out.println("아이디가 일치하지 않습니다.");

			return update();
		}

		System.out.println("비밀번호를 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			String pass = ScanUtil.nextLine("비밀번호 >> ");
			while (ScanUtil.inputCheck(pass, "p", 30) == false) {
				System.out.println("30글자 이하의 영문자와 숫자, 특수문자(*, &, _)만 입력 가능합니다.");
				pass = ScanUtil.nextLine("비밀번호>> ");
			}
			setString = setString + " STD_PASS = '" + pass + "', ";
		}

		System.out.println("이름을 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			String stdName = ScanUtil.nextLine("이름 >> ");
			while (ScanUtil.inputCheck(stdName, "k", 15) == false) {
				System.out.println("5 글자 이하의 한글만 입력 가능합니다.");
				stdName = ScanUtil.nextLine("이름 >> ");
			}
			setString = setString + " STD_NAME = '" + stdName + "', ";
		}

		System.out.println("이전 수강 횟수를 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			int preCount = preCountChoice(ScanUtil.nextInt("이전 수강 횟수 >> "));

			setString = setString + " STD_PRECOUNT = " + preCount + ", ";
		}

		System.out.println("지원제도 참여 여부를 수정하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");
		if (yesNo.equalsIgnoreCase("y")) {
			PrintUtil.supportSystemList();
			int sptChoice = supportChoice(ScanUtil.nextInt("지원제도(1~4 중 선택) >> "));

			setString = setString + " SPT_CODE = " + sptChoice + ", ";
		}

		if (setString.equals("")) {
			PrintUtil.printOne("정보 수정을 취소하였습니다.");
			PrintUtil.delay();

			return studentInfoCheck();
		}

		setString = setString.substring(0, setString.length() - 2);
		List<Object> param = new ArrayList<Object>();
		param.add(stdId);

		int result = dao.update(setString, param);

		if (result >= 0) {
			PrintUtil.printOne("정보가 수정되었습니다.");
		} else {
			PrintUtil.printOne("정보 수정에 실패했습니다.");
		}

		PrintUtil.delay();
		return studentInfoCheck();
	}

	/** 학생 정보 삭제(탈퇴) */
	public int delete() {
		String yesNo = "";
		String setString = "";
		String sessionId = (String) Controller.sessionStorage.get("STD_ID");

		PrintUtil.printOne("[ 회원 탈퇴 ]");

		idExist();

		if (!stdId.equals(sessionId)) {
			System.out.println("아이디가 일치하지 않습니다.");

			return delete();
		}

		System.out.print("정말 탈퇴하시겠습니까?(y/n)");
		yesNo = ScanUtil.nextLine("입력 >> ");

		if (yesNo.equalsIgnoreCase("y")) {
			setString = setString + " STD_ID, STD_PASS, STD_NAME, STD_PRECOUNT, SPT_CODE ";
		} else {
			PrintUtil.printOne("탈퇴를 취소하였습니다.");
			PrintUtil.delay();

			return studentInfoCheck();
		}
		
		List<Object> param = new ArrayList<Object>();
		param.add(stdId);
		
		int result = dao.delete(param);
		if (result > 0) {
			PrintUtil.printOne("회원 탈퇴가 완료되었습니다.");
		} else {
			PrintUtil.printOne("회원 탈퇴를 실패했습니다.");
		}
		Controller.sessionStorage.put("login", false);

		PrintUtil.delay();
		return View.HOME;
	}

	/** 학생 이전 수강 횟수 선택 */
	public int preCountChoice(int preCount) {
		while (ScanUtil.intLengthCheck(preCount, 1) == false) {
			preCount = ScanUtil.nextInt("이전 수강 횟수>> ");
		}

		switch (preCount) {
		case 0:
			preCount = 0;
			break;
		case 1:
			preCount = 1;
			break;
		case 2:
			preCount = 2;
			break;
		default:
			preCount = 3;
		}

		return preCount;
	}

	/** 학생 지원 유형 번호 선택 */
	public int supportChoice(int sptChoice) {
		while (ScanUtil.intLengthCheck(sptChoice, 1) == false) {
			sptChoice = ScanUtil.nextInt("번호 선택>> ");
		}

		while (true) {
			switch (sptChoice) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			default:
				System.out.println("1~4까지의 숫자를 입력해주세요.");
			}

			sptChoice = ScanUtil.nextInt("번호 선택>> ");
		}
	}
}

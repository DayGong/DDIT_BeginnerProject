package controller;

import java.util.HashMap;
import java.util.Map;

import service.AcademyService;
import service.AttendanceService;
import service.LectureManagementService;
import service.LectureService;
import service.LectureSortService;
import service.StudentService;
import service.SupportSystemService;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class Controller {
	// 세션
	static public Map<String, Object> sessionStorage = new HashMap<>();
	int view = View.HOME;

	// 필요한 서비스 클래스 객체 생성
	// getInstance()로 만들어지는 것은 싱글톤 패턴
	StudentService studentService = StudentService.getInstance();
	LectureService lectureService = LectureService.getInstance();
	AttendanceService attendanceService = AttendanceService.getInstance();
	SupportSystemService supportSystemService = SupportSystemService.getInstance();
	AcademyService academyService = AcademyService.getInstance();
	LectureSortService lectureSortService = LectureSortService.getInstance();
	LectureManagementService lectureMService = LectureManagementService.getInstance();

	public static void main(String[] args) {
		new Controller().start();
	}

	private void start() {
		sessionStorage.put("login", false); // false: 로그인 안됨

		while (true) {
			switch (view) {
			// 첫 화면
			case View.HOME:
				view = home();
				break;

			// 학생 로그인, 회원가입 페이지로 이동
			case View.STUDENT:
				view = stdMenu();
				break;

			// 학생이 로그인한 후 화면 (1.강좌 검색 2.훈련 내역 3.내 정보)
			case View.STUDENT_AFTERLOGIN:
				view = stdAfterLoginMenu();
				break;

			// 학생 검색 메뉴 (1.지역별 검색 2.분류별 검색)으로 이동
			case View.STUDENT_SEARCH:
				view = lectureSearchMenu();
				break;

			// 학생 훈련 내역 메뉴 (1.입실 2.퇴실 3.출석 확인 4.수당 조회)
			case View.STUDENT_LECTURE_CONTENT:
				view = lectureContentMenu();
				break;

			// 학생 내정보 (1.내 정보 확인 2.강의 비용 조회)
			case View.STUDENT_INFO:
				view = stdInfoMenu();
				break;

			// 학생 정보 출력 후 선택지 (1.수정 2.탈퇴)
			case View.STUDENT_INFO_CHECK:
				view = stdInfoChoiceMenu();
				break;

			// 학원 로그인 메뉴 (1.로그인 2.회원가입)
			case View.ACADEMY:
				view = acdMenu();
				break;

			// 학원 로그인 후 화면 (1.학원의 강좌 검색 2.강좌 등록 3.학원 정보)
			case View.ACADEMY_AFTERLOGIN:
				view = acdAfterLoginMenu();
				break;

			// 학원 정보 출력 후 선택지 (1.수정 2.탈퇴)
			case View.ACADEMY_INFO:
				view = academyInfoChoiceMenu();
				break;

			// 학원 강좌 목록 출력 후 선택지 (1.수정 2.삭제)
			case View.ACADEMY_LECTURE_CHECK:
				view = lectureChoiceMenu();
				break;
			}
		}
	}

	/** View.HOME 첫 화면(1.학생 2.학원 선택 0.종료) */
	private int home() {
		PrintUtil.home();

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			return View.STUDENT;
		case 2:
			return View.ACADEMY;
		case 0:
			System.exit(view);
		default:
			return View.HOME;
		}
	}

	/** 학생 로그인 메뉴 (1.로그인 2.회원가입 3.로그아웃) */
	private int stdMenu() {
		PrintUtil.stdMenu();

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = studentService.login();
			break;
		case 2:
			view = studentService.signUp();
			break;
		case 3:
			Controller.sessionStorage.put("login", false);
			PrintUtil.printOne("로그아웃 되었습니다.");
			PrintUtil.delay();
		case 0:
			return View.HOME;
		default:
			return View.STUDENT;
		}
		return view;
	}

	/** 학생이 로그인한 후 화면 (1.강좌 검색 2.훈련 내역 3.내 정보) */
	public int stdAfterLoginMenu() {
		String id = (String) Controller.sessionStorage.get("STD_ID");
		PrintUtil.stdAfterLogin(id);

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			return View.STUDENT_SEARCH;
		case 2:
			return View.STUDENT_LECTURE_CONTENT;
		case 3:
			return View.STUDENT_INFO;
		case 0:
			return View.STUDENT;
		default:
			return View.STUDENT_AFTERLOGIN;
		}
	}

	/** 학생 강좌 검색 (1.지역별 검색 2.분류별 검색 0.뒤로가기) */
	private int lectureSearchMenu() {
		PrintUtil.lecSerch();

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = lectureSortService.location();
			break;
		case 2:
			view = lectureSortService.sort();
			break;
		case 0:
			return View.STUDENT_AFTERLOGIN;
		default:
			return View.STUDENT_SEARCH;
		}
		return view;
	}

	/** 학생 훈련 내역 메뉴 1.입실/퇴실 2.출석 확인 3.수당 조회 0.뒤로가기 */
	public int lectureContentMenu() {
		PrintUtil.lectureContentMenu();

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = attendanceService.enable();
			break;
		case 2:
			view = attendanceService.status();
			break;
		case 3:
			view = supportSystemService.benefit();
			break;
		case 0:
			return View.STUDENT_AFTERLOGIN;
		default:
			return View.STUDENT_LECTURE_CONTENT;
		}
		return view;
	}

	/** 학생 내정보 1.내 정보 조회 2.강의 비용 조회 */
	public int stdInfoMenu() {
		PrintUtil.stdInfoMenu();

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = studentService.studentInfoCheck();
			break;
		case 2:
			view = lectureService.lectureFee();
			break;
		case 0:
			return View.STUDENT_AFTERLOGIN;
		default:
			return View.STUDENT_INFO;
		}
		return view;
	}

	/** 학생 정보 출력 후 선택지 1.수정 2.탈퇴 */
	public int stdInfoChoiceMenu() {
		PrintUtil.noClrUpVar("1.내 정보 수정   2.회원 탈퇴   0.뒤로 가기");
		
		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = studentService.update();
			break;
		case 2:
			view = studentService.delete();
			break;
		case 0:
			return View.STUDENT_INFO;
		default:
			return View.STUDENT_INFO_CHECK;
		}
		return view;
	}

	/** 학원 로그인 메뉴 (1.로그인 2.회원가입) */
	private int acdMenu() {
		PrintUtil.acdMenu();
		
		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = academyService.login();
			break;
		case 2:
			view = academyService.signUp();
			break;
		case 3:
			Controller.sessionStorage.put("login", false);
			PrintUtil.printOne("로그아웃 되었습니다.");
			PrintUtil.delay();
		case 0:
			return View.HOME;
		default:
			return View.ACADEMY;
		}
		return view;
	}

	/** 학원 로그인 후 화면 (1.등록 강좌 조회 2.강좌 등록 3.학원 정보) */
	public int acdAfterLoginMenu() {
		String acdId = (String) Controller.sessionStorage.get("ACD_ID");
		System.out.println(acdId);
		PrintUtil.acdAfterLogin(acdId);

		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = lectureSortService.academyLectureSearch();
			break;
		case 2:
			view = lectureService.enrollLecture();
			break;
		case 3:
			view = academyService.academyInfoCheck();
			break;
		case 0:
			return View.ACADEMY;
		default:
			return View.ACADEMY_AFTERLOGIN;
		}
		return view;
	}

	/** 학원 정보 출력 후 선택지 1.수정 2.탈퇴 */
	public int academyInfoChoiceMenu() {
		PrintUtil.noClrUpVar("1.학원 정보 수정   2.회원 탈퇴   0.뒤로 가기");
		
		switch (ScanUtil.nextInt("번호입력>> ")) {
		case 1:
			view = academyService.update();
			break;
		case 2:
			view = academyService.delete();
			break;
		case 0:
			return View.ACADEMY_AFTERLOGIN;
		default:
			return View.ACADEMY_INFO;
		}
		return view;
	}

	/** 학원 강좌 목록 띄우고 난 후 선택지 1.수정 2.삭제 */
	public int lectureChoiceMenu() {
		PrintUtil.noClrUpVar("1.강좌 정보 수정   2.강좌 삭제   0.뒤로 가기");
		
		switch (ScanUtil.nextInt("번호 입력 >> ")) {
		case 1:
			view = lectureService.lectureUpdate();
			break;
		case 2:
			view = lectureService.lectureDelete();
			break;
		case 0:
			return View.ACADEMY_AFTERLOGIN;
		default:
			return View.ACADEMY_LECTURE_CHECK;
		}
		return view;
	}
}

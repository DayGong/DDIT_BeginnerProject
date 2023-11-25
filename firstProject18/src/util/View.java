package util;

public interface View {
	/** 1.학생, 2.학원 선택 화면 */
	int HOME = 1;

	/** 학생 (1.로그인 2.회원가입) */
	int STUDENT = 2;
	/** 학생이 로그인한 후 화면 (1.강좌 검색 2.훈련 내역 3.내 정보) */
	int STUDENT_AFTERLOGIN = 21;

	/** 학생 검색 메인 */
	int STUDENT_SEARCH = 211;
	/** 학생 훈련 내역 선택지 */
	int STUDENT_LECTURE_CONTENT = 212;
	/** 내 정보 선택지 (1.내 정보 확인 2.강의 비용 조회) */
	int STUDENT_INFO = 213;

	/** 학생 정보 확인 후 선택지 (1.수정 2.삭제) */
	int STUDENT_INFO_CHECK = 2131;

	/** 학원 로그인 메뉴 (1.로그인 2.회원가입) */
	int ACADEMY = 3;
	/** 학원 로그인 후 화면 (1.등록 강좌 조회 2.강좌 등록 3.학원 정보 4.뒤로가기) */
	int ACADEMY_AFTERLOGIN = 31;
	/** 학원 정보 출력 후 선택지 (1.수정 2.탈퇴) */
	int ACADEMY_INFO = 32;

	/** 학원 강좌 목록 출력 후 선택지 (1.수정 2.삭제) */
	int ACADEMY_LECTURE_CHECK = 311;
}

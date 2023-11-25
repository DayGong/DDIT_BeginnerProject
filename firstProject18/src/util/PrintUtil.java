package util;

import java.util.concurrent.TimeUnit;

public class PrintUtil {
	/**
	 * System.out.printf()을 이용하면서 한글 공백처리에 대한 불편함을 개선한 메서드
	 * 
	 * @param format      : System.out.printf()에 입력하는 format과 비슷한 모양. 단, %{int}o 로
	 *                    입력하여 정수, 문자열을 구분하지 않는다. 음수를 입력하면 좌측정렬, 양수를 입력하면 우측정렬이다.
	 * @param spaceString : 빈 공간을 채워넣을 문자열 입력. null을 입력하면 기본값 " "이 들어간다.
	 * @param args        : format에서 %{int}o 부분을 채워줄 값을 순서대로 입력해야한다.
	 */
	public static void printf(String format, String spaceString, Object... args) {
		if (spaceString == null)
			spaceString = " ";
		String str = "";
		int idx = 0;
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			String f = "";
			if (c == '%') {
				c = format.charAt(++i);
				while (c != 'o' && i < format.length() - 1) {
					f += c;
					c = format.charAt(++i);
				}
				str += formater(args[idx++].toString(), Integer.parseInt(f), spaceString);
			} else {
				str += c;
			}
		}
		System.out.println(str);
	}

	private static String formater(String str, int length, String spaceString) {
		String result = "";
		int s = Math.abs(length);
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 'ㄱ' && str.charAt(i) <= '힇') {
				s -= 2;
			} else {
				s--;
			}
		}
		if (length < 0) {
			result += str;
			result = setSpace(result, s, spaceString);
		} else {
			result = setSpace(result, s, spaceString);
			result += str;
		}
		return result;
	}

	private static String setSpace(String str, int count, String spaceString) {
		for (int i = 0; i < count; i++) {
			str += spaceString;
		}
		return str;
	}

	public static void var() {
		System.out.println("======================================================================================================");
	}

	public static void thinVar() {
		System.out.println("------------------------------------------------------------------------------------------------------");
	}

	private static void line(int line) {
		for (int i = 0; i < line; i++) {
			System.out.println();
		}
	}
	
	public static void listLine(int data, int line) {
		if(data < line) {
			line(line-data);
			var();
		} else {
			var();
		}
	}

	public static void delay() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void clearConsole() {
		try {
			String operatingSystem = System.getProperty("os.name"); // Check the current operating system

			if (operatingSystem.contains("Windows")) {
				ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
				Process startProcess = pb.inheritIO().start();
				startProcess.waitFor();
			} else {
				ProcessBuilder pb = new ProcessBuilder("clear");
				Process startProcess = pb.inheritIO().start();

				startProcess.waitFor();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void printOne(String text) {
		clearConsole();
		var();
		line(6);
		System.out.println(text);
		line(7);
		var();
	}

	public static void printThinOne(String text) {
		clearConsole();
		var();
		line(3);
		System.out.println(text);
		line(4);
		var();
	}

	public static void underThinVar(String text) {
		System.out.println(text);
		thinVar();
	}

	public static void printThinOneNoClr(String text) {
		var();
		line(2);
		System.out.println(text);
		line(2);
		var();
	}

	public static void printTwo(String text1, String text2) {
		clearConsole();
		var();
		line(5);
		System.out.println(text1);
		line(2);
		System.out.println(text2);
		line(5);
		var();
	}

	public static void noClrUpVar(String text) {
		line(1);
		System.out.println(text);
		line(1);
		var();
	}

	public static void home() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 국비지원교육 관리 시스템 ]");
		line(2);
		System.out.println("1.학생 ");
		line(2);
		System.out.println("2.학원");
		line(2);
		System.out.println("0.종료");
		line(2);
		var();
	}

	public static void stdMenu() {
		clearConsole();
		var();
		line(1);
		System.out.println("[ 학생 페이지 ]");
		line(1);
		System.out.println("1.로그인");
		line(2);
		System.out.println("2.회원가입");
		line(2);
		System.out.println("3.로그아웃");
		line(2);
		System.out.println("0.홈으로");
		line(1);
		var();
	}

	public static void acdMenu() {
		clearConsole();
		var();
		line(1);
		System.out.println("[ 학원 페이지  ]");
		line(1);
		System.out.println("1.로그인 ");
		line(2);
		System.out.println("2.회원가입");
		line(2);
		System.out.println("3.로그아웃");
		line(2);
		System.out.println("0.홈으로");
		line(1);
		var();
	}

	public static void stdAfterLogin(Object name) {
		clearConsole();
		var();
		line(1);
		System.out.println("[ " + name + "님 환영합니다! ]");
		line(1);
		System.out.println("1.강좌 검색");
		line(2);
		System.out.println("2.훈련 내역");
		line(2);
		System.out.println("3.내 정보 조회");
		line(2);
		System.out.println("0.뒤로가기");
		line(1);
		var();
	}

	public static void acdAfterLogin(Object name) {
		clearConsole();
		var();
		line(1);
		System.out.println("[ " + name + "님 환영합니다! ]");
		line(1);
		System.out.println("1.등록 강좌 조회");
		line(2);
		System.out.println("2.강좌 등록");
		line(2);
		System.out.println("3.학원 정보");
		line(2);
		System.out.println("0.뒤로가기");
		line(1);
		var();
	}

	public static void lectureContentMenu() {
		clearConsole();
		var();
		line(1);
		System.out.println("[ 훈련 내역 ]");
		line(1);
		System.out.println("1.입실/퇴실");
		line(2);
		System.out.println("2.출석 확인");
		line(2);
		System.out.println("3.수당 조회");
		line(2);
		System.out.println("0.뒤로가기");
		line(1);
		var();
	}

	public static void stdInfoMenu() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 내 정보  ]");
		line(2);
		System.out.println("1.내 정보 조회");
		line(2);
		System.out.println("2.수강 중인 강의 및 수강비 조회");
		line(2);
		System.out.println("0.뒤로가기");
		line(2);
		var();
	}

	public static void lecSerch() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 강좌 검색 ]");
		line(2);
		System.out.println("1.지역별 검색");
		line(2);
		System.out.println("2.분류별 검색");
		line(2);
		System.out.println("0.뒤로가기");
		line(2);
		var();
	}

	public static void feePrint(String stdName, int preCount, String sptFee) {
		line(1);
		if (preCount >= 3) {
			System.out.println(stdName + "님은 지원 대상자가 아닙니다.");
		} else {
			System.out.println(stdName + "님은 이전에 " + preCount + "회 수강하였으므로 수강비 " + sptFee + " 대상자입니다.");
		}
		line(1);
		var();
	}

	public static void location(String text) {
		clearConsole();
		var();
		line(4);
		System.out.println("[ " + text + " ]");
		line(3);
		printf("%1o %15o %15o %15o %15o %15o", " ", "1.서울", "2.대전", "3.광주", "4.부산", "5.대구", "6.울산");
		line(5);
		var();
	}
	
	public static void littleLoc() {
		var();
		line(1);
		System.out.println("[ 지역 분류 ]");
		line(1);
		printf("%1o %15o %15o %15o %15o %15o", " ", "1.서울", "2.대전", "3.광주", "4.부산", "5.대구", "6.울산");
		line(1);
		var();
	}

	public static void sort(String text) {
		clearConsole();
		var();
		line(4);
		System.out.println("[ " + text + " ]");
		line(3);
		printf("%1o %20o %20o %20o %20o", " ", "1.IT", "2.경영", "3.금융", "4.보건", "5.문화");
		line(5);
		var();
	}
	
	public static void littleSort() {
		var();
		line(1);
		System.out.println("[ 강좌 분류 ]");
		line(1);
		printf("%1o %20o %20o %20o %20o", " ", "1.IT", "2.경영", "3.금융", "4.보건", "5.문화");
		line(1);
		var();
	}

	public static void lecLoc(String lecSearch, String type) {
		clearConsole();
		var();
		if (type.equals("loc")) {
			System.out.println("[ " + lecSearch + " ]에 위치한 학원의 강좌 검색");
		} else {
			System.out.println("[ " + lecSearch + " ]에 해당하는 분류의 강좌 검색");
		}
		var();
	}

	public static void supportSystemList() {
		line(1);
		System.out.println("[ 지원 제도 참여 여부 ]");
		line(1);
		System.out.println("1.내일배움카드 발급 & 국민취업제도 참여");
		line(1);
		System.out.println("2.내일배움카드 발급 & 국민취업제도 미참여");
		line(1);
		System.out.println("3.내일배움카드 미발급 & 국민취업제도 참여");
		line(1);
		System.out.println("4.내일배움카드 미발급 & 국민취업제도 미참여");
		line(1);
	}

	public static void studentInfoCheck() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 내 정보 조회  ]");
		line(1);
		thinVar();
		printf("%10o %13o %22o %20o", " ", "아이디", "이름", "이전수강횟수", "지원 제도");
		thinVar();
	}

	public static void stdInfoData(Object stdId, Object stdName, Object stdPreCt, Object sptName) {
		printf("%10o %14o %9o %59o", " ", stdId, stdName, stdPreCt, sptName);
		thinVar();
		line(2);
	}

	public static void acdInfoCheck() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 학원 정보 조회  ]");
		line(1);
		thinVar();
		printf("%15o %20o %20o %25o", " ", "학원 아이디", "학원명", "지역", "학원 전화번호");
		thinVar();
	}

	public static void acdInfoData(Object acdId, Object acdName, Object acdLoc, Object acdTel) {
		printf("%10o %25o   %18o %25o", " ", acdId, acdName, acdLoc, acdTel);
		thinVar();
		line(2);
	}

	public static void lecSearchListHead() {
		printf("%4o %20o %25o %18o     %15o", " ", "강좌번호", "강좌명", "수강료", "수강일", "학원명");
		var();
	}
	
	public static void lecSearchListData(Object lecCode, Object lecName, String fee, Object stDate, Object edDate, Object acdName) {
		printf("%5o      %-33o %12o원   %10o~%10o %10o", " ", 
				lecCode, lecName, fee, stDate, edDate, acdName);
		thinVar();
	}

	public static void acdLecListHead() {
		clearConsole();
		var();
		System.out.println("[ 강좌 목록 ]");
		var();
		printf("%8o %13o %20o %20o   %15o", " ", "강좌번호", "분류명", "강좌명", "수강비", "수강일");
		var();
	}

	public static void acdLecListData(Object lecCode, Object sortName, Object lecName, Object lecFee, Object stDate,
			Object edDate) {
		printf("%8o %10o     %-30o %10o원   %10o~%10o", " ", lecCode, sortName, lecName, lecFee, stDate, edDate);
		thinVar();
	}

	public static void getLecDetail(String lecCode, Object lecName, Object sortName, String fee, Object stDate,
			Object endDate, Object acdName, Object acdloc, Object acdTel) {
		clearConsole();
		var();
		System.out.println("[" + lecCode + "] 강좌 상세 조회");
		var();
		System.out.println("강좌번호      | " + lecCode);
		thinVar();
		System.out.println("강좌명        | " + lecName);
		thinVar();
		System.out.println("분류명        | " + sortName);
		thinVar();
		System.out.println("수강료        | " + fee);
		thinVar();
		System.out.println("수강일        | " + stDate + " ~ " + endDate);
		thinVar();
		System.out.println("학원명        | " + acdName);
		thinVar();
		System.out.println("지역          | " + acdloc);
		thinVar();
		System.out.println("학원전화번호  | " + acdTel);
		var();
	}

	public static void statusHead() {
		clearConsole();
		var();
		System.out.println("[ 출결 확인 ]");
		var();
		printf("%17o %23o %20o   %21o", " ", "일자", "입실시간", "퇴실시간", "출결상태");
		var();
	}

	public static void statusData(Object date, Object inTime, Object outTime, Object status) {
		printf("%20o %20o %20o %25o", " ", date, inTime, outTime, status);
	}

	public static void atdDetailHead() {
		System.out.println("[ 세부 내역 ]");
		var();
		printf("%20o %30o %20o", " ", "출석", "지각 또는 조퇴", "결석");
		var();
	}

	public static void atdDetailData(int atdCount, int tardyCount, int absCount) {
		printf("%19o %25o %25o", " ", atdCount, tardyCount, absCount);
		var();
	}

	public static void lecFeeHead() {
		clearConsole();
		var();
		line(2);
		System.out.println("[ 수강 중인 강의 및 수강비 조회   ]");
		line(2);
		thinVar();
		printf("%15o %20o %30o", " ", "강좌 코드", "강좌명", "수강비");
		thinVar();
	}

	public static void lecFeeData(Object lecCode, Object lecName, String lecFee) {
		printf("%15o %-30o %20o", " ", lecCode, lecName, lecFee);
		thinVar();
		line(1);
	}

}

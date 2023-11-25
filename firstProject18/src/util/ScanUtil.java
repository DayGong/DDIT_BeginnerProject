package util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ScanUtil {
	// 스캐너를 손쉽게 사용할 수 있는 static 메서드를 가지고 있음
	static Scanner sc = new Scanner(System.in);

	public static String nextLine() {
		return sc.nextLine();
	}

	public static String nextLine(String text) {
		System.out.print(text);
		return sc.nextLine();
	}

	public static int nextInt() {
		while (true) {
			try {
				int result = Integer.parseInt(sc.nextLine());
				return result;
			} catch (NumberFormatException e) {
				System.out.println("잘못 입력하였습니다.");
			}
		}
	}

	public static int nextInt(String text) {
		System.out.print(text);
		while (true) {
			try {
				int result = Integer.parseInt(sc.nextLine());
				return result;
			} catch (NumberFormatException e) {
				System.out.println("잘못 입력하였습니다.");
				System.out.print(text);
			}
		}
	}

	/** null 검사 */
	public static boolean nullCheck(String textInput) {
		boolean nullFlag = true;

		if (textInput.equals("")) {
			System.out.println("공백 입력 불가");
			nullFlag = false;
		}

		return nullFlag;
	}

	/** flag 검사 */
	public static boolean flagCheck(boolean firstFlag, boolean secondflag) {
		if (firstFlag == true && secondflag == true) {
			secondflag = true;
		} else {
			secondflag = false;
		}
		return secondflag;
	}

	/** 영문자와 숫자, 특수문자(*, &, _)만 입력 가능 */
	public static boolean inputPassCheck(String textInput) {
		char chrInput;
		boolean flag = true;
		boolean nullFlag = true;

		nullFlag = nullCheck(textInput);

		for (int i = 0; i < textInput.length(); i++) {
			chrInput = textInput.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크

			if (chrInput >= 0x61 && chrInput <= 0x7A) {
				// 영문(소문자)
			} else if (chrInput >= 0x41 && chrInput <= 0x5A) {
				// 영문(대문자) OK!
			} else if (chrInput >= 0x30 && chrInput <= 0x39) {
				// 숫자 OK!
			} else if (chrInput == 0x2A || chrInput == 0x26 || chrInput == 0x5F) {
				// 특수문자(*, &, _) OK!
			} else {
				flag = false; // 영문자도 아니고 숫자도 아님!
			}
		}

		flag = flagCheck(nullFlag, flag);
		return flag;
	}

	/** 한글 검사 */
	public static boolean inputKoreanCheck(String textInput) {
		boolean flag = true;
		boolean nullFlag = true;

		nullFlag = nullCheck(textInput);

		if (Pattern.matches("^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$", textInput)) {
			// 한글이 포함된 문자열
		} else {
			flag = false;
		}

		flag = flagCheck(nullFlag, flag);
		return flag;
	}

	/** 영문자와 숫자만 입력 가능 */
	public static boolean inputStringCheck(String textInput) {
		char chrInput;
		boolean flag = true;
		boolean nullFlag = true;

		nullFlag = nullCheck(textInput);

		for (int i = 0; i < textInput.length(); i++) {
			chrInput = textInput.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크

			if (chrInput >= 0x61 && chrInput <= 0x7A) {
				// 영문(소문자) OK!
			} else if (chrInput >= 0x41 && chrInput <= 0x5A) {
				// 영문(대문자) OK!
			} else if (chrInput >= 0x30 && chrInput <= 0x39) {
				// 숫자 OK!
			} else {
				flag = false; // 영문자도 아니고 숫자도 아님!
			}
		}

		flag = flagCheck(nullFlag, flag);
		return flag;
	}

	/** 숫자 수 입력 제한 */
	public static boolean intLengthCheck(int count, int length) {
		boolean flag = true;
		
		String strCount = String.valueOf(count);

		if (strCount.length() > length) {
			System.out.println("글자 수 입력 제한을 초과하였습니다.");
			flag = false;
		}

		return flag;
	}

	/** 글자 수 입력 제한 */
	public static boolean strLengthCheck(String count, int length) {
		boolean flag = true;

		if (count.length() > length) {
			System.out.println("글자 수 입력 제한을 초과하였습니다.");
			flag = false;
		}

		return flag;
	}

	/** 글자 검사 */
	public static boolean inputCheck(String textInput, String type, int length) {
		boolean flag = true;
		boolean lengflag = true;

		switch (type) {
		case "s":
			flag = inputStringCheck(textInput);
			break;
		case "p":
			flag = inputPassCheck(textInput);
			break;
		case "k":
			flag = inputKoreanCheck(textInput);
			break;
		default:
		}

		lengflag = strLengthCheck(textInput, length);
		flag = flagCheck(lengflag, flag);

		return flag;
	}
}

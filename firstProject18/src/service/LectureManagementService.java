package service;

import java.text.DecimalFormat;
import java.util.Map;

import controller.Controller;
import dao.LectureManagementDAO;
import dao.LectureSortDAO;
import dao.StudentDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class LectureManagementService {
	LectureManagementDAO dao = LectureManagementDAO.getInstance();
	StudentDAO stdDao = StudentDAO.getInstance();
	LectureSortDAO lecDao = LectureSortDAO.getInstance();

	// 싱글톤 패턴
	private static LectureManagementService instance = null;

	private LectureManagementService() {
	}

	public static LectureManagementService getInstance() {
		if (instance == null)
			instance = new LectureManagementService();
		return instance;
	}

	/** 수강 신청 여부를 확인하는 메서드 */
	public int enrollCheck() {
		while (true) {
			System.out.println("1.강좌 상세조회 0.뒤로가기");
			int choice = ScanUtil.nextInt("입력 >> ");

			if (choice == 0) {
				break;
			}
			if (choice == 1) {
				String lecCode = ScanUtil.nextLine("강좌번호>> ");
				return getLecture(lecCode);
			}
		}

		return View.STUDENT_SEARCH;
	}

	/** 검색된 강의 목록의 상세정보를 출력하는 메서드 */
	public int getLecture(String lecCode) {
		Map<String, Object> result = dao.getLectureInfo(lecCode);
		
		if (result == null) {
			System.out.println("해당하는 강좌가 존재하지않습니다.");
			PrintUtil.delay();
			
			return View.STUDENT_SEARCH;
		}
		
		// 숫자로 표현할 때: big integer 타입으로 인식하기 때문에 문자 > 숫자로 형변환
		int sortName = Integer.parseInt(String.valueOf(result.get("SORT_CODE")));
		Map<String, Object> res = lecDao.sortNameReturn(sortName);

		// 강좌의 수강료를 세자리 단위마다 ,로 구분하여 출력
		DecimalFormat decFormat = new DecimalFormat("###,###");
		String fee = decFormat.format(result.get("LEC_FEE"));
		
		PrintUtil.getLecDetail(lecCode, result.get("LEC_NAME"),
				res.get("SORT_NAME"), fee, result.get("STARTDATE"), result.get("ENDDATE"),
				result.get("ACD_NAME"), result.get("ACD_LOCATION"), result.get("ACD_TELNUM"));

		choiceLecture((String) result.get("LEC_CODE"));

		return View.STUDENT_SEARCH;
	}

	/** 수강 신청 메서드 */
	public int choiceLecture(String lecCode) {
		String id = (String) Controller.sessionStorage.get("STD_ID");
		System.out.println("수강신청 하시겠습니까?(y/n) ");
		String yesNo = ScanUtil.nextLine("입력>> ");

		Map<String, Object> eliRes = stdDao.getStdInfoOne(id);
		int sptCode = Integer.parseInt(String.valueOf(eliRes.get("SPT_CODE")));

		Map<String, Object> dupRes = dao.getLecCodeOne(id);

		if (yesNo.equalsIgnoreCase("y")) {
			if (dupRes != null) {
				PrintUtil.printOne("강좌는 두 개 이상 수강할 수 없습니다.");
				PrintUtil.delay();
				
				return View.STUDENT_SEARCH;
			}

			if (sptCode == 1 || sptCode == 2) {
				int result = dao.stdEnrolment(id, lecCode);
				if (result > 0) {
					PrintUtil.printOne("수강 신청이 완료되었습니다.");
				} else {
					PrintUtil.printOne("수강 신청이 실패하였습니다.");
				}
			} else {
				PrintUtil.printTwo("내일배움카드가 없습니다.", "내일배움카드를 발급해주세요.");
			}
			
			PrintUtil.delay();
		}
		
		return View.STUDENT_SEARCH;
	}
}

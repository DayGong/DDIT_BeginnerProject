package service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.LectureSortDAO;
import util.PrintUtil;
import util.ScanUtil;
import util.View;

public class LectureSortService {
	LectureManagementService lecMService = LectureManagementService.getInstance();
	LectureSortDAO dao = LectureSortDAO.getInstance();

	// 강좌의 수강료를 세자리 단위마다 ,로 구분하여 출력
	DecimalFormat decFormat = new DecimalFormat("###,###");

	// 싱글톤 패턴
	private static LectureSortService instance = null;

	private LectureSortService() {
	}

	public static LectureSortService getInstance() {
		if (instance == null)
			instance = new LectureSortService();
		return instance;
	}
	
	/** 해당 학원이 등록한 강좌 검색 */
	public int academyLectureSearch() {
		String id = (String) Controller.sessionStorage.get("ACD_ID");
		List<Map<String, Object>> lectureSearch = dao.lectureSearch(id);

		if(lectureSearch == null) {
			PrintUtil.printOne("강좌가 존재하지 않습니다.");
			PrintUtil.delay();
			
			return View.ACADEMY_AFTERLOGIN;
		}
		
		PrintUtil.acdLecListHead();
		
		for (int i = 0; i < lectureSearch.size(); i++) {
			Map<String, Object> res = lectureSearch.get(i);
			
			int sortNum = Integer.parseInt(String.valueOf(res.get("SORT_CODE")));
			int lecFee = Integer.parseInt(String.valueOf(res.get("LEC_FEE")));
			
			Map<String, Object> lectureSort = dao.sortNameReturn(sortNum);
			
			PrintUtil.acdLecListData(res.get("LEC_CODE"), lectureSort.get("SORT_NAME")
					, res.get("LEC_NAME"), decFormat.format(lecFee), res.get("STARTDATE")
					, res.get("ENDDATE"));
		}
		PrintUtil.listLine(lectureSearch.size(), 9);

		return View.ACADEMY_LECTURE_CHECK;
	}
	
	/** 지역별 검색 */
	public int location() {
		List<Map<String, Object>> result = null;
		
		PrintUtil.location("지역별 검색");

		String locName = locLocChoice(ScanUtil.nextInt("번호 입력>> "));
		result = dao.locationSearch(locName);
		
		if (result == null) {
			System.out.println("해당하는 강좌가 존재하지않습니다.");
			PrintUtil.delay();
			
			return location();
		}

		PrintUtil.lecLoc(locName, "loc");
		PrintUtil.lecSearchListHead();

		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> res = result.get(i);

			String fee = decFormat.format(res.get("LEC_FEE"));

			PrintUtil.lecSearchListData(res.get("LEC_CODE"), res.get("LEC_NAME"), fee,
					res.get("STARTDATE"), res.get("ENDDATE"), res.get("ACD_NAME"));
		}
		PrintUtil.listLine(result.size(), 9);

		lecMService.enrollCheck();
		return View.STUDENT_SEARCH;
	}

	/** 분류별 검색 */
	public int sort() {
		List<Map<String, Object>> result = null;
		
		PrintUtil.sort("분류별 검색");

		String sort = lecSortChoice(ScanUtil.nextInt("번호 입력 >> "));

		// sort를 1.IT 형식으로 받아와 .을 기준으로 나눔
		String sortAry[] = sort.split(". ");
		int sortNum = Integer.parseInt(sortAry[0]);
		String sortName = sortAry[1];

		result = dao.sortSearch(sortNum);

		if (result == null) {
			System.out.println("해당하는 강좌가 존재하지않습니다.");
			PrintUtil.delay();
			
			return sort();
		}

		PrintUtil.lecLoc(sortName, "sort");
		PrintUtil.lecSearchListHead();

		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> res = result.get(i);
			
			String fee = decFormat.format(res.get("LEC_FEE"));

			PrintUtil.lecSearchListData(res.get("LEC_CODE"), res.get("LEC_NAME"), fee,
					res.get("STARTDATE"), res.get("ENDDATE"), res.get("ACD_NAME"));
			
		}
		PrintUtil.listLine(result.size(), 9);

		lecMService.enrollCheck();
		return View.STUDENT_SEARCH;
	}

	/** 학원 강좌 지역 선택 */
	public String locLocChoice(int lecNum) {
		while (true) {
			switch (lecNum) {
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

			lecNum = ScanUtil.nextInt("번호 입력>> ");
		}
	}

	/** 학원 강좌 분류 번호 선택 */
	public String lecSortChoice(int lecNum) {
		while (true) {
			switch (lecNum) {
			case 1:
				return "1. IT";
			case 2:
				return "2. 경영";
			case 3:
				return "3. 금융";
			case 4:
				return "4. 보건";
			case 5:
				return "5. 문화";
			default:
				System.out.println("1~5까지의 숫자를 입력해주세요.");
			}

			lecNum = ScanUtil.nextInt("번호 입력>> ");
		}
	}
}

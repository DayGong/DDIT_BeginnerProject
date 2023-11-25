package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class LectureSortDAO {
	private static LectureSortDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private LectureSortDAO() {
	}

	public static LectureSortDAO getInstance() {
		if (instance == null)
			instance = new LectureSortDAO();
		return instance;
	}
	
	/**  분류 코드에 따른 분류명을 리턴하는 용도로도 사용 */
	public Map<String, Object> sortNameReturn(int sortNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT SORT_NAME FROM SORT S, LECTURE L ");
		sb.append(" WHERE S.SORT_CODE = L.SORT_CODE");
		sb.append(" AND S.SORT_CODE = ");
		sb.append(sortNum);
		String sql = sb.toString();
		
		return jdbc.selectOne(sql);
	}
	
	/** 학원이 등록한 강좌 목록을 출력할 때 사용 */
	public List<Map<String, Object>> lectureSearch(String id) {
		String sql = "SELECT * FROM LECTURE WHERE ACD_ID = '" + id + "'";
		return jdbc.selectList(sql);
	}
	
	/** 지역별 검색 */
	public List<Map<String, Object>> locationSearch(String loc) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * FROM ACADEMY A, LECTURE L ");
		sb.append(" WHERE A.ACD_ID = L.ACD_ID AND A.ACD_LOCATION = '");
		sb.append(loc);
		sb.append("' ");
		String sql = sb.toString();

		return jdbc.selectList(sql);
	}
	
	/** 분류별 검색 */
	public List<Map<String, Object>> sortSearch(int sortNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * FROM SORT S, LECTURE L, ACADEMY A ");
		sb.append(" WHERE S.SORT_CODE = L.SORT_CODE AND L.ACD_ID = A.ACD_ID");
		sb.append(" AND S.SORT_CODE = ");
		sb.append(sortNum);
		String sql = sb.toString();
		
		return jdbc.selectList(sql);
	}
}

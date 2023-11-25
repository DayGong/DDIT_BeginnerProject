package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class LectureDAO {
	private static LectureDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private LectureDAO() {
	}

	public static LectureDAO getInstance() {
		if (instance == null)
			instance = new LectureDAO();
		return instance;
	}

	/** 전체 강좌 목록 출력할 때 사용 */
	public List<Map<String, Object>> lectureList() {
		String sql = "SELECT * FROM LECTURE";
		return jdbc.selectList(sql);
	}
	
	/** 강좌코드에 해당하는 강좌의 정보 불러오기 */
	public Map<String, Object> getLecCode(String lecCode) {
		String sql = " SELECT * FROM LECTURE WHERE LEC_CODE = '" + lecCode + "'";

		return jdbc.selectOne(sql);
	}

	/** 강좌 코드 중 마지막 세자리 최대값 리턴 */
	public Map<String, Object> lecCodeReturn() {
		String sql = " SELECT DISTINCT LPAD((SELECT MAX(LEC_CODE)+1 FROM LECTURE), 3, '0') AS MAXVALUE FROM LECTURE ";
		return jdbc.selectOne(sql);
	}
	
	/** 강의 비용 조회용 View 테이블(LEC_FEE) 생성 */
	public Map<String, Object> getLecFee(String id) {
		String sql = " SELECT * FROM STD_LEC_LM ";
		sql += " WHERE STD_ID = '" + id + "' ";
		
		return jdbc.selectOne(sql);
	}

	/** 강의 정보 수정 UPDATE문 */
	public int update(String setString, List<Object> param) {
		String sql = "UPDATE LECTURE SET ";
		sql = sql + setString;
		sql = sql + " WHERE LEC_CODE = ? ";
		return jdbc.update(sql, param);
	}

	/** 강의 삭제 DELETE문 */
	public int delete(List<Object> param) {
		String sql = "DELETE FROM LECTURE ";
		sql = sql + " WHERE LEC_CODE = ? ";
		return jdbc.update(sql, param);
	}

	/** 강좌 등록 */
	public int enrollLecture(List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO LECTURE (LEC_CODE, SORT_CODE, LEC_NAME, LEC_FEE, STARTDATE, ENDDATE, ACD_ID) ");
		sb.append(" VALUES (?, ?, ?, ?, ?, ?, ?) ");
		String sql = sb.toString();

		return jdbc.update(sql, param);
	}
}

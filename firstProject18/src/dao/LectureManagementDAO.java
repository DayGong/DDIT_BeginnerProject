package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class LectureManagementDAO {
	private static LectureManagementDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private LectureManagementDAO() {
	}

	public static LectureManagementDAO getInstance() {
		if (instance == null)
			instance = new LectureManagementDAO();
		return instance;
	}

	/** 강좌 상세 조회 */
	public Map<String, Object> getLectureInfo(String lecCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * FROM LECTURE L, ACADEMY A ");
		sb.append(" WHERE L.ACD_ID = A.ACD_ID AND LEC_CODE = '");
		sb.append(lecCode);
		sb.append("'");
		String sql = sb.toString();

		return jdbc.selectOne(sql);
	}

	/** 학생의 ID 값에 해당하는 강좌 코드(LEC_CODE)와 STD_ID 조회 */
	public Map<String, Object> getLecCodeOne(String id) {
		String sql = " SELECT * FROM LECTURE_MANAGEMENT ";
		sql += " WHERE STD_ID = '" + id + "' ";
		
		return jdbc.selectOne(sql);
	}
	
	/** 학생의 ID 값에 해당하는 강좌 코드(LEC_CODE)와 STD_ID 목록 조회 */
	public List<Map<String, Object>> getLecCodeList(String id) {
		String sql = " SELECT * FROM LECTURE_MANAGEMENT WHERE STD_ID = '" + id + "' ";
		
		return jdbc.selectList(sql);
	}

	/** 수강신청 INSERT문 JDBCUtil에 INSERT SQL문과 학생 id, 강좌 코드 전달 */
	public int stdEnrolment(String id, String lecCode) {
		String sql = " INSERT INTO LECTURE_MANAGEMENT(STD_ID, LEC_CODE) ";
		sql += " VALUES ('" + id + "', '" + lecCode + "') ";

		return jdbc.update(sql);
	}

}

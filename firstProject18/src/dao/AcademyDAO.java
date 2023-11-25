package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AcademyDAO {
	private static AcademyDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private AcademyDAO() {
	}

	public static AcademyDAO getInstance() {
		if (instance == null)
			instance = new AcademyDAO();
		return instance;
	}

	/** JDBCUtil에 SELECT SQL문과 학원 id, 학원 pass 전달 */
	public Map<String, Object> login(String id, String pass) {
		return jdbc.selectOne("SELECT * FROM ACADEMY " + " WHERE ACD_ID='" + id + "' AND ACD_PASS='" + pass + "' ");
	}

	/** JDBCUtil에 SELECT SQL문과 학원 id 전달 */
	public Map<String, Object> getAcademyInfo(String acd_id) {
		String sql = " SELECT * FROM ACADEMY WHERE ACD_ID = '" + acd_id + "' ";
		return jdbc.selectOne(sql);
	}

	/** 학원 회원가입 INSERT문 */
	public int signUp(List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO ACADEMY (ACD_ID, ACD_PASS, ACD_NAME, ACD_TELNUM, ACD_LOCATION) ");
		sb.append(" VALUES (?, ?, ?, ?, ? ) ");

		String sql = sb.toString();
		return jdbc.update(sql, param);
	}

	/** 학원 정보 수정 UPDATE문 */
	public int update(String setString, List<Object> param) {
		String sql = "UPDATE ACADEMY SET ";
		sql = sql + setString;
		sql = sql + " WHERE ACD_ID = ?";
		
		return jdbc.update(sql, param);
	}

	/** 학원 정보 삭제  */
	public int delete(List<Object> param) {
		String sql = "DELETE FROM ACADEMY WHERE ACD_ID = ?";
		
		return jdbc.update(sql, param);
	}
}

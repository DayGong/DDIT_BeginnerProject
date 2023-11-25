package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class StudentDAO {
	private static StudentDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private StudentDAO() {
	}

	public static StudentDAO getInstance() {
		if (instance == null)
			instance = new StudentDAO();
		return instance;
	}

	/** JDBCUtil에 SELECT SQL문과 id, pass 전달 */
	public Map<String, Object> login(String id, String pass) {
		String sql = " SELECT * FROM STUDENT WHERE STD_ID='" + id + "' AND STD_PASS='" + pass + "' ";
		return jdbc.selectOne(sql);
	}

	/** JDBCUtil에 SELECT SQL문과 id 전달 */
	public Map<String, Object> getStdInfoOne(String id) {
		String sql = " SELECT * FROM STUDENT WHERE STD_ID = '" + id + "' ";
		return jdbc.selectOne(sql);
	}
	
	/** 학생의 전체 정보 가져올 때 사용 */
	public List<Map<String, Object>> getStdInfoList(String id) {
		String sql = " SELECT * FROM STUDENT WHERE STD_ID = '" + id + "' ";
		return jdbc.selectList(sql);
	}

	/** 학생 회원가입 INSERT문 */
	public int signUp(List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO STUDENT (STD_ID, STD_NAME, STD_PASS, STD_PRECOUNT, SPT_CODE) ");
		sb.append(" VALUES (?, ?, ?, ?, ? ) ");
		String sql = sb.toString();
		
		return jdbc.update(sql, param);
	}

	/** 학생 정보 수정 UPDATE문 */
	public int update(String setString, List<Object> param) {
		String sql = "UPDATE STUDENT SET ";
		sql = sql + setString;
		sql = sql + " WHERE STD_ID = ?";

		return jdbc.update(sql, param);
	}

	/** 학생 정보 삭제 */
	public int delete(List<Object> param) {
		String sql = "DELETE FROM STUDENT WHERE STD_ID = ?";
		
		return jdbc.update(sql, param);
	}

}

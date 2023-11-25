package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AttendanceDAO {
	private static AttendanceDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private AttendanceDAO() {
	}

	public static AttendanceDAO getInstance() {
		if (instance == null)
			instance = new AttendanceDAO();
		return instance;
	}

	/** 학생이 수강하는 과목의 학원 ID를 가져오는 메서드 */
	public Map<String, Object> getAcademy(String stdId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT L.ACD_ID FROM LECTURE L, LECTURE_MANAGEMENT LM ");
		sb.append(" WHERE L.LEC_CODE = LM.LEC_CODE AND STD_ID = '");
		sb.append(stdId);
		sb.append("' ");
		String sql = sb.toString();

		return jdbc.selectOne(sql);
	}

	/** 출석 현황 코드의 최대값을 받아오는 메서드 */
	public Map<String, Object> getMaxValue() {
		String sql = " SELECT DISTINCT LPAD((SELECT MAX(SUBSTR(ATD_CODE, 9, 11))+1 FROM ATTENDANCE), 3, '0') ";
		sql += " AS MAXVALUE FROM ATTENDANCE ";
		
		return jdbc.selectOne(sql);
	}

	/** 입실할 때 사용 */
	public int inTime(List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO ATTENDANCE (ATD_CODE, INTIME, OUTTIME, ATD_STATUS, STD_ID, ACD_ID) ");
		sb.append(" VALUES (?, ?, '00:00:00', '결석', ?, ?) ");
		String sql = sb.toString();
		
		return jdbc.update(sql, param);
	}

	/** 퇴실할 때 사용 */
	public int outTime(List<Object> param) {
		String sql = " UPDATE ATTENDANCE SET OUTTIME = ? ";
		sql += " WHERE ATD_CODE = ? ";
		
		return jdbc.update(sql, param);
	}

	/** 당일 출석 상태를 불러오는 메서드 */
	public Map<String, Object> getInOutStatus(List<Object> param) {
		String sql = " SELECT INTIME, OUTTIME FROM ATTENDANCE ";
		sql += " WHERE STD_ID = ? AND SUBSTR(ATD_CODE, 0, 8) = ? ";

		return jdbc.selectOne(sql, param);
	}

	/** 당일 출석 상태를 변경하는 메서드 */
	public int statusChange(List<Object> param) {
		String sql = " UPDATE ATTENDANCE SET ATD_STATUS = ? ";
		sql += " WHERE ATD_CODE = ? ";
		
		return jdbc.update(sql, param);
	}
	
	/** 출결 현황 검색할 때 사용 */
	public List<Map<String, Object>> getAtdStatus(String stdId) {
		String sql = " SELECT * FROM ATTENDANCE WHERE STD_ID = '";
		sql += stdId;
		sql += "' ";

		return jdbc.selectList(sql);
	}
	
}

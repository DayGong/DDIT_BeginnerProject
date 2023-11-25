package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class SupportSystemDAO {
	private static SupportSystemDAO instance = null;

	JDBCUtil jdbc = JDBCUtil.getInstance();

	private SupportSystemDAO() {
	}

	public static SupportSystemDAO getInstance() {
		if (instance == null)
			instance = new SupportSystemDAO();
		return instance;
	}

	/** 수당 검색할 때 사용 */
	public Map<String, Object> getStdLecInfo(String id) {
		String sql = " SELECT * FROM STUDENT S LEFT JOIN LECTURE_MANAGEMENT LM ";
		sql += " ON S.STD_ID = LM.STD_ID WHERE S.STD_ID = '" + id + "' ";
		
		return jdbc.selectOne(sql);
	}
	
	public Map<String, Object> getAtdStatus(String stdId) {
		String sql = " SELECT COUNT(*) FROM ATTENDANCE WHERE STD_ID = '";
		sql += stdId;
		sql += "' ";

		return jdbc.selectOne(sql);
	}

	/** 지원제도명 불러오기 */
	public List<Map<String, Object>> getSptName() {
		String sql = " SELECT SPT_NAME FROM SUPPORT_SYSTEM ";
		return jdbc.selectList(sql);
	}
}

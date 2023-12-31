package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	// 인스턴스를 보관할 변수
	private static JDBCUtil instance = null;

	// JDBCUtil 객체를 만들 수 없게(인스턴스화 할 수 없게) private으로 제한함
	private JDBCUtil() {
	}

	public static JDBCUtil getInstance() {
		if (instance == null)
			instance = new JDBCUtil();
		return instance;
	}

	private String url = "jdbc:oracle:thin:@192.168.146.55:1521:xe";
	private String user = "firstproject18";
	private String password = "java";

	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;

	public List<Map<String, Object>> selectList(String sql, List<Object> param) {
		List<Map<String, Object>> result = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			while (rs.next()) {
				if (result == null)
					result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public List<Map<String, Object>> selectList(String sql) {
		List<Map<String, Object>> result = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			while (rs.next()) {
				if (result == null)
					result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public int update(String sql, List<Object> param) {
		int result = 0;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}

			result = ps.executeUpdate();
			conn.commit();

		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			se.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public int update(String sql) {
		int result = 0;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public Map<String, Object> selectOne(String sql, List<Object> param) {
		Map<String, Object> row = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < param.size(); i++) {
				// setObject(물음표의 순번, 데이터)
				ps.setObject(i + 1, param.get(i)); // param은 list
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i); // 컬럼명
					Object value = rs.getObject(i); // 데이터값
					row.put(key, value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}

		return row;
	}

	public Map<String, Object> selectOne(String sql) {
		Map<String, Object> row = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			while (rs.next()) {
				row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					// getColumnLabel : as로 선언된 별명을 가져옴, 없으면 원본 컬럼명
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return row;
	}
}
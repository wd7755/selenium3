package com.agileach.selenium3;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * 
 * 数据库操作类
 * 
 * @author Alex Wang
 *
 */
public class OperateDB {
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	private static CallableStatement callableStatement = null;
	private static ConnectionPool jdbcObj = null;
	private static DataSource dataSource = null;

	// 在静态代码块中创建数据库连接池
	static {
		try {
			jdbcObj = new ConnectionPool();
			dataSource = jdbcObj.setUpPool();			
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getJDBCConnection() {
       try {    	
           return dataSource.getConnection();
       } catch (SQLException e) {
    	   System.out.println("获取数据库连接出错：" + e.getMessage());
           e.printStackTrace();
       }
       return null;
   }

	/**
	 * insert update delete SQL语句的执行的统一方法
	 * 
	 * @param sql    SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return int 受影响的行数
	 */
	public static int executeUpdate(String sql, Object[] params) {
		// 受影响的行数
		int affectedLine = 0;
		try {
			// 获得连接
			conn = getJDBCConnection();			
			// 调用SQL
			ps = conn.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			// 执行
			affectedLine = ps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			closeAll();
		}
		return affectedLine;
	}

	/**
	 * 执行select语句获取一个标量值
	 * 
	 * @param sql select语句
	 * @return String 返回select语句执行后的第一行第一列
	 * @throws SQLException 可能抛出SQL异常
	 */
	public static String getScalarValue(String sql) throws SQLException {
		return getScalarValue(sql, null);
	}

	/**
	 * 执行select语句获取一个标量值
	 * 
	 * @param sql    select语句
	 * @param values select语句中的变量的值的String类型的数组
	 * @return String 返回select语句执行后的第一行第一列
	 * @throws SQLException 可能抛出SQL异常
	 */
	public static String getScalarValue(String sql, String[] values) throws SQLException {
		String value = "";
		ResultSet rs = getResultSet(sql, values);
		if (rs.next()) {
			value = rs.getString(1);
		}
		//closeAll();
		return value;
	}

	/**
	 * 执行select语句获取一个结果集
	 * 
	 * @param sql select语句
	 * @return ResultSet 包含查询结果的结果集
	 * @throws SQLException 可能抛出SQL异常
	 */
	public static ResultSet getResultSet(String sql) throws SQLException {
		return getResultSet(sql, null);
	}

	/**
	 * 执行select语句获取一个结果集
	 * 
	 * @param sql    select语句
	 * @param values select语句中的变量的值的Object类型的数组
	 * @return ResultSet 包含查询结果的结果集
	 * @throws SQLException 可能抛出SQL异常
	 */
	public static ResultSet getResultSet(String sql, Object[] values) throws SQLException {
		try {
			conn = getJDBCConnection();
			ps = conn.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 获取结果集，并将结果放在List中
	 * 
	 * @param sql select语句
	 * @return List<Object> 结果集
	 * @throws SQLException 可能抛出SQL异常
	 */
	public static List<Object> excuteQuery(String sql, Object[] params) throws SQLException {
		// 执行SQL获得结果集
		ResultSet rs = getResultSet(sql, params);
		// 创建ResultSetMetaData对象
		ResultSetMetaData rsmd = null;
		// 结果集列数
		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();
			// 获得结果集列数
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// 创建List
		List<Object> list = new ArrayList<Object>();
		try {
			// 将ResultSet的结果保存到List中
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭所有资源
			//closeAll();
		}
		return list;
	}

	/**
	 * 执行存储过程带有一个输出参数的方法
	 * 
	 * @param sql         存储过程语句
	 * @param params      参数数组
	 * @param outParamPos 输出参数位置
	 * @param SqlType     输出参数类型
	 * @return Object 输出参数的值
	 */
	public static Object excuteQuery(String sql, Object[] params, int outParamPos, int SqlType) throws SQLException {
		Object object = null;
		conn = getJDBCConnection();
		try {
			// 调用存储过程
			callableStatement = conn.prepareCall(sql);
			// 给参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					callableStatement.setObject(i + 1, params[i]);
				}
			}
			// 注册输出参数
			callableStatement.registerOutParameter(outParamPos, SqlType);
			// 执行
			callableStatement.execute();
			// 得到输出参数
			object = callableStatement.getObject(outParamPos);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 释放资源
			//closeAll();
		}
		return object;
	}

	/**
	 * 关闭ResultSet，PreparedStatement，callableStatement，Connection对象
	 */
	public static void closeAll() {			
		// 关闭结果集对象
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭PreparedStatement对象
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭CallableStatement 对象
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭Connection 对象
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}

	public static void main(String[] args) {
		try {		
			List<Object> list = OperateDB.excuteQuery("select * from records where id=?", new String[] { "6002303" });
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) list.get(0);

			int id = Integer.parseInt(String.valueOf(hashMap.get("id")));
			Date clockTime = (java.util.Date) hashMap.get("clocktime");
			int deviceId = Integer.parseInt(String.valueOf(hashMap.get("deviceid")));

			System.out.println(id);
			System.out.println(clockTime);
			System.out.println(deviceId);		
			OperateDB.closeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
		 * // 模糊查询写法 try { List<Object> list = OperateDB.excuteQuery(
		 * "select name,id from member where name like ?", new String[] { "david%" });
		 * for (Object o : list) { HashMap<String, Object> hashMap = (HashMap<String,
		 * Object>) o;
		 * 
		 * for (String key : hashMap.keySet()) { System.out.println("键是：" + key); } for
		 * (Object value : hashMap.values()) { System.out.println("值是：" + value); }
		 * System.out.println("直接输出键值对:"); Set<Map.Entry<String, Object>> set =
		 * hashMap.entrySet(); Iterator<Map.Entry<String, Object>> iterator2 =
		 * set.iterator(); while (iterator2.hasNext()) { Map.Entry<String, Object> entry
		 * = iterator2.next(); System.out.println("键是：" + entry.getKey() + ";值是：" +
		 * entry.getValue()); }
		 * System.out.println("------------------------------------"); } } catch
		 * (SQLException e) { e.printStackTrace(); }
		 */
	}
}

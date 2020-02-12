package com.agileach.selenium3;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class ConnectionPool {

	// JDBC Driver Name & Database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String JDBC_DB_URL = "jdbc:mysql://127.0.0.1:3306/school?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";

	// JDBC Database Credentials
	static final String JDBC_USER = "root";
	static final String JDBC_PASS = "admin";
	private static GenericObjectPool gPool = null;

	public DataSource setUpPool() throws Exception {
		Class.forName(JDBC_DRIVER);

		// Creates an Instance of GenericObjectPool That Holds Our Pool of Connections
		// Object!
		gPool = new GenericObjectPool();
		gPool.setMaxActive(-1);

		// Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create
		// the Connection Object!
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

		// Creates a PoolableConnectionFactory That Will Wraps the Connection Object
		// Created by the ConnectionFactory to Add Object Pooling Functionality!
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		return new PoolingDataSource(gPool);
	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	// This Method Is Used To Print The Connection Pool Status
	private void printDbStatus() {
		System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: "
				+ getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	}

	public static void main(String[] args) {
		ResultSet rsObj = null;
		ResultSet rsObj2 = null;
		Connection connObj = null;
		Connection connObj2 = null;
		PreparedStatement pstmtObj = null;
		PreparedStatement pstmtObj2 = null;
		ConnectionPool jdbcObj = new ConnectionPool();
		try {
			DataSource dataSource = jdbcObj.setUpPool();
			jdbcObj.printDbStatus();

			// Performing Database Operation!
			System.out.println("\n=====Making A New Connection Object For Db Transaction=====\n");
			connObj = dataSource.getConnection();
			connObj2 = dataSource.getConnection();
			jdbcObj.printDbStatus();

			pstmtObj = connObj.prepareStatement("SELECT * FROM records");
			pstmtObj2 = connObj2.prepareStatement("SELECT * FROM records");
			rsObj = pstmtObj.executeQuery();
			rsObj2 = pstmtObj2.executeQuery();		
			jdbcObj.printDbStatus();
			while (rsObj.next()) {
				System.out.println("id: " + rsObj.getInt("id"));			
			}		
			System.out.println("\n=====Releasing Connection Object To Pool=====\n");
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				// Closing ResultSet Object
				if (rsObj != null) {
					 rsObj.close();
				}
				// Closing PreparedStatement Object
				if (pstmtObj != null) {
					 pstmtObj.close();
				}
				// Closing Connection Object
				if (connObj != null) {
					 connObj.close();
				}
				if (connObj2 != null) {
					 connObj2.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		jdbcObj.printDbStatus();
	}
}
package com.aoin.bookstore.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceUtils {
	private static DataSource dataSource = new ComboPooledDataSource("bookstore");
	private static ThreadLocal<Connection> t1 = new ThreadLocal<Connection>();
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	/**
	 * DButils需要手动控制事务时，调用该方法get一个connection
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = t1.get();
		if(conn == null) {
			conn = dataSource.getConnection();
			t1.set(conn);
		}
		return conn;
	}
	/**
	 * 开启事务
	 * @throws SQLException 
	 */
	public static void startTransaction() throws SQLException {
		Connection conn = getConnection();
		if(conn != null) {
			conn.setAutoCommit(false);
		}
	}
	/**
	 * 从ThreadLocal 中释放并且关闭Connection,结束事务
	 * @throws SQLException 
	 */
	public static void releaseAndCloseConnection() throws SQLException {
		Connection conn = getConnection();
		if(conn != null) {
			conn.commit();
			t1.remove();
			conn.close();
		}
	}
	/**
	 * 事务回滚
	 * @throws SQLException 
	 */
	public static void rollback() throws SQLException {
		Connection conn = getConnection();
		if(conn != null) {
			conn.rollback();
		}
	}
}

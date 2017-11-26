package com.yfwang.panorama.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.yfwang.panorama.shared.CommonUtil;


public class DBHelper
{
	//private static final String url = "jdbc:mysql://119.23.150.155/album?useUnicode=true&characterEncoding=UTF-8";
	private static final String url = "jdbc:mysql://127.0.0.1/pana?useUnicode=true&characterEncoding=UTF-8";
	private static final String name = "com.mysql.jdbc.Driver";
	private static final String user = "root";
	private static final String password = "wyf972355";
	private static final String local_password = "wyf972355";

	private Connection conn = null;

	public DBHelper()
	{
		try
		{
			
			Class.forName(name);// 指定连接类型

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException
	{

		conn = DriverManager.getConnection(url, user, CommonUtil.isDebug ? local_password : password);// 获取连接
		return conn;
	}

	public void close()
	{
		try
		{
			this.conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

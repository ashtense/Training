package com.ashwani.training.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "oracle");
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery("select *from HELP");
		while (resultSet.next()) {
			System.err.println(resultSet.getString("info"));
		}
	}

}
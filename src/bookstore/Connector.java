package bookstore;

import java.sql.*;

public class Connector {
	public Connection con;
	public Statement stmt;
	public Connector() throws Exception {
		System.out.println("Connecting the specified database...");
		try{
			String userName = "acmdbu01";
			String password = "sgvt3uf1";
			String url = "jdbc:mysql://georgia.eng.utah.edu/acmdb01";
//			String url = "jdbc:mysql://localhost";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			con = DriverManager.getConnection (url, userName, password);
			stmt = con.createStatement();
			System.out.println("Database connected...");
		} catch(Exception e) {
			System.err.println("Unable to open mysql jdbc connection. The error is as follows,\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}

	public void closeConnection() throws Exception{
		con.close();
	}
}

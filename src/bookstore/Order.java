package bookstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class Order {
	private Connection con;
	private BufferedReader buffer;

	public Order(Connection conn, BufferedReader buffers) throws Exception {
		con = conn;
		buffer = buffers;
	}

	public int register()
		throws Exception {
		System.out.println("cmd is register");
		System.out.print("Enter your username :");
		String username = next();
		System.out.print("Enter your password :");
		String password = next();
		System.out.println("Registering user \"" + username + "\" with password \"" + password + "\"" );
		int userPower;
		if (username.equals("root")) userPower = 2; else userPower = 1;

		PreparedStatement updateStatement = null;

		String updateString =
			"INSERT INTO User VALUES " +
			"(?, ?, ?)";

		try {
			con.setAutoCommit(false);
			updateStatement = con.prepareStatement(updateString);
			updateStatement.setString(1, username);
			updateStatement.setString(2, password);
			updateStatement.setInt(3, userPower);
			updateStatement.executeUpdate();
			con.commit();
		} catch (SQLException e ) {
			System.err.print("Error when registering :" + e.getMessage());
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			con.setAutoCommit(true);
		}
		return userPower;
	}

	public int login()
		throws Exception {
		System.out.println("cmd is login");
		System.out.print("Enter your username :");
		String username = next();
		System.out.print("Enter your password :");
		String password = next();
		System.out.println("user \"" + username + "\" trying to login with password \"" + password + "\"" );
		int userPower = -1;

		PreparedStatement queryStatement = null;

		String queryString =
			"SELECT * FROM User " + 
			"WHERE username=? and password=?";

		try {
			con.setAutoCommit(false);
			queryStatement = con.prepareStatement(queryString);
			queryStatement.setString(1, username);
			queryStatement.setString(2, password);
			ResultSet res = queryStatement.executeQuery();
			while (res.next()) {
				userPower = res.getInt("userpower");
			}
			if (userPower == -1)
				System.out.println("Login unsuccessful");
			else
				System.out.println("Login Successful!");
			res.close();
			con.commit();
		} catch (SQLException e ) {
			System.err.print("Error when registering :" + e.getMessage());
		} finally {
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return userPower;
	}

	private String next() throws Exception {
		String s;
		while ((s = buffer.readLine()) == null || s.length() == 0) ;
		return s;
	}
}



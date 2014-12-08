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

		PreparedStatement insertUser = null;

		String insertString =
			"INSERT INTO User VALUES " +
			"(?, ?, ?)";

		try {
			con.setAutoCommit(false);
			insertUser = con.prepareStatement(insertString);
			insertUser.setString(1, username);
			insertUser.setString(2, password);
			insertUser.setInt(3, userPower);
			insertUser.executeUpdate();
			con.commit();
		} catch (SQLException e ) {
			System.err.print("Error when registering :" + e.getMessage());
		} finally {
			if (insertUser != null) 
				insertUser.close();
			con.setAutoCommit(true);
		}
		return userPower;
	}
	public int login()
		throws SQLException {
		return 1;
	}

	private String next() throws Exception {
		String s;
		while ((s = buffer.readLine()) == null || s.length() == 0) ;
		return s;
	}
}



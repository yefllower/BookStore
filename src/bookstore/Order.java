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

		PreparedStatement updateStatement = null;
		int userPower = 0;

		try {
			String username = nextString("Enter your username :", false);
			String password = nextString("Enter your password :", false);
			String name = nextString("Enter your name (optional):", true);
			String address = nextString("Enter your address (optional):", true);
			String phonenum = nextString("Enter your phone number (optional):", true);
			System.out.println("Registering user \"" + username + "\" with password \"" + password + "\"" );
			if (username.equals("root")) userPower = 2; else userPower = 1;


			try {
				String updateString =
					"INSERT INTO User " + 
					"(username, password, userpower, name, address, phonenum) VALUES " +
					"(?, ?, ?, ?, ?, ?)";

				con.setAutoCommit(false);
				updateStatement = con.prepareStatement(updateString);
				updateStatement.setString(1, username);
				updateStatement.setString(2, password);
				updateStatement.setInt(3, userPower);
				updateStatement.setString(4, name);
				updateStatement.setString(5, address);
				updateStatement.setString(6, phonenum);
				updateStatement.executeUpdate();
				System.out.println("Register Successful");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when registering :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
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

		PreparedStatement queryStatement = null;
		int userPower = -1;
		int uid = -1;

		try {
			String username = nextString("Enter your username :", false);
			String password = nextString("Enter your password :", false);
			System.out.println("user \"" + username + "\" trying to login with password \"" + password + "\"" );


			try {
				String queryString =
					"SELECT * FROM User " + 
					"WHERE username=? and password=?";

				con.setAutoCommit(false);
				queryStatement = con.prepareStatement(queryString);
				queryStatement.setString(1, username);
				queryStatement.setString(2, password);
				ResultSet res = queryStatement.executeQuery();
				while (res.next()) {
					userPower = res.getInt("userpower");
					uid = res.getInt("uid");
				}
				if (userPower == -1)
					System.out.println("Login unsuccessful");
				else
					System.out.println("Login Successful!");
				res.close();
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when trying to login :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		if (userPower == -1) return -1;
		return uid * 10 + userPower;
	}

	public int newbook(int userPower)
		throws Exception {
		System.out.println("Cmd is newbook");
		if (userPower != 2) {
			System.out.println("Unauthorized, only manager can add new books");
			return -1;
		}

		PreparedStatement updateStatement = null;
		PreparedStatement queryStatement = null;

		try {
			String isbn = nextString("Enter book's isbn :", false);
			String title = nextString("Enter book's title :", false);
			String publisher = nextString("Enter book's publisher name :", false);
			int year = Integer.parseInt(nextString("Enter book's year :", false));
			int stock = Integer.parseInt(nextString("Enter book's number of copies :", false));
			Float price = Float.parseFloat(nextString("Enter book's price :", false));
			String format = nextString("Enter book's format (hardcover/softcover, softcover by default) :", true);
			if (format.length() == 0) format = "softcover";
			String subject = nextString("Enter book's subject (optional) :", true);

			try {
				String updateString =
					"INSERT INTO Book " + 
					"(isbn, title, publisher, year, stock, price, format, subject) VALUES " +
					"(?, ?, ?, ?, ?, ?, ?, ?)";

				String queryString = 
					"SELECT pid FROM Publisher " + 
					"WHERE name = ?";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setString(1, publisher);
				int pid = -1;
				ResultSet res = queryStatement.executeQuery();
				while (res.next()) 
					pid = res.getInt("pid");
				if (pid == -1) {
					System.out.println("Unknown publisher, added to Publisher");
					updateStatement = con.prepareStatement("INSERT INTO Publisher (name) VALUES (?)");
					updateStatement.setString(1, publisher);
					updateStatement.executeUpdate();
				}	
				res = queryStatement.executeQuery();
				while (res.next()) 
					pid = res.getInt("pid");
				System.out.println("pid is " + pid);

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setString(1, isbn);
				updateStatement.setString(2, title);
				updateStatement.setInt(3, pid);
				updateStatement.setInt(4, year);
				updateStatement.setInt(5, stock);
				updateStatement.setFloat(6, price);
				updateStatement.setString(7, format);
				updateStatement.setString(8, subject);
				updateStatement.executeUpdate();
				System.out.println("New book added");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new book:" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			con.setAutoCommit(true);
		}
		return userPower;
	}

	public int newcopy(int userPower)
		throws Exception {
		System.out.println("Cmd is newcopy");
		if (userPower != 2) {
			System.out.println("Unauthorized, only manager can add new book copies");
			return -1;
		}

		PreparedStatement updateStatement = null;

		try {
			String isbn = nextString("Enter book's isbn :", false);
			int delta = Integer.parseInt(nextString("Enter number of copies :", false));

			try {
				String updateString =
					"UPDATE Book SET stock=stock+? " +
					"WHERE isbn=?";

				con.setAutoCommit(false);

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setString(1, isbn);
				updateStatement.setInt(2, delta);
				updateStatement.executeUpdate();
				System.out.println("New copies added");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new copies :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			con.setAutoCommit(true);
		}
		return userPower;
	}

	public int feedback(int userPower, int uid)
		throws Exception {
		System.out.println("Cmd is feedback");
		if (userPower == 0) {
			System.out.println("Unauthorized, only logined user can give feedbacks");
			return -1;
		}

		PreparedStatement updateStatement = null;
		PreparedStatement queryStatement = null;

		try {
			String isbn = nextString("Enter the book's isbn :", false);
			int score = Integer.parseInt(nextString("Enter the score :", false));
			String comment = nextString("Enter the comment (optional) :", true);
			long timstamp = System.currentTimeMillis();


			try {
				String updateString =
					"INSERT INTO Feedback " + 
					"(bid, uid, score, dates, comment) VALUES " + 
					"(?, ?, ?, ?, ?)";
			
				String queryString = 
					"SELECT bid FROM Book " + 
					"WHERE isbn = ?";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setString(1, isbn);
				int bid = -1;
				ResultSet res = queryStatement.executeQuery();
				while (res.next()) 
					bid = res.getInt("bid");
				if (bid == -1) {
					System.out.println("Unknown book, cannot comment");
					return -1;
				}

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setInt(1, bid);
				updateStatement.setInt(2, uid);
				updateStatement.setInt(3, score);
				updateStatement.setTimestamp(4, new Timestamp(timstamp));
				updateStatement.setString(5, comment);
				updateStatement.executeUpdate();
				System.out.println("New Feedback added");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new copies :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}

	private String next(Boolean acceptEmpty) throws Exception {
		String s;
		while (true) {
			s = buffer.readLine();
			if (s != null) {
				if (acceptEmpty || s.length() != 0)
					break;
			}
		}
		return s;
	}
	private String nextString(String hint, Boolean acceptEmpty) throws Exception {
		System.out.print(hint);
		return next(acceptEmpty);
	}
}



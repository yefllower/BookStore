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

	public double neworder(int userPower, int uid)
		throws Exception {
		System.out.println("Cmd is neworder");
		if (userPower == 0) {
			System.out.println("Unauthorized, only logined user can make ordering");
			return -1.0;
		}

		PreparedStatement updateStatement = null;
		PreparedStatement queryStatement = null;
		double sumPrice = 0;

		try {
			String isbn = nextString("Enter book's isbn :", false);
			String numberString = nextString("Enter number of buying copies (1 by default):", true);
			int number = 1;
			if (numberString.length() > 0) number = Integer.parseInt(numberString);
			long timstamp = System.currentTimeMillis();

			try {
				String updateString =
					"INSERT INTO Ordering " + 
					"(bid, uid, number, dates) VALUES " +
					"(?, ?, ?, ?)";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement("SELECT bid, price, stock FROM Book WHERE isbn = ?");
				queryStatement.setString(1, isbn);
				double price = -1;
				int stock = -1;
				int bid = -1;
				ResultSet res = queryStatement.executeQuery();
				while (res.next()) {
					bid = res.getInt("bid");
					price = res.getFloat("price");
					stock = res.getInt("stock");
				}
				if (price < 0 || stock < number || bid < 0) {
					System.out.println("Unknown book or unsuffient number of stock");
					return -1;
				}

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setInt(1, bid);
				updateStatement.setInt(2, uid);
				updateStatement.setInt(3, number);
				updateStatement.setTimestamp(4, new Timestamp(timstamp));
				updateStatement.executeUpdate();
				sumPrice = price * number;
				System.out.println("New ordering added " + String.format("%.2f", sumPrice));

				updateStatement = con.prepareStatement("UPDATE Book SET stock=stock-? WHERE isbn=?");
				updateStatement.setInt(1, number);
				updateStatement.setString(2, isbn);
				updateStatement.executeUpdate();
				System.out.println("Book stock info updated ");

				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new book:" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return sumPrice;
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
			String authorString = nextString("Enter book's authors(seperated with ','):", false);
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

				String[] authors = authorString.split(",");
				for (int i = 0; i < authors.length; i++) {
					int aid = -1;
					queryStatement = con.prepareStatement("SELECT aid FROM Author WHERE name=?");
					queryStatement.setString(1, authors[i].trim());
					ResultSet tmp = queryStatement.executeQuery();
					while (tmp.next()) {
						aid = tmp.getInt("aid");
					}
					if (aid == -1) {
						System.out.println("Unknown author, added");
						updateStatement = con.prepareStatement("INSERT INTO Author (name) VALUES (?)");
						updateStatement.setString(1, authors[i].trim());
						updateStatement.executeUpdate();
					}
				}

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

				int bid = -1;
				queryStatement = con.prepareStatement("SELECT bid FROM Book WHERE isbn = ?");
				queryStatement.setString(1, isbn);
				res = queryStatement.executeQuery();
				while (res.next())
					bid = res.getInt("bid");

				for (int i = 0; i < authors.length; i++) {
					int aid = -1;
					queryStatement = con.prepareStatement("SELECT aid FROM Author WHERE name=?");
					queryStatement.setString(1, authors[i].trim());
					ResultSet tmp = queryStatement.executeQuery();
					while (tmp.next()) {
						aid = tmp.getInt("aid");
					}
					updateStatement = con.prepareStatement("INSERT INTO Written (aid, bid) VALUES (?, ?)");
					updateStatement.setInt(1, aid);
					updateStatement.setInt(2, bid);
					updateStatement.executeUpdate();
				}

				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new book:" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			if (queryStatement != null) 
				queryStatement.close();
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
			int score = Integer.parseInt(nextString("Enter the score (0 ~ 10):", false));
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
				System.err.print("Error when adding new feedback :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}

	public int rate(int userPower, int uid1)
		throws Exception {
		System.out.println("Cmd is rating");
		if (userPower == 0) {
			System.out.println("Unauthorized, only logined user can rate feedbacks");
			return -1;
		}

		PreparedStatement updateStatement = null;
		PreparedStatement queryStatement = null;

		try {
			String username = nextString("Enter that user's username :", false);
			String isbn = nextString("Enter the book's isbn :", false);
			int score = Integer.parseInt(nextString("Enter the score (0, 1, or 2):", false));

			try {
				String updateString =
					"INSERT INTO Rating " + 
					"(bid, uid0, uid1, score) VALUES " + 
					"(?, ?, ?, ?)";
			
				con.setAutoCommit(false);

				queryStatement = con.prepareStatement("SELECT bid FROM Book WHERE isbn = ?");
				queryStatement.setString(1, isbn);
				int bid = -1;
				ResultSet res = queryStatement.executeQuery();
				while (res.next()) 
					bid = res.getInt("bid");
				if (bid == -1) {
					System.out.println("Unknown book, cannot rate");
					return -1;
				}

				queryStatement = con.prepareStatement("SELECT uid FROM User WHERE username = ?");
				queryStatement.setString(1, username);
				int uid0 = -1;
				res = queryStatement.executeQuery();
				while (res.next()) 
					uid0 = res.getInt("uid");
				if (uid0 == -1) {
					System.out.println("Unknown user, cannot rate");
					return -1;
				}
				if (uid0 == uid1) {
					System.out.println("Cannot rate your own feedback");
					return -1;
				}

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setInt(1, bid);
				updateStatement.setInt(2, uid0);
				updateStatement.setInt(3, uid1);
				updateStatement.setInt(4, score);
				updateStatement.executeUpdate();
				System.out.println("New rating added");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new rating :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}

	public int trust(int userPower, int uid1)
		throws Exception {
		System.out.println("Cmd is trust");
		if (userPower == 0) {
			System.out.println("Unauthorized, only logined user can modify trusting");
			return -1;
		}

		PreparedStatement updateStatement = null;
		PreparedStatement queryStatement = null;

		try {
			String username = nextString("Enter that user's username :", false);
			String trustString = nextString("Enter trusted or untrusted (trusted by default) :", true);
			if (trustString.length() == 0)
				trustString = "trusted";

			try {
				String updateString =
					"INSERT INTO Trust " + 
					"(uid0, uid1, trust) VALUES " + 
					"(?, ?, ?)";
			
				con.setAutoCommit(false);

				queryStatement = con.prepareStatement("SELECT uid FROM User WHERE username = ?");
				queryStatement.setString(1, username);
				int uid0 = -1;
			 	ResultSet res = queryStatement.executeQuery();
				while (res.next()) 
					uid0 = res.getInt("uid");
				if (uid0 == -1) {
					System.out.println("Unknown user, cannot trust");
					return -1;
				}

				updateStatement = con.prepareStatement(updateString);
				updateStatement.setInt(1, uid0);
				updateStatement.setInt(2, uid1);
				updateStatement.setBoolean(3, trustString.equals("trusted"));
				updateStatement.executeUpdate();
				System.out.println("New trusting added");
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when adding new trusting :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (updateStatement != null) 
				updateStatement.close();
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}

	public int useful(int userPower, int uid1)
		throws Exception {
		System.out.println("Cmd is useful");

		PreparedStatement queryStatement = null;

		try {
			String isbn = nextString("Enter the book's isbn :", false);
			int number = Integer.parseInt(nextString("Enter the number of showing items:", false));

			try {
				String queryString =
					"SELECT * FROM ( " +
					"    SELECT AVG(r.score) as rating, u.username, f.score, f.comment " +
					"    FROM Rating r, Feedback f, Book b, User u " + 
					"    WHERE r.bid=f.bid and r.uid0=f.uid and f.bid=b.bid and b.isbn=? and u.uid=f.uid " + 
					"    GROUP BY r.uid0) AS fb " + 
					"ORDER BY fb.rating DESC LIMIT ? ";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setString(1, isbn);
				queryStatement.setInt(2, number);
				ResultSet res = queryStatement.executeQuery();
				System.out.println("Query of trending feedbacks returned");
				System.out.println(String.format(" %7s %10s %7s %s", "rating", "username", "score", "comment"));
				while (res.next()) {
					Float rating = res.getFloat("rating"); 
					String username = res.getString("username"); 
					int score = res.getInt("score"); 
					String comment = res.getString("comment");
					System.out.println(String.format(" %7.3f %10s %7d %s", rating, username, score, comment));
				}
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when querying trending feedbacks :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}
	
	public int suggest(int userPower, int uid)
		throws Exception {
		System.out.println("Cmd is suggest");
		
		if (userPower == 0) {
			System.out.println("Unauthorized, only logined user can get suggestion");
			return -1;
		}

		PreparedStatement queryStatement = null;

		try {
			String isbn = nextString("Enter the book's isbn :", false);

			try {
				String queryString =
					"SELECT SUM(o2.number) as commonSales, Book.isbn "+
					"FROM Book, Ordering o1, Ordering o2 "+
					"WHERE o1.uid=o2.uid and o1.bid=Book.bid and Book.isbn=? and o2.uid != ? "+
					"GROUP BY o2.bid "+
					"ORDER BY commonSales DESC ";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setString(1, isbn);
				queryStatement.setInt(2, uid);
				ResultSet res = queryStatement.executeQuery();
				System.out.println("Query of suggested book  returned");
				System.out.println(String.format(" %10s %7s", "isbn", "sales"));
				while (res.next()) {
					String isbns = res.getString("isbn"); 
					int sales = res.getInt("commonSales"); 
					System.out.println(String.format(" %10s %7s", isbns, sales));
				}
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when querying suggested books :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (queryStatement != null) 
				queryStatement.close();
			con.setAutoCommit(true);
		}
		return 1;
	}

	public int award(int userPower, int uid)
		throws Exception {
		System.out.println("Cmd is award");
		
		if (userPower != 2) {
			System.out.println("Unauthorized, only manager can access list of awarding users");
			return -1;
		}

		PreparedStatement queryStatement = null;

		try {
			int number = Integer.parseInt(nextString("Enter the number of showing users:", false));

			try {
				String queryString =
					"SELECT tmp1.username, tmp1.num1-tmp2.num2 as score " +
					"FROM ( SELECT User.uid, User.username, count(TT.uid1) as num1 FROM User " + 
					"LEFT JOIN (     SELECT * FROM Trust WHERE Trust.trust=1 ) as TT ON User.uid=TT.uid0 " +  
					"GROUP BY User.uid ) as tmp1, ( " +
					"SELECT User.uid, count(TT.uid1) as num2 FROM User " + 
					"LEFT JOIN (     SELECT * FROM Trust WHERE Trust.trust=0 ) as TT ON User.uid=TT.uid0 " +
				   	"GROUP BY User.uid ) as tmp2  WHERE tmp1.uid=tmp2.uid and tmp1.uid != ? ORDER BY score DESC LIMIT ?";

				con.setAutoCommit(false);

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setInt(1, uid);
				queryStatement.setInt(2, number);
				ResultSet res = queryStatement.executeQuery();
				System.out.println("Query of awarding trusted users returned");
				System.out.println(String.format(" %14s %14s", "username", "trust score"));
				while (res.next()) {
					String username = res.getString("username"); 
					int score = res.getInt("score"); 
					System.out.println(String.format(" %14s %14d", username, score));
				}
				
				queryString =
					"SELECT fb.username, AVG(fb.rating) as score FROM ( " +
					"    SELECT AVG(r.score) as rating, u.username, u.uid " +
					"    FROM Rating r, Feedback f, User u " + 
					"    WHERE r.bid=f.bid and r.uid0=f.uid and u.uid != ? and u.uid=f.uid " + 
					"    GROUP BY r.uid0) AS fb " + 
					"GROUP BY fb.uid ORDER BY fb.rating DESC LIMIT ? ";

				queryStatement = con.prepareStatement(queryString);
				queryStatement.setInt(1, uid);
				queryStatement.setInt(2, number);
				res = queryStatement.executeQuery();
				System.out.println("Query of awarding useful users returned");
				System.out.println(String.format(" %14s %14s", "username", "useful score"));
				while (res.next()) {
					String username = res.getString("username"); 
					double score = res.getFloat("score"); 
					System.out.println(String.format(" %14s %14.3f", username, score));
				}
	
				con.commit();
			} catch (SQLException e ) {
				System.err.print("Error when querying awarding users :" + e.getMessage());
			}
		} catch (Exception e ) {
			System.out.println("Errors in input");
		} finally {
			if (queryStatement != null) 
				queryStatement.close();
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



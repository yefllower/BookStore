package bookstore;

public class Main {
	public static void main(String[] args) {
		try {
			Connector con = new Connector();
			HtmlOrder order = new HtmlOrder(con.con);
			order.login("u4", "4");
			UserInterface UI = new UserInterface(con.con);
			
			UI.mainLoop();

			con.closeConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

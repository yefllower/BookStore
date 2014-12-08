package bookstore;

public class Main {
	public static void main(String[] args) {
		try {
			Connector con = new Connector();
			UserInterface UI = new UserInterface(con.con);
			
			UI.mainLoop();

			con.closeConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

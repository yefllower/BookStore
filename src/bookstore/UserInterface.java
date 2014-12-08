package bookstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class UserInterface {
	private int userPower; //0 for unlogin, 1 for normal user, 2 for store manager;
	private Connection con;
	private Order order;
    private BufferedReader buffer;

	static String usageInfo = 
		"  0. exit                " + "\n" + 
		"  1. register            " + "\n" + 
		"  2. login               " + "\n" + 
		"  3. exit                " + "\n" + 
		"  4. newbook             " + "\n" + 
		"  5. newcopy             " + "\n" + 
		"  6. exit                " + "\n" + 
		"  7. exit                " + "\n" + 
		"  8. exit                " + "\n" + 
		"  9. exit                " + "\n" + 
		"  Have fun!";
	
	public UserInterface(Connection conn) throws Exception {
		userPower = 0;
		con = conn;
		buffer = new BufferedReader(new InputStreamReader(System.in));
		order = new Order(conn, buffer);
	}

	public void mainLoop() throws Exception {
		while (true) {
			int res;
			String cmd;
		
			if (userPower == 2)
				System.out.print("# ");
			else
				System.out.print("$ ");
            
			while ((cmd = buffer.readLine()) == null) ;
			if (cmd.length() == 0)
				continue;

			if (cmd.equals("0") || cmd.equals("exit")) {
				break;
			} else if (cmd.equals("1") || cmd.equals("register")) {
				res = order.register();
			} else if (cmd.equals("2") || cmd.equals("login")) {
				res = order.login();
				if (res == -1) res = 0;
				if (userPower != res && res == 2)
						System.out.println("You've logined as manager");
				userPower = res;
			} else if (cmd.equals("3") || cmd.equals("order")) {
			} else if (cmd.equals("4") || cmd.equals("newbook")) {
				res = order.newbook(userPower);
			} else if (cmd.equals("5") || cmd.equals("newcopy")) {
			} else if (cmd.equals("6") || cmd.equals("feedback")) {
			} else if (cmd.equals("7") || cmd.equals("ratebook")) {
			} else if (cmd.equals("8") || cmd.equals("trust")) {
				System.out.println("Cmd is trust");
			} else if (cmd.equals("9") || cmd.equals("browse")) {
			} else if (cmd.equals("10") || cmd.equals("usefull")) {
			} else if (cmd.equals("11") || cmd.equals("suggest")) {
			} else if (cmd.equals("12") || cmd.equals("seperate")) {
			} else if (cmd.equals("13") || cmd.equals("statics")) {
			} else if (cmd.equals("14") || cmd.equals("award")) {
			} else {
				System.out.println("Command not recognized, usages:\n" + usageInfo);
			}
		}
	}
}

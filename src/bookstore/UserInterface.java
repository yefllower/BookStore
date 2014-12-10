package bookstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class UserInterface {
	private int userPower; //0 for unlogin, 1 for normal user, 2 for store manager;
	private int uid; // the uid of logined user, -1 for unlogin
	private Connection con;
	private Order order;
    private BufferedReader buffer;

	static String usageInfo = 
		"  0. exit                " + "\n" + 
		"  1. register            " + "\n" + 
		"  2. login               " + "\n" + 
		"  3. order               " + "\n" + 
		"  4. newbook             " + "\n" + 
		"  5. newcopy             " + "\n" + 
		"  6. feedback            " + "\n" + 
		"  7. rate                " + "\n" + 
		"  8. trust               " + "\n" + 
		"  9. browse              " + "\n" + 
		" 10. useful              " + "\n" + 
		" 11. suggest             " + "\n" + 
		" 12. seperat             " + "\n" + 
		" 13. statistics          " + "\n" + 
		" 14. award               " + "\n" + 
		"  Have fun!";
	
	public UserInterface(Connection conn) throws Exception {
		userPower = 0;
		uid = -1;
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
				System.out.print("> ");
            
			while ((cmd = buffer.readLine()) == null) ;
			if (cmd.length() == 0)
				continue;

			if (cmd.equals("0") || cmd.equals("exit")) {
				break;
			} else if (cmd.equals("1") || cmd.equals("register")) {
				res = order.register();
			} else if (cmd.equals("2") || cmd.equals("login")) {
				res = order.login();
				if (res == -1) continue;
				if (userPower != res && res == 2)
						System.out.println("You've logined as manager");
				userPower = res % 10;
				uid = res / 10;
			} else if (cmd.equals("3") || cmd.equals("order")) {
				double ress = order.neworder(userPower, uid);
			} else if (cmd.equals("4") || cmd.equals("newbook")) {
				res = order.newbook(userPower);
			} else if (cmd.equals("5") || cmd.equals("newcopy")) {
				res = order.newcopy(userPower);
			} else if (cmd.equals("6") || cmd.equals("feedback")) {
				res = order.feedback(userPower, uid);
			} else if (cmd.equals("7") || cmd.equals("rate")) {
				res = order.rate(userPower, uid);
			} else if (cmd.equals("8") || cmd.equals("trust")) {
				res = order.trust(userPower, uid);
			} else if (cmd.equals("9") || cmd.equals("browse")) {
				res = order.browse(userPower, uid);
			} else if (cmd.equals("10") || cmd.equals("useful")) {
				res = order.useful(userPower, uid);
			} else if (cmd.equals("11") || cmd.equals("suggest")) {
				res = order.suggest(userPower, uid);
			} else if (cmd.equals("12") || cmd.equals("seperate")) {
				res = order.seperate(userPower, uid);
			} else if (cmd.equals("13") || cmd.equals("statistcs")) {
				res = order.statistics(userPower, uid);
			} else if (cmd.equals("14") || cmd.equals("award")) {
				res = order.award(userPower, uid);
			} else {
				System.out.println("Command '" + cmd + "' not recognized, usages:\n" + usageInfo);
			}
		}
	}
}

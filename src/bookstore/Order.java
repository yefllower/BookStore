package bookstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class Order {
	private Statement stmt;
    private BufferedReader buffer;

	public Order(Statement stmts, BufferedReader buffers) throws Exception {
		stmt = stmts;
		buffer = buffers;
	}

	public int register() throws Exception {
		System.out.println("cmd is register");
		System.out.print("Enter your username :");
		String username = next();
		System.out.print("Enter your password :");
		String password = next();
		System.out.println("Registering user \"" + username + "\" with password \"" + password + "\"" );
		return 1;
	}

	public int login() throws Exception {
		System.out.println("cmd is login");
		System.out.print("Enter your username :");
		String username = next();
		System.out.print("Enter your password :");
		String password = next();
		System.out.println("login user \"" + username + "\" with password \"" + password + "\"" );
		return 1;
	}

	private String next() throws Exception {
		String s;
		while ((s = buffer.readLine()) == null || s.length() == 0) ;
		return s;
	}
}

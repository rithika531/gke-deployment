package com.mit;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionProvider{
	
	static Connection con ;
	
	public static Connection getCon() throws IOException {
		
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("com/mit/config.properties");
		
		Properties prop = new Properties();
		prop.load(input);
		System.out.println(prop.getProperty("connectionURL")); 

		try {
			Class.forName("org.postgresql.Driver");
			con=DriverManager.getConnection(prop.getProperty("connectionURL"), prop.getProperty("username"), prop.getProperty("password"));
		}catch(Exception ex) {
			System.out.println(ex);
		}
		return con; 
	}
	

}

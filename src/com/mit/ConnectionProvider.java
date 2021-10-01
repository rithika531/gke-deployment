package com.mit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConnectionProvider{
	
	static Connection con=null ;
	
	public static Connection getCon() throws IOException {
		Properties dbProps = new Properties();
		String path = "/Users/rithika/eclipse-workspace/tomcatapp/PSQLConProject/src/resources/config.properties";
		try {
	        InputStream is = new FileInputStream(path);
	        dbProps.load(is);
	        
	        //String url = dbProps.getProperty("connectionURL");
	        //System.out.println(url); 
	    } catch (Exception e) {
	        throw new IOException("Could not read properties file");
	    }
		try {
			Class.forName("org.postgresql.Driver");
			con=DriverManager.getConnection(dbProps.getProperty("connectionURL"), dbProps.getProperty("username"), dbProps.getProperty("password"));
		}catch(Exception ex) {
			System.out.println(ex);
		}
		return con; 
	}
	

}

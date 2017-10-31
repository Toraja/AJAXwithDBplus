package logic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import resources.AppProperties;

/**
 * Helper class for database activities
 */
// Not in use
public class DBConnectionCreator {
	private String url;
	private String user;
	private String pass;
	
	public DBConnectionCreator(){
		AppProperties prop = new AppProperties();
		this.url = prop.get("db_url");
		this.user = prop.get("db_user");
		this.pass = prop.get("db_pass");
	}
	
	public DBConnectionCreator(String url, String user, String pass){
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	public Connection getSqlConnection() throws SQLException {
		return DriverManager.getConnection(this.url, this.user, this.pass);
	}
}

package logic.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import logic.db.DBConnectionCreator;

public class ITDBConnectionCreator {

	private List<String> expectedList = Arrays.asList("misc");

	/**
	 * test condition no-arg constructor is used
	 * 
	 * <pre>
	 * test point:
	 * 	connection is established properly
	 * 	intended schema is used
	 * </pre>
	 */
	@Test
	public void testGetSqlConnection() {
		try (Connection con = new DBConnectionCreator().getSqlConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("select database()");) {

			List<String> actualList = new ArrayList<>();
			while (rs.next()) {
				actualList.add(rs.getString(1));
			}

			assertEquals(expectedList, actualList);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception has been thrown");
		}
	}

	/**
	 * test condition constructor with args is used
	 * 
	 * <pre>
	 * test point:
	 * 	connection is established properly
	 * 	intended schema is used
	 * </pre>
	 */
	@Test
	public void testGetSqlConnectionWithParametersProvided() {

		try (Connection con = new DBConnectionCreator("jdbc:mysql://localhost:3306/misc?useSSL=false", "root", "")
				.getSqlConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("select database()");) {

			List<String> actualList = new ArrayList<>();
			while (rs.next()) {
				actualList.add(rs.getString(1));
			}

			assertEquals(expectedList, actualList);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception has been thrown");
		}
	}
}

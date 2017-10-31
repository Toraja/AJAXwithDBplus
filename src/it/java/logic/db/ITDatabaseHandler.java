package logic.db;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import data.DBQuery;
import data.Emp2;
import json.deserialiser.DBQueryDeserialiser;

/**
 * Make sure DB server is running
 */
public class ITDatabaseHandler {

	@Test
	public void testGetTableNames(){
		// expectation
		List<String> expected = Arrays.asList("emp", "emp2");
		List<String> actual = null;
		
		// execution
		try {
			actual = new DatabaseHandler(new Configuration()).getTableNames();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetTableColumns() {
		List<String> expectedEmp = Arrays.asList("ID", "Name");
		List<String> actualEmp = null;
		List<String> expectedEmp2 = Arrays.asList("ID", "FName", "LName", "Department", "City", "Hobby");
		List<String> actualEmp2 = null;

		try {
			actualEmp = new DatabaseHandler(new Configuration()).getTableColumns("Emp");
			actualEmp2 = new DatabaseHandler(new Configuration()).getTableColumns("Emp2");
		} catch (SQLException e) {
			fail("Unexpected exception occured");
		}

		assertEquals(expectedEmp, actualEmp);
		assertEquals(expectedEmp2, actualEmp2);
	}
	
	@Test
	public void testGetTableColumnsNoMatchingTable() {
		List<String> result = null;
		
		try {
			result = new DatabaseHandler(new Configuration()).getTableColumns("Empire");
		} catch (SQLException e) {
			fail("Unexpected exception occured");
		}
		
		assertThat(result.size(), is(0));
	}

	@Test
	public void testGetRowsMatchingWithSomeMatch() {
		// setup
		String queryJson = "{\"tableName\":\"emp2\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}";
		DBQuery query = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create()
				.fromJson(queryJson, DBQuery.class);

		// expectation
		List<Emp2> expected = Arrays.asList(new Emp2(1, "Tom", "Jones", "Accounting", "Dubai", "Gambling"),
				new Emp2(2, "Tom", "Jones", "Accounting", "Paris", "Gambling"),
				new Emp2(5, "Tom", "Jones", "Accounting", "Dubai", "Fishing"),
				new Emp2(6, "Tom", "Jones", "Accounting", "Paris", "Fishing"));

		// execution
		List<?> actual = new DatabaseHandler(new Configuration()).getRowsMatching(query);

		// verification
		assertEquals(expected, actual);
	}

	@Test
	public void testGetRowsMatchingWithNoMatch() {
		// setup
		String queryJson = "{\"tableName\":\"emp2\",\"condition\":{\"FName\":\"Bob\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}";
		DBQuery query = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create()
				.fromJson(queryJson, DBQuery.class);

		// execution
		List<?> result = new DatabaseHandler(new Configuration()).getRowsMatching(query);

		// verification
		assertTrue(result.size() == 0);		
	}
}

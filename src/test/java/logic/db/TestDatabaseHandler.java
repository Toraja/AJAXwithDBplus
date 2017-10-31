package logic.db;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.GsonBuilder;

import data.DBQuery;
import data.Emp2;
import json.deserialiser.DBQueryDeserialiser;

public class TestDatabaseHandler {
	@Mock Configuration configMock;
	@Mock SessionFactory sfMock;
	@Mock Session sessionMock;
	@Mock Connection conMock;
	@Mock DatabaseMetaData dbmMock;
	@Mock ResultSet rsMock;
	@Mock Query<Emp2> queryMock;

	@Before
	public void initMock() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(configMock.configure()).thenReturn(configMock);
		when(configMock.buildSessionFactory()).thenReturn(sfMock);
		when(sfMock.openSession()).thenReturn(sessionMock);
		when(conMock.getMetaData()).thenReturn(dbmMock);
	}

	/**
	 * This test seems to have no point... <br>
	 * Actual value is manually crafted value returned by mock
	 */
	@Test
	public void testGetTableNames() {
		// setup
		String[] tableNames = { "table1", "table2" };
		try {
			when(dbmMock.getTables(any(), any(), any(), any())).thenReturn(rsMock);
			when(rsMock.next()).thenReturn(true, true, false);
			when(rsMock.getString(3)).thenReturn(tableNames[0], Arrays.copyOfRange(tableNames, 1, tableNames.length));

			// stub Session.doWork() method
			doAnswer(lambdaForDoWork -> {
				Work work = lambdaForDoWork.getArgument(0);
				work.execute(conMock);
				return null;
			}).when(sessionMock).doWork(any());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected expectation occured");
		}

		// expectation
		List<String> expected = Arrays.asList(tableNames);

		// execution
		List<String> actual = null;
		try {
			actual = new DatabaseHandler(configMock).getTableNames();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}

		// verification
		assertEquals(expected, actual);
	}

	/**
	 * This test seems to have no point... <br>
	 * Actual value is manually crafted value returned by mock
	 */
	@Test
	public void testGetTableColumns() {
		// setup
		String tableName = "emp2";
		String[] columns = { "ID", "Name", "DOB" };

		try {
			when(dbmMock.getColumns(any(), any(), eq(tableName), any())).thenReturn(rsMock);
			when(rsMock.next()).thenReturn(true, true, true, false);
			when(rsMock.getString(4)).thenReturn(columns[0], Arrays.copyOfRange(columns, 1, columns.length));
			// `-- this returns value in the incorrect order when debugging

			// stub Session.doWork() method
			doAnswer(lambdaForDoWork -> {
				Work work = lambdaForDoWork.getArgument(0);
				work.execute(conMock);
				return null; // <-- I guess this is the return value of stubbed method (doWork in this case)
			}).when(sessionMock).doWork(any());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while mock setup?? What the hell?");
		}

		// expectation
		List<String> expected = Arrays.asList(columns);

		// execution
		List<String> actual = null;
		try {
			actual = new DatabaseHandler(configMock).getTableColumns(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}

		// verification
		try {
			assertEquals(expected, actual);
			verify(rsMock, times(3)).getString(4);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while mock verification?? What the hell?");
		}
	}

	@Test
	public void testGetTableColumnsIllegalArgument() {
		try {
			new DatabaseHandler(configMock).getTableColumns(null);

		} catch (IllegalArgumentException e) {
			assertEquals("Table name must not be null or empty", e.getMessage());
		} catch (Exception e) {
			fail("Unexpected exception occured");
		}
	}

	/**
	 * This is actually just a test for createQueryFromQueryObject()
	 */
	@Test
	public void testGetRowsMatchingWithSomeMatch() {
		// expectation
		String hql = "FROM Emp2 WHERE FName = 'Tom' AND LName = 'Jones' AND Department = 'Accounting'";
		List<Emp2> expected = Arrays.asList(new Emp2(1, "Tom", "Jones", "Accounting", "Dubai", "Gambling"),
				new Emp2(2, "Tom", "Jones", "Accounting", "Paris", "Gambling"),
				new Emp2(5, "Tom", "Jones", "Accounting", "Dubai", "Fishing"),
				new Emp2(6, "Tom", "Jones", "Accounting", "Paris", "Fishing"));

		// setup
		String queryJson = "{\"tableName\":\"emp2\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}";
		DBQuery query = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create()
				.fromJson(queryJson, DBQuery.class);

		when(sessionMock.createQuery(anyString())).thenReturn(queryMock);
		when(queryMock.list()).thenReturn(expected);

		// execution
		List<?> actual = new DatabaseHandler(configMock).getRowsMatching(query);

		// verification
		assertEquals(expected, actual);
		verify(sessionMock).createQuery(hql);
	}

	//	@Test
	// Tested method is currently not in use 
	// Tested method has been changed to private, so use reflection to test private method
	/*	
	public void testCreateQueryFromEntity() {
		Emp2 emp2 = new Emp2(new Emp2Id("john", "smith", "IT", "London", "Fishing"));
	
		String query = new DatabaseHandler(new Configuration()).createQueryFromEntity(emp2);
		String expected = "FROM Emp2 WHERE fname='john' AND lname='smith' AND department='IT' AND city='London' AND hobby='Fishing'";
	
		assertThat(query, is(expected));
	}
	*/
}

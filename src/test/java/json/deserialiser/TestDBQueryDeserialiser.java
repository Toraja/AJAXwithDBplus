package json.deserialiser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import data.DBQuery;

public class TestDBQueryDeserialiser {

	@Test
	public void testDeserialize() {
		try {

			// setup
			String queryString = "{\"tableName\":\"emp2\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}";

			// expectation
			Map<String, String> condition = new HashMap<>();
			condition.put("FName", "Tom");
			condition.put("LName", "Jones");
			condition.put("Department", "Accounting");
			condition.put("City", "");
			condition.put("Hobby", null);
			Constructor<DBQuery> constructor = DBQuery.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			DBQuery expected = constructor.newInstance();
			expected.setTableName("Emp2");
			expected.setCondition(condition);

			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			DBQuery actual = gson.fromJson(queryString, DBQuery.class);

			// verification
			assertEquals(expected, actual);
			System.out.println(actual);
		} catch (Exception e) {
			fail("Unexpected exception occured");
		}

	}

	@Test
	public void testDeserializeTableNameIsNull() {
		// setup
		String queryString = "{\"tableName\":null,\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":\"\"}}";

		try {
			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			gson.fromJson(queryString, DBQuery.class);
		} catch (JsonSyntaxException e) {
			// verification
			assertThat(e.getMessage(), is("Table name must not be null or empty"));
		}
	}

	@Test
	public void testDeserializeTableNameIsEmpty() {
		// setup
		String queryString = "{\"tableName\":\"\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":\"\"}}";

		try {
			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			gson.fromJson(queryString, DBQuery.class);
		} catch (JsonSyntaxException e) {
			// verification
			assertThat(e.getMessage(), is("Table name must not be null or empty"));
		}
	}

	@Test
	public void testDeserializeConditionIsNull() {
		// setup
		String queryString = "{\"tableName\":\"emp2\",\"condition\":null}";

		try {
			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			gson.fromJson(queryString, DBQuery.class);
		} catch (JsonSyntaxException e) {
			// verification
			assertThat(e.getMessage(), is("Condition must not be null or empty"));
		}
	}

	@Test
	public void testDeserializeConditionIsEmpty() {
		// setup
		String queryString = "{\"tableName\":\"emp2\",\"condition\":{}}";

		try {
			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			gson.fromJson(queryString, DBQuery.class);
		} catch (JsonSyntaxException e) {
			// verification
			assertThat(e.getMessage(), is("Condition must not be null or empty"));
		}
	}

	@Test
	public void testDeserializeConditionIsAllNullOrEmpty() {
		// setup
		String queryString = "{\"tableName\":\"emp2\",\"condition\":{\"FName\":null,\"LName\":null,\"Department\":null,\"City\":\"\",\"Hobby\":\"\"}}";

		try {
			// execution
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			gson.fromJson(queryString, DBQuery.class);
		} catch (JsonSyntaxException e) {
			// verification
			assertThat(e.getMessage(), is("Values of condition must not be all null or empty"));
		}
	}
}

package json.deserialiser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import data.DBQuery;

public class TestStringNonNullEnforcer {

	private DBQuery defaultDBQuery;
	private Constructor<DBQuery> constructor;

	@Before
	public void setupDefaultData() {
		try {
			this.constructor = DBQuery.class.getDeclaredConstructor();
			this.constructor.setAccessible(true);
			defaultDBQuery = this.constructor.newInstance();
//		defaultDBQuery = new DBQuery();
			Map<String, String> condition = new HashMap<>();
			condition.put("c", "cat");
			condition.put("d", "dog");
			defaultDBQuery.setTableName("the table");
			defaultDBQuery.setCondition(condition);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}
	}

	@Before
	public void printBeginningSeparator() {
//		System.out.println("--- " + Thread.currentThread().getStackTrace()[1].getMethodName() + " ---");
		System.out.println("--------------------------");
	}

	@After
	public void printEndingSeparator() {
		System.out.println();
	}

	@Test
	public void testDeserialize() {
		// setup
		String jsonString = "{\"tableName\":\"the table\",\"condition\":{\"c\":\"cat\",\"d\":\"dog\"}}";

		// expectation
		// this.defaultDBQuery;

		// execution
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(String.class, new StringNonNullEnforcer());
		Gson gson = gsonBuilder.create();
		DBQuery deserialisedObject = gson.fromJson(jsonString, DBQuery.class);

		// assertion
		assertThat(deserialisedObject, is(this.defaultDBQuery));

		// misc
		System.out.println(deserialisedObject);
	}

	/**
	 * Gson skips custom deserialiser on null value so there is no point of this
	 * deserialiser...
	 */
	//	@Test
	public void testDeserializeNull() {
		// setup
		String jsonString = "{\"tableName\":null,\"condition\":{\"c\":\"cat\",\"d\":\"dog\"}}";

		// expectation

		// execution
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(String.class, new StringNonNullEnforcer());
		Gson gson = gsonBuilder.create();
		DBQuery deserialisedObject;
		try {
			deserialisedObject = gson.fromJson(jsonString, DBQuery.class);
			fail("an exception should have been thrown by now --- " + deserialisedObject);
		} catch (JsonSyntaxException e) {
			// assertion
			assertThat(e.getMessage(), is("String field cannot be empty"));
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDeserializeEmptyString() {
		// setup
		String jsonString = "{\"tableName\":\"\",\"condition\":{\"c\":\"cat\",\"d\":\"dog\"}}";

		// expectation

		// execution
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(String.class, new StringNonNullEnforcer())
				.serializeNulls();
		Gson gson = gsonBuilder.create();
		DBQuery deserialisedObject;
		try {
			deserialisedObject = gson.fromJson(jsonString, DBQuery.class);
			fail("an exception should have been thrown by now --- " + deserialisedObject);
		} catch (JsonSyntaxException e) {
			// assertion
			assertThat(e.getMessage(), is("String field cannot be empty"));
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Test basic deserialisation
	 */
	//	@Test
	public void tryGsonDeserialiseMap() {

		String jsonString = "{\"a\":\"apple\",\"b\":\"banana\",\"c\":\"cherry\"}";

		Map<String, String> expected = new HashMap<>();
		expected.put("a", "apple");
		expected.put("b", "banana");
		expected.put("c", "cherry");

		Map<String, String> unexpected = new HashMap<>();
		unexpected.put("a", "apple");
		unexpected.put("b", "banana");
		unexpected.put("c", "coconut");

		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, String> actual = gson.fromJson(jsonString, Map.class);
		System.out.println(actual);
		System.out.println(expected);
		System.out.println(unexpected);

		assertTrue(actual.equals(expected));
		assertFalse(actual.equals(unexpected));
	}

	/**
	 * <b>Point:</b> Test how Gson handles JSON containing null<br>
	 * <br>
	 * <b>Result:</b> null will be null. But some Map implementation does not
	 * allow null so if Gson's implementation is changed to use Map which does
	 * not allow null, it might cause error. (Gson currently uses LinkedTreeMap
	 * which is google's own invention)
	 */
	//	@Test
	public void tryGsonDeserialiseJsonWithNullIntoMap() {

		String jsonString = "{\"a\":\"apple\",\"b\":\"banana\",\"c\":\"cherry\",\"d\":null}";

		Map<String, String> expected = new HashMap<>();
		expected.put("a", "apple");
		expected.put("b", "banana");
		expected.put("c", "cherry");
		expected.put("d", null);

		Map<String, String> unexpected = new HashMap<>();
		unexpected.put("a", "apple");
		unexpected.put("b", "banana");
		unexpected.put("c", "cherry");
		unexpected.put("d", "null");

		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, String> actual = gson.fromJson(jsonString, Map.class);
		System.out.println(actual);
		System.out.println(actual.getClass().getName());
		System.out.println(expected);
		System.out.println(unexpected);

		assertTrue(actual.equals(expected));
		assertTrue(actual.get("d") == null);
		assertFalse(actual.equals(unexpected));
	}

	/**
	 * <b>Point:</b> See what happens when deserialising JSON containing an
	 * Array as value into Map<String, String> (Incompatible type).<br>
	 * <br>
	 * <b>Result:</b> Deserialisation succeeds, but it fails when retrieving the
	 * array value from Map.
	 */
	//	@Test
	public void tryGsonDeserialiseJsonWithArrayIntoMapOfString() {

		String jsonString = "{\"a\":\"apple\",\"b\":\"banana\",\"c\":[\"cherry\"]}";

		Map<String, String> expected = new HashMap<>();
		expected.put("a", "apple");
		expected.put("b", "banana");
		expected.put("c", "cherry");

		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, String> actual = gson.fromJson(jsonString, Map.class);
		// System.out.println(actual.get("c").toString()); // <-- throw an exception
		System.out.println("a is " + actual.get("a").getClass().getName());
		System.out.println("b is " + actual.get("b").getClass().getName());
		System.out.println(actual);
		// System.out.println(actual.get("c").getClass().getName()); // <-- throw an exception
		System.out.println(expected);

		assertFalse(actual.equals(expected));
	}

	/**
	 * Try deserialising Json into DBQuery object
	 */
	//	@Test
	public void tryGsonDeserialiseJsonIntoDBQuery() {

		try {
			String jsonString = "{\"tableName\":\"the table\",\"condition\":{\"c\":\"cat\",\"d\":\"dog\"}}";
			DBQuery expected = this.constructor.newInstance();
			Map<String, String> condition = new HashMap<>();
			condition.put("c", "cat");
			condition.put("d", "dog");
			expected.setTableName("the table");
			expected.setCondition(condition);
			Gson gson = new Gson();
			DBQuery actual = gson.fromJson(jsonString, DBQuery.class);
			System.out.println(actual.getCondition());
			System.out.println(actual);
			System.out.println(expected);
			assertEquals(actual, expected);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}
	}

	/**
	 * <b>Points:</b> See what happens if JSON's key name and the field name of
	 * the object differ.
	 * 
	 * <b>Result:</b> the field of the object will be null (default value,
	 * probably).
	 */
	//	@Test
	public void tryGsonDeserialiseJsonFieldNameUnmatch() {

		String jsonString = "{\"table\":\"the table\",\"condition\":{\"c\":\"cat\",\"d\":\"dog\"}}";

		DBQuery expected = this.defaultDBQuery;

		Gson gson = new Gson();
		DBQuery actual = gson.fromJson(jsonString, DBQuery.class);
		System.out.println(actual.getCondition());
		System.out.println(actual);
		System.out.println(expected);

		assertNotEquals(actual, expected);
		assertNull(actual.getTableName());
	}

	// @Test
	public void tryGsonDeserialiseDBQuery() {
		String jsonString = null;

		Gson gson = new Gson();
		DBQuery dbq = gson.fromJson(jsonString, DBQuery.class);
		dbq.toString();
	}
}

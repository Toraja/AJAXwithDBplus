package json.deserialiser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestStringCapitaliser {

	@Test
	public void testDeserialize() {
		// setup
		String json = "{\"str1\":\"str1\",\"str2\":\"str2\",\"char1\":\"c\",\"int1\":1}";
		
		GsonBuilder gb = new GsonBuilder().registerTypeAdapter(String.class, new StringCapitaliser());
		Gson gson = gb.create();
		
		// expectation
		TestEntity stringCapitalised = new TestEntity("Str1", "Str2", 'c', 1);
		
		// execution
		TestEntity deserialised = gson.fromJson(json, TestEntity.class);
		
		// verification
		assertThat(deserialised, is(stringCapitalised));
		System.out.println(deserialised.toString());
	}

	private class TestEntity {
		String str1;
		String str2;
		char char1;
		int int1;
		
		/**
		 * @param str1
		 * @param str2
		 * @param char1
		 * @param int1
		 */
		public TestEntity(String str1, String str2, char char1, int int1) {
			super();
			this.str1 = str1;
			this.str2 = str2;
			this.char1 = char1;
			this.int1 = int1;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			TestEntity other = (TestEntity) obj;
			if (char1 != other.char1) return false;
			if (int1 != other.int1) return false;
			if (str1 == null) {
				if (other.str1 != null) return false;
			} else if (!str1.equals(other.str1)) return false;
			if (str2 == null) {
				if (other.str2 != null) return false;
			} else if (!str2.equals(other.str2)) return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestEntity [str1=" + str1 + ", str2=" + str2 + ", char1=" + char1 + ", int1=" + int1 + "]";
		}
	}
}

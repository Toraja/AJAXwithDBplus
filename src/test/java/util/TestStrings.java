package util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class TestStrings {

	@Test
	public void testNullOrEmptyOnEmptyString() {
		assertTrue(Strings.isNullOrEmpty(""));
	}
	
	@Test
	public void testNullOrEmptyOnNull() {
		assertTrue(Strings.isNullOrEmpty(null));
	}
	@Test
	
	public void testNullOrEmptyOnSomeString() {
		assertFalse(Strings.isNullOrEmpty("a"));
	}
	
	
	@Test
	public void testCapitaliseNull() {
		assertThat(Strings.capitalise(null), is((String)null));
	}
	@Test
	public void testCapitaliseEmpty() {
		assertThat(Strings.capitalise(""), is(""));
	}
	@Test
	public void testCapitaliseOneLetter() {
		assertThat(Strings.capitalise("x"), is("X"));
	}
	@Test
	public void testCapitaliseManyLetter() {
		assertThat(Strings.capitalise("aBc"), is("Abc"));
	}
	@Test
	public void testCapitaliseNumber() {
		assertThat(Strings.capitalise("345"), is("345"));
	}
	@Test
	public void testCapitaliseSpecialChars() {
		assertThat(Strings.capitalise(".$*"), is(".$*"));
	}

}

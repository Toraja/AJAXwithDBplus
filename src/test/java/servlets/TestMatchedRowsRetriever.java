package servlets;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestMatchedRowsRetriever {
	@Mock HttpServletRequest request;
	@Mock BufferedReader reader;
	HttpServletResponse response;
	@Mock PrintWriter writer;

	@Before
	public void initReqAndRes() {
		MockitoAnnotations.initMocks(this);
		response = new StubHttpServletResponse(writer) {
		};
	}

	/**
	 * Test for bad request such as that table name is empty
	 */
	@Test
	public void testDoGetBadRequest() {

		try {
			when(this.request.getReader()).thenReturn(this.reader);
			when(this.reader.readLine()).thenReturn(
					"{\"tableName\":\"\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}");

			new MatchedRowsRetriever().doPost(this.request, this.response);

			assertEquals(HttpServletResponse.SC_BAD_REQUEST, this.response.getStatus());
			verify(this.writer).println(anyString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}
	}

	/**
	 * Test for server side issue such as DB error
	 */
	@Test
	public void testDoGetInternalError() {

		try {
			when(this.request.getReader()).thenReturn(this.reader);
			when(this.reader.readLine()).thenReturn(
					"{\"tableName\":\"emp\",\"condition\":{\"FName\":\"Tom\",\"LName\":\"Jones\",\"Department\":\"Accounting\",\"City\":\"\",\"Hobby\":null}}");
			
			new MatchedRowsRetriever().doPost(this.request, this.response);

			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, this.response.getStatus());
			verify(this.writer).println(anyString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured");
		}
	}

}

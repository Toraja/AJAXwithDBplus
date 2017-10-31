package servlets;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestTableGetter {
	@Mock HttpServletRequest request;
	HttpServletResponse response;
	@Mock PrintWriter writer;

	@Before
	public void initReqAndRes() {
		MockitoAnnotations.initMocks(this);
		response = new StubHttpServletResponse(writer) {
		};
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {

		try {
			// execution
			new TableGetter().doGet(this.request, this.response);

			// verify
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, this.response.getStatus());
			verify(this.writer).println(anyString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}

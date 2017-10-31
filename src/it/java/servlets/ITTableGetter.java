package servlets;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class ITTableGetter {

	@Test
	public void testDoGet() {
		try {
			// setup
			URL url = new URL("http://localhost:8080/AJAXwithDBplus/TableGetter");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			// execution
			int responseCode = con.getResponseCode();
			StringBuffer responseContents = new StringBuffer();
			try (InputStreamReader isr = new InputStreamReader(con.getInputStream());
					BufferedReader br = new BufferedReader(isr)) {

				String response;
				while ((response = br.readLine()) != null) {
					responseContents.append(response);
				}
			}
			
			// verification
			assertThat(responseCode, is(200));
			assertThat(responseContents.toString(), is("[\"emp\",\"emp2\"]"));

		} catch (Exception e) {
			fail("An Exception occured");
		}
	}
}

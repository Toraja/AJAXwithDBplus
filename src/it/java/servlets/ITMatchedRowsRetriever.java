package servlets;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class ITMatchedRowsRetriever {

	@Test
	public void testDoPost() {
		try {
			// setup
			URL url = new URL("http://localhost:8080/AJAXwithDBplus/MatchedRowsRetriever");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			byte[] out = "{\"tableName\":\"emp2\",\"condition\":{\"LName\":\"Jones\",\"Hobby\":\"Fishing\"}}"
					.getBytes(StandardCharsets.UTF_8);
			int length = out.length;
			con.setFixedLengthStreamingMode(length);
			con.setRequestProperty("Content-type", "application/json; charset=UTF=8");
			try (OutputStream os = con.getOutputStream()) {
				os.write(out);
			}

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
			assertThat(responseContents.toString(), is(
					"[{\"id\":5,\"fname\":\"Tom\",\"lname\":\"Jones\",\"department\":\"Accounting\",\"city\":\"Dubai\",\"hobby\":\"Fishing\"},{\"id\":6,\"fname\":\"Tom\",\"lname\":\"Jones\",\"department\":\"Accounting\",\"city\":\"Paris\",\"hobby\":\"Fishing\"},{\"id\":7,\"fname\":\"Tom\",\"lname\":\"Jones\",\"department\":\"Sales\",\"city\":\"Dubai\",\"hobby\":\"Fishing\"},{\"id\":8,\"fname\":\"Tom\",\"lname\":\"Jones\",\"department\":\"Sales\",\"city\":\"Paris\",\"hobby\":\"Fishing\"}]"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("An Exception occured");
		}
	}
}

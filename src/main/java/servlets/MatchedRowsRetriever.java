package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import data.DBQuery;
import json.deserialiser.DBQueryDeserialiser;
import logic.db.DatabaseHandler;

/**
 * Servlet implementation class DBAccessor
 */
@WebServlet("/MatchedRowsRetriever")
public class MatchedRowsRetriever extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MatchedRowsRetriever() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try (BufferedReader reader = request.getReader()){
			String json = reader.readLine();
			Gson gson = new GsonBuilder().registerTypeAdapter(DBQuery.class, new DBQueryDeserialiser()).create();
			DBQuery query = gson.fromJson(json, DBQuery.class);

			List<?> data = new DatabaseHandler(new Configuration()).getRowsMatching(query);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Access-Control-Allow-Origin", "*");	// To avoid CORS error
			PrintWriter out = response.getWriter();
			out.println(gson.toJson(data));

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			// response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			// response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
		}

	}
}

package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;

import logic.db.DatabaseHandler;

/**
 * Servlet implementation class ColumnGetter
 */
@WebServlet("/ColumnGetter")
public class ColumnGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ColumnGetter() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String tableName = request.getParameter("table");

			List<String> columnNames = new DatabaseHandler(new Configuration()).getTableColumns(tableName);
			String json = new Gson().toJson(columnNames);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Access-Control-Allow-Origin", "*");	// To avoid CORS error
			response.getWriter().println(json);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// The sendError causes showing error page while setStatus() and PrintWriter.println()
			// only returns the message to the client 
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//	}

}

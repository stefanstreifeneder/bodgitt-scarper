package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendMailServlet
 */
@WebServlet("/SendMailServlet")
public class SendMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SendMailServlet START");
		
		try{
			SendMail.sendGMX(request.getParameter("EMAIL"),  request.getParameter("REF"), 
					request.getParameter("TEXT"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("SendMailServlet role: " + request.getParameter("ROLE"));
		
	 // displays done.jsp page after upload finished
		String role = request.getParameter("ROLE");
		if(role != null) {
			getServletContext().getRequestDispatcher("/allRecordsVendor.jsp").forward(
	                request, response);
		}else {
			getServletContext().getRequestDispatcher("/allRecordsBuyerRent.jsp").forward(
	                request, response);
		}
	}

}

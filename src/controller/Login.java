package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Bhuser;
import service.DbUser;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//this page does not require user to be logged in
		String useremail = request.getParameter("email");
		String userpassword = request.getParameter("password");
		String action = request.getParameter("action");
		String nextURL = "/error.jsp";		
		//the JSP and NavBar will read from the session 		
		HttpSession session = request.getSession();
		if (action.equals("logout")){
			session.invalidate();
			nextURL = "/login.jsp";
			
		}else {
			Bhuser user = null;
			if ( (user = DbUser.getUserByEmail(useremail)) != null 
					&& userpassword.equals(user.getUserpassword())){ //email is unique
				session.setAttribute("user", user);
				int gravatarImageWidth = 30;
				String gravatarURL = 
					DbUser.getGravatarURL(useremail, gravatarImageWidth);
				session.setAttribute("gravatarURL", gravatarURL);
				nextURL = "/index.jsp";
				
			}else{				
				nextURL = "/login.jsp";
			}
		}
		//redirect to next page as indicated by the value of the nextURL variable
		getServletContext().getRequestDispatcher(nextURL).forward(request,response);
	}

}

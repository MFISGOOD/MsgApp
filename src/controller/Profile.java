package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Bhuser;
import service.DbUser;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String nextURL = "/error.jsp";
		long userid = 0;
		Bhuser profileUser = null;
		Bhuser loggedInUser = null;
		//get user out of session. If they don't exist then send them back to the login page.
		//kill the session while you're at it.
		if (session.getAttribute("user")==null){
			nextURL = "/login.jsp";
			session.invalidate();
			response.sendRedirect(request.getContextPath() + nextURL);
		    return;//return prevents an error
		}
		try {
			 userid=Long.valueOf(request.getParameter("userid")).longValue();
			//get the user from the parameter
			profileUser = DbUser.getUser(userid);
			//get the current user out of the session
			loggedInUser = (Bhuser) session.getAttribute("user");	
			if (profileUser.getBhuserid()==loggedInUser.getBhuserid()){
				//update profile for user in request variable if action = updateprofile
				if (request.getParameter("action").equals("updateprofile")){
					long uid = Long.parseLong(request.getParameter("userid"));
					String userEmail = request.getParameter("useremail");
					String userMotto = request.getParameter("usermotto");
					Bhuser updateUser = DbUser.getUser(uid);
					updateUser.setMotto(userMotto);
					updateUser.setUseremail(userEmail);
					DbUser.update(updateUser);
				}
				//display profile as form
				//the session variable editProfile is used by the JSP to
				//display the profile in edit mode
				session.setAttribute("editProfile", true);
			}else{
				//display profile read-only
				//the session variable editProfile is used by the JSP to
				//display the profile in read-only mode
				session.setAttribute("editProfile", false);
			}
			//populate the data in the attributes
			int imgSize = 120;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
			String joindate = sdf.format(profileUser.getJoindate());
			request.setAttribute("userid", profileUser.getBhuserid());
			request.setAttribute("userimage",
					DbUser.getGravatarURL(profileUser.getUseremail(), imgSize));
			request.setAttribute("username", profileUser.getUsername());
			request.setAttribute("useremail", profileUser.getUseremail());
			request.setAttribute("usermotto", profileUser.getMotto());
			request.setAttribute("userjoindate", joindate);
			nextURL = "/profile.jsp";
		}catch(Exception e){
			//print the exception so we can see it while testing the application
			//in production it isn't a good idea to print to the console since it
			//consumes resources and will not be seen
//			System.out.println(e);
			e.printStackTrace();
		}
		//redirect to next page as indicated by the value of the nextURL variable
		getServletContext().getRequestDispatcher(nextURL)
								.forward(request,response);
				
	}

}

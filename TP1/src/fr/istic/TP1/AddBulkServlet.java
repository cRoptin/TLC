package fr.istic.TP1;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

public class AddBulkServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User oUser = userService.getCurrentUser();

		String sTitle = req.getParameter("bulkContent");
		String sPrice = req.getParameter("bulkPrice");
		float fPrice = 0;
		try {
			fPrice = Float.parseFloat(sPrice);
		} catch (Exception e) {
			fPrice = 0;
		}
		Date date = new Date();

		Bulk bulk = new Bulk(oUser, sTitle, date, fPrice);

		PersistenceManager poManager = PersistenteManager.get().getPersistenceManager();
		try {
			poManager.makePersistent(bulk);
		} finally {
			poManager.close();
		}
		resp.sendRedirect("/bulks.jsp");
	}
}

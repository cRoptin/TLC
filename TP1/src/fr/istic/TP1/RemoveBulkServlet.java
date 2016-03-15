package fr.istic.TP1;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveBulkServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String id = request.getParameter("id");
		if(id != null && id.length() != 0){
			PersistenceManager poManager = PersistenteManager.get().getPersistenceManager();
			Bulk bulk = poManager.getObjectById(Bulk.class, Long.parseLong(id));
			poManager.deletePersistent(bulk);
		}
	}
}

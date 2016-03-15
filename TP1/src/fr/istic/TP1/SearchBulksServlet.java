package fr.istic.TP1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;

public class SearchBulksServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String keyWords = request.getParameter("keyWords");
		String minPrice = request.getParameter("minPrice");
		String maxPrice = request.getParameter("maxPrice");
		String minDate = request.getParameter("minDate");
		String maxDate = request.getParameter("maxDate");

		PersistenceManager poManager = PersistenteManager.get().getPersistenceManager();

		List<Bulk> loBulks = new ArrayList<Bulk>();

		// Key Words filtrer
		if (!Strings.isNullOrEmpty(keyWords)) {
			String[] splitKeywords = keyWords.split("\\s");

			String filter = "";
			for (String keyword : splitKeywords) {
				filter = "title == '" + keyword + "'";
				loBulks.addAll(this.executeStringMatchingQuery(filter, poManager));
			}
		}

		if (!Strings.isNullOrEmpty(minPrice) && !Strings.isNullOrEmpty(maxPrice)) {
			float fMinPrice = new Float(minPrice);
			float fMaxPrice = new Float(maxPrice);
			if (loBulks.size() == 0) {
				String query = "price >= " + fMinPrice + " && price <= " + fMaxPrice;
				loBulks.addAll(this.executeQuery(query, poManager));
			} else {
				for (Iterator<Bulk> itAds = loBulks.iterator(); itAds.hasNext();) {
					Bulk bulk = itAds.next();
					if (!(bulk.getBulkPrice() >= fMinPrice && bulk.getBulkPrice() <= fMaxPrice)) {
						itAds.remove();
					}
				}
			}
		}

		if (!Strings.isNullOrEmpty(minDate) && !Strings.isNullOrEmpty(maxDate)) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date dMinDate = simpleDateFormat.parse(minDate);
				Date dMaxDate = simpleDateFormat.parse(maxDate);
				
				if (loBulks.size() == 0) {
					String query1 = "date >= :datemin";
					String query2 = "date <= :datemax";
					List<Bulk> bulkMin = this.executeQueryParameters(query1, poManager, dMinDate);
					List<Bulk> bulkMax = this.executeQueryParameters(query2, poManager, dMaxDate);
					loBulks.addAll(this.intersection(bulkMin, bulkMax));
				} else {
					for (Iterator<Bulk> itAds = loBulks.iterator(); itAds.hasNext();) {
						Bulk bulk = itAds.next();
						if (bulk.getCreationDate().after(dMaxDate) || bulk.getCreationDate().before(dMinDate)) {
							loBulks.remove(bulk);
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		try {
			JSONArray respBulks = new JSONArray();
			for (Bulk bulk : loBulks) {
				JSONObject obj = new JSONObject();
				obj.put("author", bulk.getBulkCreator());
				obj.put("date", bulk.getCreationDate());
				obj.put("key", bulk.getBulkKey());
				obj.put("price", bulk.getBulkPrice());
				obj.put("title", bulk.getBulkTitle());
				respBulks.put(obj);
			}

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(respBulks);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bulk> executeQuery(String query, PersistenceManager pm) {
		Query q = pm.newQuery(Bulk.class, query);
		q.declareImports("import java.util.Date");
		List<Bulk> loBulks = (List<Bulk>) q.execute();
		return loBulks;
	}

	@SuppressWarnings("unchecked")
	public List<Bulk> executeStringMatchingQuery(String filter, PersistenceManager pm) {
		Query query = pm.newQuery(Bulk.class, filter);
		query.declareImports("import java.util.Date");
		List<Bulk> loBulks = (List<Bulk>) query.execute();
		return loBulks;
	}

	@SuppressWarnings("unchecked")
	public List<Bulk> executeQueryParameters(String filter, PersistenceManager pm, Object parameter) {
		Query query = pm.newQuery(Bulk.class, filter);
		query.declareImports("import java.util.Date");
		List<Bulk> loBulks = (List<Bulk>) query.execute(parameter);
		return loBulks;
	}

	public List<Bulk> intersection(List<Bulk> list1, List<Bulk> list2) {
		List<Bulk> list = new ArrayList<Bulk>();
		for (Bulk ad1 : list1) {
			for (Bulk ad2 : list2) {
				if (ad1.getBulkKey() == ad2.getBulkKey()) {
					list.add(ad1);
					break;
				}
			}
		}

		return list;
	}
}

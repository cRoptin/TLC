package fr.istic.TP1;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PersistenteManager {
	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private PersistenteManager() {}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}

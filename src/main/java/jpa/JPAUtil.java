package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	private static EntityManagerFactory emf = null;
	
	static {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("devjobs");
		}
	}
	
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}

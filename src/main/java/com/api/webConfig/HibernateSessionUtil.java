package com.api.webConfig;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionUtil {

	public static ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();
	public static SessionFactory sessionFactory;
	private static HibernateSessionUtil factoryUtil;
	static {
		sessionFactory = new Configuration().configure("com/api/webConfig/hibernate.cfg.xml").buildSessionFactory();
	}
	private HibernateSessionUtil(){
		
	}
	
	public static HibernateSessionUtil getSessionFactory(){
		if(factoryUtil==null)
			factoryUtil = new HibernateSessionUtil();
		return factoryUtil;
	}
	

	
	public static Session openSession() {
		Session session = threadLocalSession.get();
		if (session == null) {
			session = sessionFactory.openSession();
			threadLocalSession.set(session);
		}
		return session;
	}

	public static void destroySession() {
		threadLocalSession.remove();
	}
	
}

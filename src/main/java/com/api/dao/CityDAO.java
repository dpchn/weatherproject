package com.api.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.api.model.Weather;
import com.api.webConfig.HibernateSessionUtil;

public class CityDAO {

	
	public boolean isCityAvailable(String city){
		Session session = HibernateSessionUtil.getSessionFactory().openSession();
		String sql = "from com.api.model.Weather where CITY =:city";
		Query query = session.createQuery(sql);
		query.setParameter("city",city);
		return query.uniqueResult() !=null;	
	}
}

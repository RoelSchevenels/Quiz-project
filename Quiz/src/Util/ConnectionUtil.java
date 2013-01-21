/**
 * paar hulpmiddelen voor database sesies te verkrijgen
 * zorgt ervoor dat er hoogstens 1 sessionfactory bestaat
 * @author vrolijkx
 */
package Util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ConnectionUtil {
	private static SessionFactory sessionFactory;
	private static Configuration configuration;
	
	public static SessionFactory configureSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			configuration = new Configuration().configure();
			
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);	
		}
	    return sessionFactory;
	}
	
	public static void createCleanDatabase() {
		if(configuration!=null) {
			SchemaExport export = new SchemaExport(configuration);
			export.setOutputFile("/Users/vrolijkx/Desktop/test.sql");
			export.create(true,true);
		}
	}
	
	public static Session getSession() {
		if (sessionFactory == null) {
			configureSessionFactory();
		}
		
		try {
			return sessionFactory.getCurrentSession();
		} catch(HibernateException e) {
			return sessionFactory.openSession();
		}
	}

	public static Session getNewSession() {
		if (sessionFactory == null) {
			configureSessionFactory();
		}
		
		return sessionFactory.openSession();
	}

	public static StatelessSession getStatelessSession() {
		if (sessionFactory == null) {
			configureSessionFactory();
		}
		
		return sessionFactory.openStatelessSession();
	}

	public static void CloseSessionFactory() {
		if(sessionFactory!=null) {
			sessionFactory.close();
			sessionFactory= null;
		}
	}
}

/**
 * paar hulpmiddelen voor database sesies te verkrijgen
 * zorgt ervoor dat er hoogstens 1 sessionfactory bestaat
 * @author vrolijkx
 */
package Util;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ConnectionUtil {
	private static SessionFactory sessionFactory;
	private static Configuration configuration;

	/**
	 * deze methode zal kijken of de database in order is.
	 * en een nieuwe database aanmaken als er geen database is.
	 * enz...
	 * 
	 * @throws bij fout in het configuratie bestand of als de database al geopend is
	 * 		   wss wordt de quiz app 2 maal geopend.
	 */
	public static void StartDataBase() throws HibernateException{
		System.out.println("start");
		configureSessionFactory();
		Session s = getNewSession();
		Transaction t = s.beginTransaction();

		//kijken of de database gepopuleert is
		try {
			System.out.println("querie uitVoeren");
			ScrollableResults r = s.createQuery("select q from Quiz q").scroll();
			r.close();
			System.out.println("querie uitgevoerd");
		} catch (HibernateException e) {
			//de database bestaat wss niet
			createCleanDatabase();
			StartDataBase();
		}

		s.close();
	}

	public static Configuration getHibernateConfiguration() throws HibernateException {
		if(configuration == null) {
			configuration = new Configuration().configure();
		}
		return configuration;
	}

	public static SessionFactory configureSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			getHibernateConfiguration();
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);	
		}
		return sessionFactory;
	}

	public static void createCleanDatabase() {
		if(configuration==null) {
			getHibernateConfiguration();
		}
		SchemaExport export = new SchemaExport(configuration);
		export.create(false,true);

	}

	public static void exportDDL(File exportFile) {
		if(configuration!=null) {
			SchemaExport export = new SchemaExport(configuration);
			export.setOutputFile(exportFile.getPath());
			export.create(false,false);
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
			sessionFactory = null;
			configuration = null;
		}
	}
}

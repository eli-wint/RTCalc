package main.java;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateIO {
	private static SessionFactory sessionFactory;
	
	public HibernateIO() {}
	
	public static void connect() {
		Configuration config = new Configuration();
		
		System.err.println("[RTC] INITIALIZING HIBERNATE AND HSQLDB");
		
		config.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver");
		config.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:RTC_DB/db;shutdown=true");
		config.setProperty("hibernate.connection.username", "SA");
		config.setProperty("hibernate.connection.password", "");
		config.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		config.setProperty("hibernate.hbm2ddl.auto", "update");
		config.setProperty("hibernate.show_sql", "false");
		
		config.addAnnotatedClass(VehicleModel.class);
		
		sessionFactory = config.buildSessionFactory();
		
		System.err.println("[RTC] DATABASE CONNECTED");
	}
	
	public static void shutdown() {
		sessionFactory.close();
	}
	
	public static void save(VehicleModel vehicle) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.persist(vehicle);
			tx.commit();
			session.close();
		}
	}
	
	public static List<VehicleModel> getVehicleModels() {
		try (Session session = sessionFactory.openSession()) {
			return session.createQuery("SELECT a FROM VehicleModel a", VehicleModel.class).getResultList();
		}
	}
}

package data;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.query.Query;
import org.junit.Test;

/**
 * Can't make it work...
 * Cannot get session
 *
 */
public class ITEmpHome {

//	@Test
	public void testFindByExample() {
		try (SessionFactory sf = new Configuration().configure().buildSessionFactory();
				Session session = sf.openSession();) {
			
			// Partial match using MatchMode
			Emp instance = new Emp("James");
			List result = session.createCriteria(Emp.class).add(Example.create(instance).enableLike(MatchMode.ANYWHERE)).list();
			
			// Partial match using wildcard char '%'
//			Emp instance = new Emp("James%");
//			List result = session.createCriteria(Emp.class).add(Example.create(instance).enableLike()).list();
			
			// Using auto-generated DAO. (DAO has to be modified a bit)
//			Emp instance = new Emp("James Mill");
//			List<Emp> result = new EmpHome().findByExample(instance);
			
			System.out.println(result);
		}
	}
	
//	@Test
	public void testHibernateCfgProperties(){
		Properties prop = new Configuration().configure().getProperties();
		prop.forEach((key, value) -> System.out.println(key + ": " + value));
	}
	
	// Cannot access to the properties in hbm.xml
//	@Test
	public void testHibernateHbmProperties(){
		Properties prop = new Configuration().configure().addResource("Emp.hbm.xml").getProperties();
		prop.forEach((key, value) -> System.out.println(key + ": " + value));
	}
}

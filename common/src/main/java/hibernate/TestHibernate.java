package hibernate;

import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.StandardBasicTypes;

public class TestHibernate {

  public static void testHibernate() {
    var dbUrl = "jdbc:tc:postgresql:16.2:///databasename";

    // Create Hibernate SessionFactory and Session
    var props = new Properties();
    props.setProperty("hibernate.connection.url", dbUrl);
    props.setProperty("show_sql", "true");
    SessionFactory sessionFactory = new Configuration().addProperties(props).buildSessionFactory();

    Session session = sessionFactory.openSession();

    try {
      var rows =
          session
              .createNativeQuery("SELECT CAST('[1,2,3]' as jsonb) as data, 2 as id", Object[].class)
              .addScalar("data")
              .addScalar("id", StandardBasicTypes.INTEGER)
              .list();
      rows.forEach(row -> System.out.println(row[0].getClass() + " -> " + row[0]));
    } finally {
      session.close();
      sessionFactory.close();
    }
  }
}

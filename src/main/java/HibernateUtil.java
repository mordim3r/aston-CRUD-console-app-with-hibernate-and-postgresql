import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure()
                    .buildSessionFactory();
        }

        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory testSessionFactory) {
        sessionFactory = testSessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserStorageIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    @BeforeAll
    static void initSessionFactory() {


        Properties settings = new Properties();
        settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        settings.put("hibernate.connection.url", POSTGRES.getJdbcUrl());
        settings.put("hibernate.connection.username", POSTGRES.getUsername());
        settings.put("hibernate.connection.password", POSTGRES.getPassword());
        settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        settings.put("hibernate.hbm2ddl.auto", "create-drop");
        settings.put("hibernate.show_sql", "false");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata();

        sessionFactory = metadata.buildSessionFactory();

        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        session.createMutationQuery("delete from User").executeUpdate();
        transaction.commit();

        transaction = session.beginTransaction();
    }

    @AfterEach
    void rollbackAndCloseSession() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    // ---------- Тесты ----------

    @Test
    void saveUser_and_findById() {
        UserStorage userStorage = new UserStorage();

        User user = new User("Alice", "alice@example.com", 30);
        userStorage.saveUser(user);

        User found = session.find(User.class, user.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
        assertEquals("alice@example.com", found.getEmail());
        assertEquals(30, found.getAge());
    }

    @Test
    void findAll_returnsAllUsers() {
        UserStorage userStorage = new UserStorage();

        User u1 = new User("Bob", "bob@example.com", 25);
        User u2 = new User("Carol", "carol@example.com", 28);

        session.persist(u1);
        session.persist(u2);
        transaction.commit();
        transaction = session.beginTransaction();

        List<User> all = userStorage.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(u -> u.getName().equals("Bob")));
        assertTrue(all.stream().anyMatch(u -> u.getName().equals("Carol")));
    }

    @Test
    void updateUser_changesData() {
        UserStorage userStorage = new UserStorage();

        User user = new User("Dave", "dave@example.com", 40);

        session.persist(user);
        transaction.commit();
        transaction = session.beginTransaction();

        user.setName("Dave Updated");
        user.setEmail("dave.updated@example.com");
        user.setAge(41);

        userStorage.updateUser(user);

        User refreshed = session.find(User.class, user.getId());
        assertEquals("Dave Updated", refreshed.getName());
        assertEquals("dave.updated@example.com", refreshed.getEmail());
        assertEquals(41, refreshed.getAge());
    }

    @Test
    void deleteUser_removesFromDatabase() {
        UserStorage userStorage = new UserStorage();

        User user = new User("Eve", "eve@example.com", 35);

        session.persist(user);
        transaction.commit();
        transaction = session.beginTransaction();

        userStorage.deleteUser(user.getId());
        session.clear();

        User deleted = session.find(User.class, user.getId());
        assertNull(deleted);
    }
}
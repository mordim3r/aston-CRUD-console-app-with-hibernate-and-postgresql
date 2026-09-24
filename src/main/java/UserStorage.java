
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;




import java.util.List;

public class UserStorage implements UserRepository {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();
    @Override
    public void saveUser(User user) {
        if (user==null){
            throw new IllegalArgumentException("User не может быть null");
        }
        Transaction transaction =null;
        try (Session session = factory.openSession()){
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();

        }catch (RuntimeException e){
            if (transaction !=null && transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(int id) {

    }
}

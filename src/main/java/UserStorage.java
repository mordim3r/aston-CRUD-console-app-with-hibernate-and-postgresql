
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

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
        try (Session session = factory.openSession()){
            return session.find(User.class, id);

        }

    }

    @Override
    public List<User> findAll() {
        try ( Session session = factory.openSession()){
            String hql = "from User";
            SelectionQuery <User> query = session.createSelectionQuery(hql,User.class);
            return query.getResultList();
        }
    }

    @Override
    public void updateUser(User user) {
        if (user==null){
            throw new IllegalArgumentException("User не может быть null");
        }
        if (user.getId()<=0){
            throw new IllegalArgumentException("Для изменения записи нужен корректный id");
        }
        Transaction transaction =null;
        try(Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            User existingUser = session.find(User.class, user.getId());
            if (existingUser==null){
                transaction.rollback();
                throw new IllegalArgumentException("Пользователь с таким id не найден");
            }
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());
            transaction.commit();
        } catch (RuntimeException e){
            if (transaction != null && transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    @Override
    public void deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Для удаления нужен корректный id");
        }
        Transaction transaction =null;
        try (Session session = factory.openSession()){
            transaction = session.beginTransaction();
            User existingUser = session.find(User.class, id);
            if (existingUser == null) {
                transaction.rollback();
                throw new IllegalArgumentException("Пользователь с таким id не найден");
            }
            session.remove(existingUser);
            transaction.commit();
        }catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }
}

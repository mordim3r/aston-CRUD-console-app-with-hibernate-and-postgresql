import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        User user = new User("Andrew", "andrew@gmail.com", 22);
        UserStorage userStorage = new UserStorage();
        userStorage.saveUser(user);
        System.out.println(user);
        HibernateUtil.shutdown();
    }
}

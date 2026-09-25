import org.hibernate.SessionFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
       // User user = new User("Andrew", "andrew2@gmail.com", 22);
        UserStorage userStorage = new UserStorage();
      //  userStorage.saveUser(user);
      //  System.out.println(user);
       // System.out.println("----find----");
       // System.out.println(userStorage.findById(user.getId()));

        List<User> users = userStorage.findAll();
        users.forEach(System.out::println);
        User user = userStorage.findById(3);
        user.setName("Alex");
        userStorage.updateUser(user);
        System.out.println(userStorage.findById(3));
        HibernateUtil.shutdown();
    }
}

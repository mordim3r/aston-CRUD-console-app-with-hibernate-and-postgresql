import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User findById(int id);
    List<User> findAll();
    void updateUser(User user);
    void deleteUser(int id);

}

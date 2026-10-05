import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String name, String email, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Некорректный возраст");
        }

        User user = new User(name, email, age);
        userRepository.saveUser(user);
    }

    public User getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(int id, String name, String email, int age) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Некорректный возраст");
        }

        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь с таким id не найден");
        }

        existingUser.setName(name);
        existingUser.setEmail(email);
        existingUser.setAge(age);
        userRepository.updateUser(existingUser);
    }

    public void deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        userRepository.deleteUser(id);
    }
}
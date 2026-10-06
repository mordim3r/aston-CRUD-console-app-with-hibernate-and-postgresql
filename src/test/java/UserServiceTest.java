//проверить не было ли взаимодействий с userRepository
//verifyNoInteractions(userRepository)

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserService userService;

    //---------------add user-----------------
    @Test
    void addUser_shouldThrow_whenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser(null, "r@gmail.com", 20));
        verifyNoInteractions(userRepository);

    }

    @Test
    void addUser_shouldThrow_whenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("", "r@gmail.com", 20));
        verifyNoInteractions(userRepository);

    }

    @Test
    void addUser_shouldThrow_whenEmailIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("Andrew", null, 20));
        verifyNoInteractions(userRepository);

    }

    @Test
    void addUser_shouldThrow_whenEmailIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("Andrew", "", 20));
        verifyNoInteractions(userRepository);

    }

    @Test
    void addUser_shouldThrow_whenAgeIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("Alice", "a@b.com", -10));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_shouldThrow_whenAgeTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUser("Alice", "a@b.com", 151));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_shouldSave_whenDataValid() {
        userService.addUser("Mike", "mike135@gmai.com", 30);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(captor.capture());
        User saved = captor.getValue();
        assertEquals("Mike", saved.getName());
        assertEquals("mike135@gmai.com", saved.getEmail());
        assertEquals(30, saved.getAge());
    }


    //----------------------getUserById-------------------------------
    @Test
    void getUserById_shouldThrow_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(0));
        verifyNoInteractions(userRepository);
    }

    @Test
    void getUserById_shouldThrow_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(-1));
        verifyNoInteractions(userRepository);
    }

    @Test
    void getUserById_shouldReturnUser_whenFound() {
        User user = new User("Alice", "alice@example.com", 30);
        when(userRepository.findById(1)).thenReturn(user);
        User result = userService.getUserById(1);
        assertEquals(user, result);
        verify(userRepository).findById(1);
    }
//-----------------------------getAllUsers-------------------------

    @Test
    void getAllUsers_shouldReturn_allUsers() {
        List<User> users = List.of(
                new User("Alice", "alice@example.com", 30),
                new User("Bob", "bob@example.com", 25)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    //---------------updateUsers----------------------

    @Test
    void updateUser_shouldThrow_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(0, "Alice", "a@b.com", 20));
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_shouldThrow_whenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1, null, "a@b.com", 20));
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_shouldThrow_whenEmailIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1, "Alice", null, 20));
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_shouldThrow_whenAgeIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1, "Alice", "a@b.com", 151));
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_shouldUpdateUser_whenDataValid() {
        User existing = new User("Old", "old@example.com", 20);
        when(userRepository.findById(1)).thenReturn(existing);

        userService.updateUser(1, "New", "new@example.com", 25);

        assertEquals("New", existing.getName());
        assertEquals("new@example.com", existing.getEmail());
        assertEquals(25, existing.getAge());
        verify(userRepository).updateUser(existing);
    }


    @Test
    void deleteUser_shouldThrow_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(0));
        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteUser_shouldThrow_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1));
        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteUser_shouldCallRepository_whenIdIsPositive() {
        userService.deleteUser(1);
        verify(userRepository).deleteUser(1);
    }
}

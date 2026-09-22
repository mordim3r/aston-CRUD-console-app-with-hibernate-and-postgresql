import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
//@Entity означает что один объект User будет соответствовать одной строке в таблице
//@Table(name="user") связывает класс User и таблицу users
@Entity
@Table(name = "users")
public class User {
    //@Id указывает на ключевое поле
    //@GeneratedValue и IDENTITY указывают, что бд сама создает ключ автоматически при выполнении INSERT
    //@Column связывает явно переменную в коде со столбцом в бд
    //@CreationTimestamp указывает на то, что Hibernate заполнит время автоматически при INSERT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable = false, length = 100)
    private String name;
    @Column(name="email", nullable = false, length = 100, unique = true)
    private String email;
    @Column(name = "age")
    private int age;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User(String name, String email, int age) {

        this.name = name;
        this.email = email;
        this.age = age;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

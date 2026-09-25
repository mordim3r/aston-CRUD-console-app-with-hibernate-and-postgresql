import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserStorage userStorage = new UserStorage();
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("""
                    1-Добавить пользователя
                    2-Найти по ID пользователя
                    3-Показать всех пользователей
                    4-Изменить пользователя
                    5-Удалить пользователя
                    0-Выход
                    """);
            String choice = scanner.nextLine();
            if (choice.equals("0")){
                break;
            }
            System.out.println("Выбран пункт: "+ choice);
            switch (choice){
                case "1"->{
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();

                    System.out.print("Введите email: ");
                    String email = scanner.nextLine();

                    System.out.print("Введите возраст: ");

                    try {
                        int age = Integer.parseInt(scanner.nextLine());
                        User user = new User(name, email, age);
                        userStorage.saveUser(user);
                        System.out.println("Пользователь добавлен: " + user);
                    } catch (NumberFormatException e) {
                        System.out.println("Возраст должен быть целым числом");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.print("Введите id пользователя: ");

                    try {
                        int id = Integer.parseInt(scanner.nextLine());
                        User user = userStorage.findById(id);

                        if (user == null) {
                            System.out.println("Пользователь не найден");
                        } else {
                            System.out.println(user);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID должен быть целым числом");
                    }
                }
                case "3"->{
                    List<User> users = userStorage.findAll();
                    users.forEach(System.out::println);
                }
                case "4"->{
                    try {
                    System.out.print("Введите ID пользователя: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    User user = userStorage.findById(id);
                    if (user == null) {
                        System.out.println("Пользователь с таким id не найден");
                        break;
                    }

                    System.out.print("Новое имя: ");
                    String name = scanner.nextLine();

                    System.out.print("Новый email: ");
                    String email = scanner.nextLine();

                    System.out.print("Новый возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);

                    userStorage.updateUser(user);
                    System.out.println("Пользователь обновлён");
                } catch (NumberFormatException e) {
                    System.out.println("ID и возраст должны быть целыми числами");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
                case "5"->{
                    System.out.println("Введите id пользователя для удаления: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine());
                        userStorage.deleteUser(id);
                        System.out.println("Пользователь удален");
                    }catch (NumberFormatException e){
                        System.out.println("Ошибка! id должен быть целым числом");
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                }
                default -> System.out.println("Выберите 1-5");
            }
        }
//        User user = new User("Andrew2", "andrew2@gmail.com", 25);
        HibernateUtil.shutdown();
    }
}

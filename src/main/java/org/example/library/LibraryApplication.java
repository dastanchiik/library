package org.example.library;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.library.model.Book;
import org.example.library.model.Category;
import org.example.library.model.Role;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class LibraryApplication {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    public void initData() {
        if (bookRepository.count() == 0) {
            List<Book> books = List.of(

                    createBook("И дольше века длится день", "Чынгыз Айтматов", Category.FICTION, 850.0, 10,
                            "Философский роман о судьбе человека, памяти и космосе. История Едигея, пытающегося похоронить друга на старом кладбище."),
                    createBook("Белый пароход", "Чынгыз Айтматов", Category.FICTION, 600.0, 15,
                            "Трогательная и трагическая повесть о мальчике, который верил в сказку о Рогатой матери-оленихе."),
                    createBook("Плаха", "Чынгыз Айтматов", Category.FICTION, 900.0, 12,
                            "Глубокое произведение о поиске Бога, человеческой жестокости и судьбе синих волков."),
                    createBook("Первый учитель", "Чынгыз Айтматов", Category.FICTION, 450.0, 20,
                            "История о становлении образования в далеких аилах и самопожертвовании учителя Дюйшена."),
                    createBook("Джамиля", "Чынгыз Айтматов", Category.FICTION, 500.0, 18,
                            "Повесть, которую Луи Арагон назвал самой красивой историей любви в мире."),


                    createBook("Манас (сокр. вариант)", "Саякбай Каралаев", Category.HISTORY, 1500.0, 5,
                            "Величайший кыргызский эпос, включенный в список ЮНЕСКО. История легендарного батыра Манаса."),
                    createBook("Семетей", "Саякбай Каралаев", Category.HISTORY, 1200.0, 7,
                            "Вторая часть трилогии эпоса, повествующая о сыне Манаса."),
                    createBook("Сын Киргизии", "Тугельбай Сыдыкбеков", Category.FICTION, 700.0, 10,
                            "Классический роман о жизни и труде простых людей в советский период."),
                    createBook("Любовь и надежда", "Алыкул Осмонов", Category.POETRY, 400.0, 25,
                            "Сборник стихотворений великого кыргызского поэта, воспевающего красоту Иссык-Куля."),


                    createBook("Кыргызы: Этногенез", "Булат Юнусалиев", Category.SCIENCE, 1100.0, 8,
                            "Научный труд о происхождении и формировании кыргызского народа."),
                    createBook("Древние тюрки", "Лев Гумилев", Category.HISTORY, 950.0, 12,
                            "История тюркских каганатов, кочевавших по территории Центральной Азии."),
                    createBook("Искусство Кыргызстана", "Сборник", Category.ART, 2500.0, 3,
                            "Иллюстрированное издание о национальных орнаментах, ремеслах и живописи."),


                    createBook("Кочевье", "Мукай Элебаев", Category.FICTION, 550.0, 10,
                            "Первый кыргызский автобиографический роман о трагических событиях 1916 года."),
                    createBook("Кандуу жылдар", "Аалы Токомбаев", Category.HISTORY, 800.0, 9,
                            "Историческое повествование о судьбоносных временах для кыргызского народа."),
                    createBook("Сказки небесных гор", "Народное творчество", Category.FICTION, 350.0, 30,
                            "Сборник волшебных сказок и легенд кыргызского народа для детей и взрослых."),
                    createBook("Кыргызская кухня", "Т. Ахмедов", Category.LIFESTYLE, 1200.0, 15,
                            "Рецепты традиционных блюд: от бешбармака до боорсоков с пошаговыми фото."),
                    createBook("Тенгрианство", "Чокан Валиханов", Category.SCIENCE, 750.0, 10,
                            "Исследование древних верований кочевых народов Центральной Азии."),
                    createBook("Сын гор", "Касымалы Баялинов", Category.FICTION, 480.0, 14,
                            "Повесть о жизни в горах и преодолении трудностей."),
                    createBook("Иссык-Куль: Жемчужина", "Фотоальбом", Category.TRAVEL, 1800.0, 6,
                            "Подарочное издание с захватывающими видами священного озера."),
                    createBook("Слова назидания", "Абай Кунанбаев", Category.PHILOSOPHY, 650.0, 20,
                            "Философские мысли и мудрость сопредельных народов, популярные в регионе.")
            );

            bookRepository.saveAll(books);
        }
    }

    private Book createBook(String title, String author, Category category, double price, int stock, String description) {
        return Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .price(price)
                .stock(stock)
                .description(description)
                .salesCount(0)
                .isbn("ISBN-" + (int)(Math.random() * 100000))
                .build();
    }}

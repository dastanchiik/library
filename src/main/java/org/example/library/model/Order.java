package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // Теперь таблица называется заказы
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime orderDate; // Дата заказа

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // СТАТУС: "НОВЫЙ", "ВЫДАН", "ОТМЕНЕН"

    private int quantity; // Количество (обычно 1, но вдруг купят 2)

    private int totalPrice; // Сумма заказа на момент покупки
}
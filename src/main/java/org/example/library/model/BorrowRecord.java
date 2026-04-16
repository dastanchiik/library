package org.example.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "last_read_page")
    private Integer lastReadPage = 0;

    @Column(name = "is_purchased")
    private boolean isPurchased;

    @Column(name = "total_points_earned")
    private Integer totalPointsEarned = 0;

    @Column(name = "is_completed")
    private boolean isCompleted;

    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

}
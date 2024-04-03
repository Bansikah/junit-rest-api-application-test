package org.bansikah.bookjunittest2;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_record")
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long bookId;
    @NonNull
    private String name;
    @NonNull
    private String summary;
    private int rating;

}

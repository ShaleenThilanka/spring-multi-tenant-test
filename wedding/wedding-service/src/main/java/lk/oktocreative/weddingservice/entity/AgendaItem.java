package lk.oktocreative.weddingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agenda_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String time;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false, length = 280)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;
}

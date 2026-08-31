package lk.oktocreative.weddingservice.entity;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "wedding_tables")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WeddingTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer tableNumber;

    @Column(nullable = false)
    private Integer capacity = 12;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Guest> guests;


}
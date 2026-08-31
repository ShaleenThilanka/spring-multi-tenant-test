package lk.oktocreative.weddingservice.entity;
import jakarta.persistence.*;

import lk.oktocreative.weddingservice.enums.RsvpStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "guests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String inviteCode; // e.g. "kavindya-shaleen-a3f9"

    @Column(nullable = false)
    private Integer plusOneCount = 0; // extra seats this guest brings (0 if solo)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private WeddingTable table; // nullable until assigned

    @Enumerated(EnumType.STRING)
    private RsvpStatus rsvpStatus = RsvpStatus.PENDING;

    private LocalDateTime respondedAt;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Photo> photos;
}

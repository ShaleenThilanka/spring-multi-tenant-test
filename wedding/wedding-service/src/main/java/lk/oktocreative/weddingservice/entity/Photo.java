package lk.oktocreative.weddingservice.entity;
import jakarta.persistence.*;

import lk.oktocreative.weddingservice.enums.PhotoType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest; // nullable - preshoot photos have no guest

    @Enumerated(EnumType.STRING)
    private PhotoType type;

    @Column(nullable = false)
    private String filePath;

    @Builder.Default
    private Boolean approved = true; // set false if you want a moderation queue

    @Column(nullable = false)
    private LocalDateTime uploadedAt;


}

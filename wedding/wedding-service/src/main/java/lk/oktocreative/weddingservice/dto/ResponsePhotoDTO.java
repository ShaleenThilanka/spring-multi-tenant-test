package lk.oktocreative.weddingservice.dto;
import lk.oktocreative.weddingservice.enums.PhotoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePhotoDTO {

    private Long id;
    private Long guestId;
    private String guestName;
    private String fileUrl;
    private PhotoType type;
    private Boolean approved;
    private LocalDateTime uploadedAt;
}

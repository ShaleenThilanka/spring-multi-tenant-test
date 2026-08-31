package lk.oktocreative.weddingservice.dto;

import lk.oktocreative.weddingservice.enums.RsvpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GuestResponseDTO {
    private Long guestId;
    private String name;
    private String inviteCode;
    private Integer plusOneCount;
    private RsvpStatus rsvpStatus;

    private Long tableId;
    private Integer tableNumber;
}

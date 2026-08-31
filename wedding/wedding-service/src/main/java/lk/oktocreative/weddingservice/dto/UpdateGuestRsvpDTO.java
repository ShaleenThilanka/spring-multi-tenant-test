package lk.oktocreative.weddingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateGuestRsvpDTO {
    private Long guestId;
    private String rsvpStatus;
}

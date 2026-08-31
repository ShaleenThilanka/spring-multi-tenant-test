package lk.oktocreative.weddingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseWeddingTableDTO {
    private Long id;

    private Integer tableNumber;

    private Integer capacity;

    private Integer occupiedSeats;

    private Integer availableSeats;

    private Integer guestCount;
}

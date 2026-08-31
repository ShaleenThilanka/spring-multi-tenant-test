package lk.oktocreative.weddingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestGuestDTO {
    private String name;
    private Integer plusOneCount;
}

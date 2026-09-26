package lk.oktocreative.weddingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAgendaDTO {
    private String time;
    private String title;
    private String description;
    private Integer sortOrder;
}

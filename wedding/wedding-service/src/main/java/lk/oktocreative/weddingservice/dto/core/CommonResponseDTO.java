package lk.oktocreative.weddingservice.dto.core;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonResponseDTO {
    private int code;
    private String message;
    private Object data;
    private ArrayList<Object> records;
}

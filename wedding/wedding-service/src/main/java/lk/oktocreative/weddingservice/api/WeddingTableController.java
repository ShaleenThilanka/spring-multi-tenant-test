package lk.oktocreative.weddingservice.api;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestWeddingTableDTO;
import lk.oktocreative.weddingservice.dto.ResponseWeddingTableDTO;
import lk.oktocreative.weddingservice.service.TableService;
import lk.oktocreative.weddingservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wedding-table")
@RequiredArgsConstructor
@CrossOrigin
public class WeddingTableController {

    private final TableService tableService;

    @PostMapping("/visitor/save")
    public ResponseEntity<StandardResponse> save(
            @RequestBody RequestWeddingTableDTO dto
    ) throws IOException, SQLException {

        CommonResponseDTO responseData = tableService.save(dto);

        return new ResponseEntity<>(
                new StandardResponse(
                        responseData.getCode(),
                        responseData.getMessage(),
                        responseData.getData()
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/visitor/all")
    public ResponseEntity<StandardResponse> getAll() {

        List<ResponseWeddingTableDTO> data = tableService.getAll();

        return ResponseEntity.ok(
                new StandardResponse(
                        200,
                        "Wedding tables fetched successfully!",
                        data
                )
        );
    }

    @GetMapping("/visitor/{id}")
    public ResponseEntity<StandardResponse> getById(
            @PathVariable Long id
    ) {

        ResponseWeddingTableDTO data = tableService.getById(id);

        return ResponseEntity.ok(
                new StandardResponse(
                        200,
                        "Wedding table fetched successfully!",
                        data
                )
        );
    }

    @GetMapping("/visitor/{id}/available-seats")
    public ResponseEntity<StandardResponse> getAvailableSeats(
            @PathVariable Long id
    ) {

        Integer availableSeats = tableService.getAvailableSeats(id);

        return ResponseEntity.ok(
                new StandardResponse(
                        200,
                        "Available seats fetched successfully!",
                        availableSeats
                )
        );
    }

}
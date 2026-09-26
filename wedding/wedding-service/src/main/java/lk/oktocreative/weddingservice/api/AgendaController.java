package lk.oktocreative.weddingservice.api;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestAgendaDTO;
import lk.oktocreative.weddingservice.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/agenda")
@RequiredArgsConstructor
@CrossOrigin
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public CommonResponseDTO getAll() {
        return new CommonResponseDTO(
                200,
                "Agenda fetched successfully",
                agendaService.getAll(),
                new ArrayList<>()
        );
    }

    @PostMapping
    public CommonResponseDTO save(@RequestBody RequestAgendaDTO dto) {
        return agendaService.save(dto);
    }

    @DeleteMapping("/{id}")
    public CommonResponseDTO delete(@PathVariable Long id) {
        return agendaService.delete(id);
    }
}

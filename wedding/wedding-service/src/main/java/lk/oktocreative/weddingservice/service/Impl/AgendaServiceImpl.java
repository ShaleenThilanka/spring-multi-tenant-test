package lk.oktocreative.weddingservice.service.Impl;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestAgendaDTO;
import lk.oktocreative.weddingservice.dto.ResponseAgendaDTO;
import lk.oktocreative.weddingservice.entity.AgendaItem;
import lk.oktocreative.weddingservice.repo.AgendaRepo;
import lk.oktocreative.weddingservice.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepo agendaRepo;

    @Override
    public CommonResponseDTO save(RequestAgendaDTO dto) {
        if (dto.getTime() == null || dto.getTime().isBlank()) {
            throw new RuntimeException("Time is required");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new RuntimeException("Description is required");
        }

        int nextOrder = dto.getSortOrder() != null
                ? dto.getSortOrder()
                : agendaRepo.findAll().stream()
                    .mapToInt(item -> item.getSortOrder() == null ? 0 : item.getSortOrder())
                    .max()
                    .orElse(0) + 1;

        AgendaItem item = AgendaItem.builder()
                .time(dto.getTime().trim())
                .title(dto.getTitle().trim())
                .description(dto.getDescription().trim())
                .sortOrder(nextOrder)
                .build();

        agendaRepo.save(item);

        return new CommonResponseDTO(
                201,
                "Agenda item saved successfully",
                map(item),
                new ArrayList<>()
        );
    }

    @Override
    public List<ResponseAgendaDTO> getAll() {
        return agendaRepo.findAllByOrderBySortOrderAscIdAsc().stream().map(this::map).toList();
    }

    @Override
    public CommonResponseDTO delete(Long id) {
        AgendaItem item = agendaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda item not found"));
        agendaRepo.delete(item);
        return new CommonResponseDTO(200, "Agenda item deleted", id, new ArrayList<>());
    }

    private ResponseAgendaDTO map(AgendaItem item) {
        return ResponseAgendaDTO.builder()
                .id(item.getId())
                .time(item.getTime())
                .title(item.getTitle())
                .description(item.getDescription())
                .sortOrder(item.getSortOrder())
                .build();
    }
}

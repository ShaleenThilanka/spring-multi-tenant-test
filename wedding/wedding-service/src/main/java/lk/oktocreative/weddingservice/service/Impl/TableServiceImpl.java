package lk.oktocreative.weddingservice.service.Impl;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestWeddingTableDTO;
import lk.oktocreative.weddingservice.dto.ResponseWeddingTableDTO;
import lk.oktocreative.weddingservice.entity.WeddingTable;
import lk.oktocreative.weddingservice.repo.WeddingTableRepo;
import lk.oktocreative.weddingservice.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final WeddingTableRepo weddingTableRepo;

    @Override
    public CommonResponseDTO save(RequestWeddingTableDTO dto) {

        if (weddingTableRepo.existsByTableNumber(dto.getTableNumber())) {
            throw new RuntimeException("Table number already exists");
        }

        WeddingTable table = WeddingTable.builder()
                .tableNumber(dto.getTableNumber())
                .capacity(dto.getCapacity() == null ? 12 : dto.getCapacity())
                .build();

        table = weddingTableRepo.save(table);

        return new CommonResponseDTO(
                201,
                "Wedding table was saved!",
                table.getId(),
                new ArrayList<>()
        );
    }

    @Override
    public List<ResponseWeddingTableDTO> getAll() {

        return weddingTableRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ResponseWeddingTableDTO getById(Long id) {

        WeddingTable table = weddingTableRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        return convertToDto(table);
    }

    @Override
    public Integer getAvailableSeats(Long tableId) {

        WeddingTable table = weddingTableRepo.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        int occupiedSeats = calculateOccupiedSeats(table);

        return table.getCapacity() - occupiedSeats;
    }

    private ResponseWeddingTableDTO convertToDto(WeddingTable table) {

        int occupiedSeats = calculateOccupiedSeats(table);

        return ResponseWeddingTableDTO.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .occupiedSeats(occupiedSeats)
                .availableSeats(table.getCapacity() - occupiedSeats)
                .guestCount(
                        table.getGuests() == null ?
                                0 :
                                table.getGuests().size()
                )
                .build();
    }

    private int calculateOccupiedSeats(WeddingTable table) {

        if (table.getGuests() == null) {
            return 0;
        }

        return table.getGuests()
                .stream()
                .mapToInt(g -> 1 + (g.getPlusOneCount() == null ? 0 : g.getPlusOneCount()))
                .sum();
    }
}

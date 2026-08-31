package lk.oktocreative.weddingservice.service;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestWeddingTableDTO;
import lk.oktocreative.weddingservice.dto.ResponseWeddingTableDTO;

import java.util.List;

public interface TableService {
    CommonResponseDTO save(RequestWeddingTableDTO dto);

    List<ResponseWeddingTableDTO> getAll();

    ResponseWeddingTableDTO getById(Long id);

    Integer getAvailableSeats(Long tableId);
}

package lk.oktocreative.weddingservice.service;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestAgendaDTO;
import lk.oktocreative.weddingservice.dto.ResponseAgendaDTO;

import java.util.List;

public interface AgendaService {
    CommonResponseDTO save(RequestAgendaDTO dto);
    List<ResponseAgendaDTO> getAll();
    CommonResponseDTO delete(Long id);
}

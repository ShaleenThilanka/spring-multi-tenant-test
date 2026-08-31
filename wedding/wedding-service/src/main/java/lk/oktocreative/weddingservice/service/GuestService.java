package lk.oktocreative.weddingservice.service;

import lk.oktocreative.weddingservice.dto.AssignGuestTableDTO;
import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestGuestDTO;
import lk.oktocreative.weddingservice.dto.UpdateGuestRsvpDTO;

public interface GuestService {
    CommonResponseDTO save(RequestGuestDTO dto);

    CommonResponseDTO search(String name);

    CommonResponseDTO updateRsvp(UpdateGuestRsvpDTO dto);

    CommonResponseDTO assignTable(AssignGuestTableDTO dto);

    CommonResponseDTO getAllGuestsWithTables();
}

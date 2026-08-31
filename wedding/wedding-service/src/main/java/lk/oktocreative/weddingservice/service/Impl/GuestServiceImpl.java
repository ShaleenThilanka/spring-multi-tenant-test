package lk.oktocreative.weddingservice.service.Impl;

import lk.oktocreative.weddingservice.dto.*;
import lk.oktocreative.weddingservice.entity.Guest;
import lk.oktocreative.weddingservice.entity.WeddingTable;
import lk.oktocreative.weddingservice.enums.RsvpStatus;
import lk.oktocreative.weddingservice.repo.GuestRepo;
import lk.oktocreative.weddingservice.repo.WeddingTableRepo;
import lk.oktocreative.weddingservice.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepo guestRepository;
    private final WeddingTableRepo tableRepository;

    @Override
    public CommonResponseDTO save(RequestGuestDTO dto) {

        String inviteCode = dto.getName()
                .toLowerCase()
                .replace(" ", "-")
                + "-"
                + UUID.randomUUID().toString().substring(0, 4);

        Guest guest = Guest.builder()
                .name(dto.getName())
                .plusOneCount(dto.getPlusOneCount())
                .inviteCode(inviteCode)
                .rsvpStatus(RsvpStatus.PENDING)
                .build();

        guestRepository.save(guest);

        return new CommonResponseDTO(
                201,
                "Guest saved successfully",
                guest,new ArrayList<>()
        );
    }

    @Override
    public CommonResponseDTO search(String name) {

        List<GuestResponseDTO> guests =
                guestRepository.findByNameContainingIgnoreCase(name)
                        .stream()
                        .map(this::mapGuest)
                        .toList();

        return new CommonResponseDTO(
                200,
                "Guests fetched successfully",
                guests,new ArrayList<>()
        );
    }

    @Override
    public CommonResponseDTO updateRsvp(UpdateGuestRsvpDTO dto) {

        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        guest.setRsvpStatus(RsvpStatus.valueOf(dto.getRsvpStatus()));
        guest.setRespondedAt(LocalDateTime.now());

        guestRepository.save(guest);

        return new CommonResponseDTO(
                200,
                "RSVP updated successfully",
                null,new ArrayList<>()
        );
    }

    @Override
    public CommonResponseDTO assignTable(AssignGuestTableDTO dto) {

        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        WeddingTable table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        int occupiedSeats = table.getGuests()
                .stream()
                .mapToInt(g -> 1 + g.getPlusOneCount())
                .sum();

        int requiredSeats = 1 + guest.getPlusOneCount();

        if (occupiedSeats + requiredSeats > table.getCapacity()) {
            throw new RuntimeException("Table capacity exceeded");
        }

        guest.setTable(table);

        guestRepository.save(guest);

        return new CommonResponseDTO(
                200,
                "Guest assigned successfully",
                null,new ArrayList<>()
        );
    }

    @Override
    public CommonResponseDTO getAllGuestsWithTables() {

        List<GuestResponseDTO> guests =
                guestRepository.findAll()
                        .stream()
                        .map(this::mapGuest)
                        .toList();

        return new CommonResponseDTO(
                200,
                "Guests fetched successfully",
                guests,new ArrayList<>()
        );
    }

    private GuestResponseDTO mapGuest(Guest guest) {

        return GuestResponseDTO.builder()
                .guestId(guest.getId())
                .name(guest.getName())
                .inviteCode(guest.getInviteCode())
                .plusOneCount(guest.getPlusOneCount())
                .rsvpStatus(guest.getRsvpStatus())
                .tableId(
                        guest.getTable() != null
                                ? guest.getTable().getId()
                                : null
                )
                .tableNumber(
                        guest.getTable() != null
                                ? guest.getTable().getTableNumber()
                                : null
                )
                .build();
    }
}

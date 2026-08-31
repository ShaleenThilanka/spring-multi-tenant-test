package lk.oktocreative.weddingservice.api;

import lk.oktocreative.weddingservice.dto.AssignGuestTableDTO;
import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.RequestGuestDTO;
import lk.oktocreative.weddingservice.dto.UpdateGuestRsvpDTO;
import lk.oktocreative.weddingservice.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
@CrossOrigin
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    public CommonResponseDTO saveGuest(
            @RequestBody RequestGuestDTO dto
    ) {
        return guestService.save(dto);
    }

    @GetMapping("/search")
    public CommonResponseDTO searchGuest(
            @RequestParam String name
    ) {
        return guestService.search(name);
    }

    @PutMapping("/rsvp")
    public CommonResponseDTO updateRsvp(
            @RequestBody UpdateGuestRsvpDTO dto
    ) {
        return guestService.updateRsvp(dto);
    }

    @PutMapping("/assign-table")
    public CommonResponseDTO assignTable(
            @RequestBody AssignGuestTableDTO dto
    ) {
        return guestService.assignTable(dto);
    }

    @GetMapping
    public CommonResponseDTO getAllGuests() {
        return guestService.getAllGuestsWithTables();
    }
}
package lk.oktocreative.weddingservice.api;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@CrossOrigin
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/guest")
    public CommonResponseDTO uploadGuestPhoto(
            @RequestParam Long guestId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws SQLException {
        List<MultipartFile> toUpload = new ArrayList<>();
        if (files != null) {
            toUpload.addAll(files);
        }
        if (file != null && !file.isEmpty()) {
            toUpload.add(file);
        }
        if (toUpload.isEmpty()) {
            throw new RuntimeException("No photo files were provided");
        }

        List<Long> ids = new ArrayList<>();
        for (MultipartFile photo : toUpload) {
            if (photo == null || photo.isEmpty()) continue;
            CommonResponseDTO saved = photoService.uploadGuestPhoto(guestId, photo);
            if (saved.getData() instanceof Long id) {
                ids.add(id);
            }
        }

        return new CommonResponseDTO(
                201,
                "Guest photo(s) uploaded!",
                ids,
                new ArrayList<>()
        );
    }

    @PostMapping("/preshoot")
    public CommonResponseDTO uploadPreshootPhotos(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws SQLException {
        List<MultipartFile> toUpload = new ArrayList<>();
        if (files != null) {
            toUpload.addAll(files);
        }
        if (file != null && !file.isEmpty()) {
            toUpload.add(file);
        }
        if (toUpload.isEmpty()) {
            throw new RuntimeException("No photo files were provided");
        }

        Long lastId = null;
        for (MultipartFile photo : toUpload) {
            if (photo == null || photo.isEmpty()) continue;
            CommonResponseDTO saved = photoService.uploadPreshootPhoto(photo);
            if (saved.getData() instanceof Long id) {
                lastId = id;
            }
        }

        return new CommonResponseDTO(
                201,
                "Pre-shoot photo(s) uploaded!",
                lastId,
                new ArrayList<>()
        );
    }

    @GetMapping("/preshoot")
    public CommonResponseDTO getPreshootPhotos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        if (page == null) {
            return new CommonResponseDTO(
                    200,
                    "Pre-shoot photos fetched successfully",
                    photoService.getPreshootPhotos(),
                    new ArrayList<>()
            );
        }
        return new CommonResponseDTO(
                200,
                "Pre-shoot photos fetched successfully",
                photoService.getPreshootPhotos(page, size == null ? 12 : size),
                new ArrayList<>()
        );
    }

    @GetMapping("/guest")
    public CommonResponseDTO getGuestPhotos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        if (page == null) {
            return new CommonResponseDTO(
                    200,
                    "Guest photos fetched successfully",
                    photoService.getGuestPhotos(),
                    new ArrayList<>()
            );
        }
        return new CommonResponseDTO(
                200,
                "Guest photos fetched successfully",
                photoService.getGuestPhotos(page, size == null ? 12 : size),
                new ArrayList<>()
        );
    }

    @DeleteMapping("/{photoId}")
    public CommonResponseDTO deletePhoto(@PathVariable Long photoId) throws SQLException {
        return photoService.deletePhoto(photoId);
    }
}

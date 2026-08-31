package lk.oktocreative.weddingservice.service.Impl;

import jakarta.transaction.Transactional;
import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.PhotoPageDTO;
import lk.oktocreative.weddingservice.dto.ResponsePhotoDTO;
import lk.oktocreative.weddingservice.entity.Guest;
import lk.oktocreative.weddingservice.entity.Photo;
import lk.oktocreative.weddingservice.enums.PhotoType;
import lk.oktocreative.weddingservice.repo.GuestRepo;
import lk.oktocreative.weddingservice.repo.PhotoRepo;
import lk.oktocreative.weddingservice.service.FileService;
import lk.oktocreative.weddingservice.service.PhotoService;
import lk.oktocreative.weddingservice.util.FileDataExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepo photoRepo;
    private final GuestRepo guestRepo;
    private final FileService fileService;
    private final FileDataExtractor fileDataExtractor;

    @Override
    public CommonResponseDTO uploadPreshootPhoto(MultipartFile photoFile) throws SQLException {

        String resource = null;

        try {

            resource = fileService.saveFile(
                    photoFile,
                    "shaleenkavindya.com/wedding/preshoot/"
            );

            Photo photo = Photo.builder()
                    .guest(null)
                    .type(PhotoType.PRESHOOT)
                    .filePath(resource)
                    .approved(true)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            photo = photoRepo.save(photo);

            return new CommonResponseDTO(
                    201,
                    "Pre-shoot photo uploaded!",
                    photo.getId(),
                    new ArrayList<>()
            );

        } catch (Exception e) {

            if (resource != null) {
                fileService.deleteFile(resource);
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CommonResponseDTO uploadGuestPhoto(
            Long guestId,
            MultipartFile photoFile
    ) throws SQLException {

        Guest guest = guestRepo.findById(guestId)
                .orElseThrow(() ->
                        new RuntimeException("Guest not found"));

        String resource = null;

        try {

            resource = fileService.saveFile(
                    photoFile,
                    "shaleenkavindya.com/wedding/guest/"
            );

            Photo photo = Photo.builder()
                    .guest(guest)
                    .type(PhotoType.GUEST_UPLOAD)
                    .filePath(resource)
                    .approved(true)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            photo = photoRepo.save(photo);

            return new CommonResponseDTO(
                    201,
                    "Guest photo uploaded!",
                    photo.getId(),
                    new ArrayList<>()
            );

        } catch (Exception e) {

            if (resource != null) {
                fileService.deleteFile(resource);
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ResponsePhotoDTO> getPreshootPhotos() {

        return photoRepo
                .findAllByTypeOrderByUploadedAtDesc(
                        PhotoType.PRESHOOT
                )
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<ResponsePhotoDTO> getGuestPhotos() {

        return photoRepo
                .findAllByTypeOrderByUploadedAtDesc(
                        PhotoType.GUEST_UPLOAD
                )
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public PhotoPageDTO getPreshootPhotos(int page, int size) {
        return pageByType(PhotoType.PRESHOOT, page, size);
    }

    @Override
    public PhotoPageDTO getGuestPhotos(int page, int size) {
        return pageByType(PhotoType.GUEST_UPLOAD, page, size);
    }

    private PhotoPageDTO pageByType(PhotoType type, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 12 : Math.min(size, 48);
        Page<Photo> result = photoRepo.findAllByTypeOrderByUploadedAtDesc(
                type,
                PageRequest.of(safePage, safeSize)
        );
        return PhotoPageDTO.builder()
                .items(result.getContent().stream().map(this::convert).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .total(result.getTotalElements())
                .hasMore(result.hasNext())
                .build();
    }

    @Override
    public CommonResponseDTO deletePhoto(Long photoId) throws SQLException {

        Photo photo = photoRepo.findById(photoId)
                .orElseThrow(() ->
                        new RuntimeException("Photo not found"));

        if (photo.getFilePath() != null) {
            fileService.deleteFile(photo.getFilePath());
        }

        photoRepo.delete(photo);

        return new CommonResponseDTO(
                200,
                "Photo deleted successfully!",
                photoId,
                new ArrayList<>()
        );
    }

    private ResponsePhotoDTO convert(Photo photo) {

        return ResponsePhotoDTO.builder()
                .id(photo.getId())
                .guestId(
                        photo.getGuest() != null
                                ? photo.getGuest().getId()
                                : null
                )
                .guestName(
                        photo.getGuest() != null
                                ? photo.getGuest().getName()
                                : null
                )
                .fileUrl(photo.getFilePath())
                .type(photo.getType())
                .approved(photo.getApproved())
                .uploadedAt(photo.getUploadedAt())
                .build();
    }
}

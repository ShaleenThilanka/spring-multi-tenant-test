package lk.oktocreative.weddingservice.service;

import lk.oktocreative.weddingservice.dto.CommonResponseDTO;
import lk.oktocreative.weddingservice.dto.PhotoPageDTO;
import lk.oktocreative.weddingservice.dto.ResponsePhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

public interface PhotoService {

    CommonResponseDTO uploadPreshootPhoto(MultipartFile photo) throws SQLException;

    CommonResponseDTO uploadGuestPhoto(Long guestId, MultipartFile photo) throws SQLException;

    List<ResponsePhotoDTO> getPreshootPhotos();

    List<ResponsePhotoDTO> getGuestPhotos();

    PhotoPageDTO getPreshootPhotos(int page, int size);

    PhotoPageDTO getGuestPhotos(int page, int size);

    CommonResponseDTO deletePhoto(Long photoId) throws SQLException;
}

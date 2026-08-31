package lk.oktocreative.weddingservice.repo;

import lk.oktocreative.weddingservice.entity.Photo;
import lk.oktocreative.weddingservice.enums.PhotoType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepo extends JpaRepository<Photo, Long> {

    List<Photo> findAllByTypeOrderByUploadedAtDesc(PhotoType type);

    Page<Photo> findAllByTypeOrderByUploadedAtDesc(PhotoType type, Pageable pageable);

}

package lk.oktocreative.weddingservice.repo;

import lk.oktocreative.weddingservice.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GuestRepo extends JpaRepository<Guest, Long> {
    List<Guest> findByNameContainingIgnoreCase(String name);

    Optional<Guest> findByInviteCode(String inviteCode);
}

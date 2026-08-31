package lk.oktocreative.weddingservice.repo;

import lk.oktocreative.weddingservice.entity.WeddingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingTableRepo extends JpaRepository<WeddingTable, Long> {
    boolean existsByTableNumber(Integer tableNumber);
}

package lk.oktocreative.weddingservice.repo;

import lk.oktocreative.weddingservice.entity.AgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepo extends JpaRepository<AgendaItem, Long> {
    List<AgendaItem> findAllByOrderBySortOrderAscIdAsc();
}

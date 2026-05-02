package ewm.main.event.repository;

import ewm.main.event.model.Event;
import ewm.main.event.model.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PublicEventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    @EntityGraph(attributePaths = {"initiator", "category"})
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    Optional<Event> findOneByIdAndState(Long id, EventStatus state);
}

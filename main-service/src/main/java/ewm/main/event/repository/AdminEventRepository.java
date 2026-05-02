package ewm.main.event.repository;

import ewm.main.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminEventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    @EntityGraph(attributePaths = {"initiator", "category"})
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);
}

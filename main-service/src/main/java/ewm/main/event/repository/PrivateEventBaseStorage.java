package ewm.main.event.repository;

import ewm.main.event.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrivateEventBaseStorage extends JpaRepository<Event, Integer> {
    String BASE_QUERY = """
            select e from Event e
            join fetch e.initiator i
            join fetch e.category c
            """;

    @Query(BASE_QUERY + """
            where e.initiator.id = :userId
            order by e.eventDate asc
            """)
    List<Event> findPrivateEventsByUserId(@Param("userId") long userId, Pageable pageable);

    Optional<Event> findOneByInitiator_IdAndId(long userId, long eventId);
}

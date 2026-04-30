package ewm.main.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateEventBaseStorage  extends JpaRepository<Event, Integer> {
}

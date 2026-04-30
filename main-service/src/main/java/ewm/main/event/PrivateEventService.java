package ewm.main.event;

import ewm.main.dto.EventFullDto;
import ewm.main.dto.NewEventDto;

public interface PrivateEventService {

    EventFullDto createEvent(long userId, NewEventDto dto);
}

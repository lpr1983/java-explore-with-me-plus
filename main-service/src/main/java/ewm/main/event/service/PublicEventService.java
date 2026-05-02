package ewm.main.event.service;

import ewm.main.dto.EventFullDto;
import ewm.main.dto.EventShortDto;
import ewm.main.event.model.search.PageParam;
import ewm.main.event.model.search.PublicEventSearchParam;

import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEvents(PublicEventSearchParam searchParam, PageParam pageParam);

    EventFullDto getEventById(Long id);
}

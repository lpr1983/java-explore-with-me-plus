package ewm.main.event.service;

import ewm.main.dto.UpdateEventAdminRequestDto;
import ewm.main.dto.EventFullDto;
import ewm.main.event.model.search.AdminEventSearchParam;
import ewm.main.event.model.search.PageParam;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> searchEvents(AdminEventSearchParam searchParam, PageParam pageParam);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequestDto request);
}

package ewm.main.event.controller;

import ewm.main.dto.EventFullDto;
import ewm.main.dto.EventShortDto;
import ewm.main.event.model.search.PageParam;
import ewm.main.event.model.search.PublicEventSearchParam;
import ewm.main.event.service.PublicEventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping("/test")
    String test() {
        return "main-service test: it works";
    }

    @GetMapping
    public List<EventShortDto> getEvents(@Valid PublicEventSearchParam searchParam,
                                         @Valid PageParam pageParam) {
        return publicEventService.getEvents(searchParam, pageParam);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id) {
        return publicEventService.getEventById(id);
    }
}

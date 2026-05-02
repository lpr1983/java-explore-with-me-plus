package ewm.main.event.controller;

import ewm.main.dto.UpdateEventAdminRequestDto;
import ewm.main.dto.EventFullDto;
import ewm.main.event.model.search.AdminEventSearchParam;
import ewm.main.event.model.search.PageParam;
import ewm.main.event.service.AdminEventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> searchEvents(
            @Valid AdminEventSearchParam searchParam,
            @Valid PageParam pageParam
    ) {
        return adminEventService.searchEvents(searchParam, pageParam);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable
            @Positive
            Long eventId,
            @RequestBody
            @Valid
            UpdateEventAdminRequestDto request
    ) {
        return adminEventService.updateEvent(eventId, request);
    }
}
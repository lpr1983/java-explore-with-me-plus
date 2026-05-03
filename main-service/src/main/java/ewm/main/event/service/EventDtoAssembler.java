package ewm.main.event.service;

import ewm.main.dto.EventFullDto;
import ewm.main.dto.EventShortDto;
import ewm.main.event.model.Event;
import ewm.main.stat.StatService;
import ewm.stat.client.model.GetStatsParams;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class EventDtoAssembler {

    private static final boolean UNIQUE_VIEWS = false;

    private final StatService statService;

    public EventDtoAssembler(StatService statService) {
        this.statService = statService;
    }

    public void fillShortDto(Event event, EventShortDto dto) {
        if (event.getPublishedOn() == null) {
            dto.setViews(null);
            return;
        }

        GetStatsParams params = GetStatsParams.builder()
                .start(event.getPublishedOn())
                .end(LocalDateTime.now())
                .uris(List.of(getEventUri(event)))
                .unique(UNIQUE_VIEWS)
                .build();

        Map<String, Long> viewsByUri = statService.getViews(params);

        if (viewsByUri == null) {
            dto.setViews(null);
            return;
        }

        dto.setViews(viewsByUri.getOrDefault(getEventUri(event), 0L));
    }

    public void fillFullDto(Event event, EventFullDto dto) {
        if (event.getPublishedOn() == null) {
            dto.setViews(null);
            return;
        }

        GetStatsParams params = GetStatsParams.builder()
                .start(event.getPublishedOn())
                .end(LocalDateTime.now())
                .uris(List.of(getEventUri(event)))
                .unique(UNIQUE_VIEWS)
                .build();

        Map<String, Long> viewsByUri = statService.getViews(params);

        if (viewsByUri == null) {
            dto.setViews(null);
            return;
        }

        dto.setViews(viewsByUri.getOrDefault(getEventUri(event), 0L));
    }

    public void fillShortDtoList(List<Event> events, List<EventShortDto> dtos) {
        Map<Long, Long> viewsByEventId = getViewsByEventId(events);

        // Не удалось запросить статистику (ошибка вызова сервиса)
        if (viewsByEventId == null) {
            for (EventShortDto dto : dtos) {
                dto.setViews(null);
            }
            return;
        }

        for (EventShortDto dto : dtos) {
            dto.setViews(viewsByEventId.get(dto.getId()));
        }
    }

    public void fillFullDtoList(List<Event> events, List<EventFullDto> dtos) {
        Map<Long, Long> viewsByEventId = getViewsByEventId(events);

        if (viewsByEventId == null) {
            for (EventFullDto dto : dtos) {
                dto.setViews(null);
            }
            return;
        }

        for (EventFullDto dto : dtos) {
            dto.setViews(viewsByEventId.get(dto.getId()));
        }
    }

    private Map<Long, Long> getViewsByEventId(List<Event> events) {
        List<Event> eventsWithPublishedOn = getEventsWithPublishedOn(events);

        if (eventsWithPublishedOn.isEmpty()) {
            return Map.of();
        }

        GetStatsParams params = GetStatsParams.builder()
                .start(getMinPublishedOn(eventsWithPublishedOn))
                .end(LocalDateTime.now())
                .uris(getEventUris(eventsWithPublishedOn))
                .unique(UNIQUE_VIEWS)
                .build();

        Map<String, Long> viewsByUri = statService.getViews(params);

        if (viewsByUri == null) {
            return null;
        }

        Map<Long, Long> viewsByEventId = new HashMap<>();

        for (Event event : eventsWithPublishedOn) {
            String uri = getEventUri(event);
            Long views = viewsByUri.getOrDefault(uri, 0L);

            viewsByEventId.put(event.getId(), views);
        }

        return viewsByEventId;
    }

    private List<Event> getEventsWithPublishedOn(List<Event> events) {
        List<Event> result = new ArrayList<>();

        for (Event event : events) {
            if (event.getPublishedOn() != null) {
                result.add(event);
            }
        }

        return result;
    }

    private List<String> getEventUris(List<Event> events) {
        Set<String> uniqueUris = new HashSet<>();

        for (Event event : events) {
            uniqueUris.add(getEventUri(event));
        }

        return new ArrayList<>(uniqueUris);
    }

    private LocalDateTime getMinPublishedOn(List<Event> events) {
        LocalDateTime minPublishedOn = events.get(0).getPublishedOn();

        for (Event event : events) {
            if (event.getPublishedOn().isBefore(minPublishedOn)) {
                minPublishedOn = event.getPublishedOn();
            }
        }

        return minPublishedOn;
    }

    private String getEventUri(Event event) {
        return "/events/" + event.getId();
    }
}
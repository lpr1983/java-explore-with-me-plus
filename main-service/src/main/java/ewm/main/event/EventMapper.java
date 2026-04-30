package ewm.main.event;

import ewm.main.category.Category;
import ewm.main.category.mapper.CategoryMapper;
import ewm.main.dto.EventFullDto;
import ewm.main.dto.LocationDto;
import ewm.main.dto.NewEventDto;
import ewm.main.user.User;
import ewm.main.user.UserMapper;

public class EventMapper {
    public static Event toEntity(NewEventDto dto, Category category, User initiator) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setCategory(category);
        event.setEventDate(dto.getEventDate());
        event.setLocation(toLocation(dto.getLocation()));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setInitiator(initiator);

        return event;
    }

    private static Location toLocation(LocationDto locationDto) {
        return locationDto != null
                ? new Location(locationDto.getLat(), locationDto.getLon())
                : null;
    }

    public static LocationDto toLocationDto(Location location) {
        return location != null ?
                new LocationDto(location.getLat(), location.getLon())
                : null;
    }

    public static EventFullDto toFullDto(Event event, long views, long confirmedRequests) {
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setConfirmedRequests(confirmedRequests);
        dto.setCreatedOn(event.getCreatedOn());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setId(event.getId());
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator())); // обязательное поле
        dto.setLocation(toLocationDto(event.getLocation()));
        dto.setPaid(event.isPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.isRequestModeration());
        dto.setState(event.getState().name()); // обязательное поле
        dto.setTitle(event.getTitle());
        dto.setViews(views);

        return dto;
    }
}

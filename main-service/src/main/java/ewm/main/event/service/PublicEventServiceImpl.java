package ewm.main.event.service;

import ewm.main.dto.EventFullDto;
import ewm.main.dto.EventShortDto;
import ewm.main.event.mapper.EventMapper;
import ewm.main.event.model.Event;
import ewm.main.event.model.search.EventSort;
import ewm.main.event.model.EventStatus;
import ewm.main.event.model.search.PageParam;
import ewm.main.event.model.search.PublicEventSearchParam;
import ewm.main.event.repository.EventSpecifications;
import ewm.main.event.repository.PublicEventRepository;
import ewm.main.exception.NotFoundException;
import ewm.main.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {

    private final PublicEventRepository publicEventRepository;

    public PublicEventServiceImpl(PublicEventRepository publicEventRepository) {
        this.publicEventRepository = publicEventRepository;
    }

    @Override
    public List<EventShortDto> getEvents(PublicEventSearchParam searchParam, PageParam pageParam) {
        log.info("Поиск событий с параметрами: {}, {}", searchParam, pageParam);

        LocalDateTime rangeStart = searchParam.getRangeStart();
        LocalDateTime rangeEnd = searchParam.getRangeEnd();

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("rangeStart должен быть раньше rangeEnd");
        }

        Specification<Event> specification = Specification
                .where(EventSpecifications.stateEqual(EventStatus.PUBLISHED))
                .and(EventSpecifications.searchByTextInAnnotationAndDescription(searchParam.getText()));

        if (rangeStart == null && rangeEnd == null) {
            specification = specification.and(EventSpecifications.eventDateAfter(LocalDateTime.now()));
        } else {
            specification = specification
                    .and(EventSpecifications.eventDateAfter(rangeStart))
                    .and(EventSpecifications.eventDateBefore(rangeEnd));
        }

        specification = specification
                .and(EventSpecifications.paid(searchParam.getPaid()))
                .and(EventSpecifications.categoryIdIn(searchParam.getCategories()));

        EventSort eventSort = EventSort.parse(searchParam.getSort());

        Sort sort = eventSort == EventSort.EVENT_DATE
                ? Sort.by(Sort.Direction.ASC, "eventDate")
                : Sort.unsorted();

        Pageable pageable = PageRequest.of(
                pageParam.getFrom() / pageParam.getSize(),
                pageParam.getSize(),
                sort
        );

        List<Event> events = publicEventRepository.findAll(specification, pageable).getContent();

        log.info("Найдено {} событий, соответствующих критериям.", events.size());

        return events.stream()
                .map(event -> EventMapper.toShortDto(event, 0, 0))
                .toList();
    }

    @Override
    public EventFullDto getEventById(Long id) {

        Event event = publicEventRepository.findOneByIdAndState(id, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + id + " не найдено или недоступно"));

        return EventMapper.toFullDto(event, 0, 0);
    }
}

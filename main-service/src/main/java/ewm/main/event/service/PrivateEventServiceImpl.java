package ewm.main.event.service;

import ewm.main.dto.EventShortDto;
import ewm.main.dto.UpdateEventUserRequestDto;
import ewm.main.event.mapper.EventMapper;
import ewm.main.event.model.Event;
import ewm.main.event.model.EventStatus;
import ewm.main.event.repository.PrivateEventRepository;
import ewm.main.exception.ConflictException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import ewm.main.category.Category;
import ewm.main.category.repository.CategoryRepository;
import ewm.main.dto.EventFullDto;
import ewm.main.dto.NewEventDto;
import ewm.main.exception.NotFoundException;
import ewm.main.user.User;
import ewm.main.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserRepository userRepository;
    private final PrivateEventRepository privateEventRepository;
    private final CategoryRepository categoryRepository;

    public PrivateEventServiceImpl(UserRepository userRepository,
                                   PrivateEventRepository privateEventRepository,
                                   CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.privateEventRepository = privateEventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EventFullDto getEventOfUserById(long userId, long eventId) {
        log.info("Получение события для userId: {}, eventId: {}", userId, eventId);

        Event event = findEventByUserIdAndEventIdOrThrow(userId, eventId);

        log.info("Событие успешно получено, eventId: {}", eventId);
        return EventMapper.toFullDto(event, 0, 0);
    }

    @Override
    public List<EventShortDto> getAllByUserId(long userId, int from, int size) {
        log.info("Получение событий для userId: {}, с: {}, размер: {}", userId, from, size);

        Pageable pageable = PageRequest.of(from / size, size);

        List<EventShortDto> events = privateEventRepository.findPrivateEventsByUserId(userId, pageable).stream()
                .map(e -> EventMapper.toShortDto(e, 0, 0))
                .toList();

        log.info("Количество событий: {}", events.size());
        return events;
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto dto) {
        log.info("Создание события для userId: {}, детали события: {}", userId, dto);

        validateEventDate(dto.getEventDate());

        User user = findUserByIdOrThrow(userId);

        Category category = findCategoryByIdOrThrow(dto.getCategory());

        Event event = EventMapper.toEntity(dto, category, user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventStatus.PENDING);
        Event savedEvent = privateEventRepository.save(event);

        log.info("Событие успешно создано с id: {}", savedEvent.getId());
        return EventMapper.toFullDto(savedEvent, 0, 0);
    }

    @Override
    public EventFullDto updateEventOfUser(long userId, long eventId, UpdateEventUserRequestDto dto) {
        log.info("Обновление события для userId: {}, eventId: {}, детали обновления: {}", userId, eventId, dto);

        Event event = findEventByUserIdAndEventIdOrThrow(userId, eventId);

        if (event.getState() != EventStatus.PENDING && event.getState() != EventStatus.CANCELED) {
            throw new ConflictException("Можно изменять события только в статусах PENDING и CANCELED");
        }

        validateEventDate(event.getEventDate());

        Category category = dto.getCategory() != null ? findCategoryByIdOrThrow(dto.getCategory()) : null;

        EventMapper.updateEntity(event, dto, category);

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case "SEND_TO_REVIEW" -> event.setState(EventStatus.PENDING);
                case "CANCEL_REVIEW" -> event.setState(EventStatus.CANCELED);
                default -> throw new ValidationException("Недопустимое действие: " + dto.getStateAction());
            }
        }

        Event updatedEvent = privateEventRepository.save(event);

        log.info("Событие успешно обновлено с id: {}", updatedEvent.getId());
        return EventMapper.toFullDto(updatedEvent, 0, 0);
    }

    private User findUserByIdOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
    }

    private Category findCategoryByIdOrThrow(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Не найдена категория с id: " + categoryId));
    }

    private Event findEventByUserIdAndEventIdOrThrow(long userId, long eventId) {
        return privateEventRepository.findOneByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("У пользователя с id: %d нет события с id: %d", userId, eventId)));
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата события должна быть не ранее чем через 2 часа от текущего момента");
        }
    }

}

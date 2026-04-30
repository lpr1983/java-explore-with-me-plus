package ewm.main.event;

import lombok.extern.slf4j.Slf4j;

import ewm.main.category.Category;
import ewm.main.category.repository.CategoryRepository;
import ewm.main.dto.EventFullDto;
import ewm.main.dto.NewEventDto;
import ewm.main.exception.NotFoundException;
import ewm.main.user.User;
import ewm.main.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserRepository userRepository;
    private final PrivateEventBaseStorage privateEventBaseStorage;
    private final CategoryRepository categoryRepository;

    public PrivateEventServiceImpl(UserRepository userRepository,
                                   PrivateEventBaseStorage privateEventBaseStorage,
                                   CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.privateEventBaseStorage = privateEventBaseStorage;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto dto) {
        log.info("Creating event for userId: {}, event details: {}", userId, dto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Не найдена категория с id: " + dto.getCategory()));

        Event event = EventMapper.toEntity(dto, category, user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventStatus.PENDING);
        Event savedEvent = privateEventBaseStorage.save(event);

        log.info("Event created successfully with id: {}", savedEvent.getId());
        return EventMapper.toFullDto(savedEvent, 0, 0);
    }
}

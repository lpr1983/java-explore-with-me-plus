package ewm.main.compilation.service;

import ewm.main.compilation.mapper.CompilationMapper;
import ewm.main.compilation.model.Compilation;
import ewm.main.compilation.repository.CompilationRepository;
import ewm.main.dto.CompilationDto;
import ewm.main.dto.NewCompilationDto;
import ewm.main.dto.UpdateCompilationRequestDto;
import ewm.main.event.model.Event;
import ewm.main.event.repository.EventRepository;
import ewm.main.exception.ConflictException;
import ewm.main.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto add(NewCompilationDto dto) {
        if (isTitleTaken(dto.getTitle())) {
            throw new ConflictException("Compilation title already taken");
        }
        List<Event> events = List.of();
        if (!Objects.isNull(dto.getEvents()) && !dto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(dto.getEvents());
        }

        Compilation newCompilation = compilationRepository.save(CompilationMapper.toEntity(dto, events));
        return CompilationMapper.toDto(newCompilation);
    }

    @Transactional
    public void delete(Long id) {
        Compilation existing = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no compilations found"));

        compilationRepository.delete(existing);
    }

    @Transactional
    public CompilationDto update(Long id, UpdateCompilationRequestDto dto) {
        Compilation existing = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no compilations found"));

        if (isTitleTaken(dto.getTitle(), id)) {
            throw new ConflictException("Compilation title already taken");
        }

        if (!Objects.isNull(dto.getTitle())) {
            existing.setTitle(dto.getTitle());
        }

        if (!Objects.isNull(dto.getPinned())) {
            existing.setPinned(dto.getPinned());
        }

        if (!Objects.isNull(dto.getEvents())) {
            List<Event> events = eventRepository.findAllById(dto.getEvents());
            existing.setEvents(events);
        }
        return CompilationMapper.toDto(existing);
    }

    @Transactional
    public List<CompilationDto> get() {
        return compilationRepository.findAll().stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CompilationDto> get(Integer from, Integer size, Boolean pinned) {
        List<Compilation> list;
        if (Objects.isNull(pinned)) {
            list = compilationRepository.findAllWithLimitOffset(from, size);
        } else {
            list = compilationRepository.findAllWithLimitOffset(from, size, pinned);
        }

        return list.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public CompilationDto get(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found"));
        return CompilationMapper.toDto(compilation);
    }

    private Boolean isTitleTaken(String title) {
        return compilationRepository.countByTitle(title) > 0;
    }

    private boolean isTitleTaken(String title, Long id) {
        return compilationRepository.countByTitleExcludingId(id, title) > 0;
    }
}

package ewm.main.compilation.service;

import ewm.main.compilation.mapper.CompilationMapper;
import ewm.main.compilation.model.Compilation;
import ewm.main.compilation.repository.CompilationRepository;
import ewm.main.dto.CompilationDto;
import ewm.main.dto.NewCompilationDto;
import ewm.main.event.model.Event;
import ewm.main.event.repository.PublicEventRepository;
import ewm.main.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final PublicEventRepository eventRepository;

    public CompilationDto add(NewCompilationDto dto) {
        // 409 Integrity constraint has been violated.
        List<Event> events = eventRepository.findAllById(dto.getEvents());
        Compilation newCompilation = compilationRepository.save(CompilationMapper.toEntity(dto, events));
        return CompilationMapper.toDto(newCompilation);
    }

    public void delete(Long id) {
        throw new NotFoundException("not done");
    }

    public CompilationDto update(Long id, NewCompilationDto dto) {
        throw new NotFoundException("not done");
    }

    public List<CompilationDto> get() {
        return compilationRepository.findAll().stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    public CompilationDto get(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found"));
        return CompilationMapper.toDto(compilation);
    }
}

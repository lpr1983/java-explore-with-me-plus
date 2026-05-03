package ewm.main.compilation.controller;

import ewm.main.compilation.service.CompilationService;
import ewm.main.dto.CompilationDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "compilations")
@AllArgsConstructor
public class PublicCompilationController {
    private final CompilationService service;

    @GetMapping()
    public List<CompilationDto> get() {
        // TODO: filter
        return service.get();
    }

    @GetMapping("{id}")
    public CompilationDto get(@PathVariable long id) {
        return service.get(id);
    }
}

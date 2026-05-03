package ewm.main.compilation.controller;

import ewm.main.compilation.service.CompilationService;
import ewm.main.dto.CompilationDto;
import ewm.main.dto.NewCompilationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/compilations")
@AllArgsConstructor
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@RequestBody NewCompilationDto dto) {
        return service.add(dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto update(@PathVariable Long id, @RequestBody NewCompilationDto dto) {
        return service.update(id, dto);
    }
}

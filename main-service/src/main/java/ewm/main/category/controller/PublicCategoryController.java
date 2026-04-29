package ewm.main.category.controller;

import ewm.main.category.service.CategoryService;
import ewm.main.dto.CategoryDto;
import ewm.main.exception.ConflictException;
import ewm.main.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("categories")
@AllArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAll(@RequestParam(required = false,defaultValue = "0") Integer from,@RequestParam(required = false,defaultValue = "10") Integer size) {
        return categoryService.getAll(from,size);
    }

    @GetMapping("{catId}")
    public CategoryDto getById(@PathVariable("catId") Long categoryId) {
        return categoryService.getById(categoryId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException e) {
        return Map.of(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(final ConflictException e) {
        return Map.of(HttpStatus.CONFLICT.toString(), e.getMessage());
    }
}

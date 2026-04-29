package ewm.main.category.controller;

import ewm.main.category.service.CategoryService;
import ewm.main.dto.CategoryDto;
import ewm.main.dto.NewCategoryDto;
import ewm.main.exception.ConflictException;
import ewm.main.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@RequestBody NewCategoryDto dto) {
        return categoryService.add(dto);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("catId") Long categoryId) {
        categoryService.deleteOne(categoryId);
    }

    @PatchMapping("{catId}")
    public CategoryDto patchById(@PathVariable("catId") Long categoryId,@RequestBody NewCategoryDto dto) {
        return categoryService.patchById(categoryId,dto);
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

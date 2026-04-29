package ewm.main.user;

import ewm.main.exception.ConflictException;
import ewm.main.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        log.info("UserDto = {} in controller", user);
        return userService.createUser(user);
    }

    /**
     * Получение информации о пользователях
     *
     * @param ids  массив ID пользователей (опционально)
     * @param from количество элементов, которые нужно пропустить (по умолчанию 0)
     * @param size количество элементов в наборе (по умолчанию 10)
     * @return список пользователей, соответствующий фильтрам
     */
    @GetMapping
    public List<User> getUsers(
            @RequestParam(required = false) List<Long> ids, // почему в задании Integer?
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        // Если передан массив ID — ищем только этих пользователей
        if (ids != null && !ids.isEmpty()) {
            return userService.findUsersByIds(ids);
        }

        // Иначе — возвращаем всех пользователей с учётом параметров пагинации
        return userService.findAllUsers(from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundIdException(NotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleGeneralException(ConflictException e) {
        return Map.of("Duplicate email", e.getMessage());
    }
}

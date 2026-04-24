package ewm.stat.server.controller;

import ewm.stat.dto.HitDto;
import ewm.stat.dto.StatDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatController {

    @GetMapping("/test")
    public String test() {
        return "stats: it works";
    }

    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody HitDto hit) {
        // Будет вызов сервиса/хранилища.
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam String start,
                                  @RequestParam String end,
                                  @RequestParam(required = false, defaultValue = "false") boolean unique,
                                  @RequestParam(required = false) String[] uris) {

        return List.of();
    }
}

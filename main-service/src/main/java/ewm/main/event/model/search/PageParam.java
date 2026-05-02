package ewm.main.event.model.search;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PageParam {
    @PositiveOrZero
    private Integer from = 0;

    @PositiveOrZero
    private Integer size = 10;
}

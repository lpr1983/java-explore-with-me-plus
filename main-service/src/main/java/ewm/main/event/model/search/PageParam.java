package ewm.main.event.model.search;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageParam {
    @PositiveOrZero
    private Integer from = 0;

    @Positive
    private Integer size = 10;
}

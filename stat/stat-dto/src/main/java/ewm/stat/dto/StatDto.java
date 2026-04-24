package ewm.stat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatDto {
    String app;
    String uri;
    int hits;
}

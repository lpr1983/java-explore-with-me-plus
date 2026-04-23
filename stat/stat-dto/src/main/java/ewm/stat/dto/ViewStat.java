package ewm.stat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewStat {
    String app;
    String uri;
    int hits;
}

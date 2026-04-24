package ewm.stat.client.mapper;

import ewm.stat.client.model.HitParams;
import ewm.stat.dto.HitDto;

import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static HitDto fromHitParams(HitParams params, String app) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return HitDto.builder()
                .uri(params.getUri())
                .timestamp(params.getTimestamp().format(formatter))
                .ip(params.getIp())
                .app(app)
                .build();
    }
}

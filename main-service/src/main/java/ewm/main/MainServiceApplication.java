package ewm.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@ComponentScan(value = {"ewm.main", "ewm.stat.client"})
public class MainServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApplication.class, args);

        // Демонстрация работы StatClient (будет убрана на 2-м этапе)
        /* StatClient statClient = context.getBean(StatClient.class);
        try {
            HitParams params = HitParams.builder()
                    .uri("/event/1")
                    .ip("192.168.1.1")
                    .timestamp(LocalDateTime.now())
                    .build();

            statClient.saveHit(params);
            log.info("saveHit");
        } catch (StatClientException e) {
            log.error("Ошибка работы statClient.saveHit: {}", e.getMessage());
        }

        try {
            GetStatsParams params = GetStatsParams.builder()
                    .start(LocalDateTime.now().minusHours(1))
                    .end(LocalDateTime.now().plusHours(1))
                    .uris(List.of("/event/1", "/event/2"))
                    .unique(false)
                    .build();

            List<StatDto> statResult = statClient.getStats(params);
            log.info("getStats, result: {}", statResult);
        } catch (StatClientException e) {
            log.error("Ошибка работы statClient.getStats: {}", e.getMessage());
        } */
    }

}

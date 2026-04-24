package ewm;

import ewm.stat.client.StatClient;
import ewm.stat.client.model.GetStatsParams;
import ewm.stat.client.model.HitParams;
import ewm.stat.dto.StatDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MainServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApplication.class, args);

        // Демонстрация работы клиента стастистики
        StatClient statClient = context.getBean(StatClient.class);

        try {
            HitParams params = HitParams.builder()
                    .uri("/event/1")
                    .ip("192.168.1.1")
                    .timestamp(LocalDateTime.now()).build();

            statClient.saveHit(params);
        } catch (Exception e) {
            System.out.println("Ошибка работы statClient.saveHit: " + e.getMessage());
        }

        try {
            GetStatsParams params = GetStatsParams.builder()
                    .start(LocalDateTime.now().minusHours(1))
                    .end(LocalDateTime.now().plusHours(1))
                    .uris(List.of("/event/1", "/event/2"))
                    .unique(false)
                    .build();

            List<StatDto> statResult = statClient.getStats(params);
            System.out.println(statResult);
        } catch (Exception e) {
            System.out.println("Ошибка работы statClient.getStats: " + e.getMessage());
        }
        //

    }

}

# Explore With Me — Stats Service

Сервис статистики для проекта Explore With Me.  
Реализует сбор и получение информации о посещениях эндпоинтов.

---

Реализован **этап 1 — сервис статистики**:

- REST API статистики
- HTTP-клиент
- Docker-сборка
- Подготовлена структура проекта для дальнейшего развития

## Структура проекта

Многомодульный Maven-проект:

```
.
├── stat-service
│   ├── stat-server   # HTTP-сервис статистики
│   ├── stat-client   # HTTP-клиент для работы со stats-service
│   └── stat-dto      # общие DTO для сервера и клиента
├── main-service      # основной сервис (заготовка)
└── docker-compose.yml
```

### Модули

- **stat-server**  
  REST API для сохранения и получения статистики

- **stat-client**  
  HTTP-клиент для взаимодействия с сервисом статистики

- **stat-dto**  
  Общие классы запросов/ответов (`HitDto`, `StatDto`)

- **main-service**  
  Заготовка основного сервиса

---

## API сервиса статистики

Реализовано в соответствии со спецификацией `ewm-stats-service.json`.

### Сохранение события

```
POST /hit
```

Сохраняет информацию о посещении.

Пример запроса:

```json
{
  "app": "ewm-main-service",
  "uri": "/events/1",
  "ip": "192.168.0.1",
  "timestamp": "2022-09-06 11:00:23"
}
```

Ответ: `201 Created`

---

### Получение статистики

```
GET /stats
```

Параметры:

- `start` — начало диапазона (yyyy-MM-dd HH:mm:ss)
- `end` — конец диапазона
- `uris` — список URI (опционально)
- `unique` — учитывать только уникальные IP (опционально)

Пример:

```
/stats?start=2022-09-01 00:00:00&end=2022-09-30 23:59:59&uris=/events/1&unique=true
```

Ответ:

```json
[
  {
    "app": "ewm-main-service",
    "uri": "/events/1",
    "hits": 6
  }
]
```

---

## Пример использования stat-client

```java
try {
HitParams params = HitParams.builder()
        .uri("/event/1")
        .ip("192.168.1.1")
        .timestamp(LocalDateTime.now())
        .build();

    statClient.saveHit(params);
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
} catch (StatClientException e) {
    log.error("Ошибка работы statClient.getStats: {}", e.getMessage());
}
```
## Запуск через Docker

```bash
docker-compose up
```

Поднимаются:

- `stats-server` — порт 9090
- `stats-db` (PostgreSQL)
- `ewm-service` — порт 8080 (заготовка основного сервиса)
- `ewm-db` (PostgreSQL)

## Особенности реализации

- Многомодульная структура (server + client + dto)
- Общие DTO используются и сервером, и клиентом
- Поддержка фильтрации по URI
- Поддержка уникальных просмотров (по IP)
- Сортировка статистики по количеству просмотров

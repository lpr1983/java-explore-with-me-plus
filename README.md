# Explore With Me

## Структура проекта

Многомодульный Maven-проект:

```
.
├── stat-service
│   ├── stat-server   # HTTP-сервис статистики
│   ├── stat-client   # HTTP-клиент для работы со stats-service
│   └── stat-dto      # общие DTO для сервера и клиента
├── main-service      # основной сервис
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
  Основной сервис


## Запуск через Docker

```bash
docker-compose up
```

Поднимаются:

- `stats-server` — порт 9090
- `stats-db` (PostgreSQL)
- `ewm-service` — порт 8080 (заготовка основного сервиса)
- `ewm-db` (PostgreSQL)

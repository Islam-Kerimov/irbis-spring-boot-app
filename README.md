# irbis-spring-boot-app

Запустить проект с помощью docker compose:
1. ./mvnw clean package -DskipTests
2. docker compose up

Есть 2 пользователя с правами доступа, после аутенфикации.
- user имеет права доступа только к GET методам
- admin имеет права доступа ко всем методам проекта (GET, POST, PUT, DELETE)

Чтобы получить токен необходимо:
Отравить POST запрос по адресу "http://localhost:8080/api/v1/auth/authenticate" с телом
{
  "username": "admin",
  "password": "1234"
}

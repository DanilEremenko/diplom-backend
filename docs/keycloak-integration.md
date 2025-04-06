# Разграничение ответственности по процессам регистрации, аутентификации и авторизации

## 1. Регистрация первого пользователя
- На стороне Spring-приложения выполняется проверка отсутствия указанной компании в базе данных.
- При успешном прохождении процедуры генерируется ссылка для подтверждения почты пользователя, запросившего регистрацию.
- В случае подтверждения почты, через Keycloak Admin API добавляется пользователь с указанными учетными данными для последующей аутентификации на стороне Keycloak.

## 2. Добавление нового пользователя
- Генерируется ссылка на указанный email для установки пароля.
- После успешной установки пароля пользователь сохраняется в базе данных для сопоставления с компанией.
- Учетные данные отправляются в Keycloak через Keycloak Admin API для последующей аутентификации на стороне Keycloak.

## 3. Роли и доступы
- Ролевая модель реализуется на стороне Spring-приложения.

## 4. Сброс паролей и восстановление доступа
- Функционал сброса пароля и восстановления доступа реализуется на стороне Keycloak.

## 5. Конфигурация сессий
- На стороне Keycloak настраиваются токены доступа и рефреш-токены, а также реализуется функционал выхода из системы.

---

# Конфигурация Keycloak в Docker Compose

```yaml
name: be-better
services:
  keycloak:
    image: keycloak/keycloak:26.0.4
    environment:
      KEYCLOAK_ADMIN: ${KEYCLOAK_ADMIN}
      KEYCLOAK_ADMIN_PASSWORD: ${KEYCLOAK_ADMIN_PASSWORD}
    volumes:
      - "${KEYCLOAK_IMPORT_PATH}:/opt/keycloak/data/import"
    ports:
      - "${KEYCLOAK_PORT_EXTERNAL}:${KEYCLOAK_PORT_INTERNAL}"
    command: ${KEYCLOAK_PROFILE} --import-realm
    networks:
      - app-network
```

# Переменные окружения

```dotenv
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_PROFILE=start-dev
KEYCLOAK_PORT_EXTERNAL=8082
KEYCLOAK_PORT_INTERNAL=8080
KEYCLOAK_IMPORT_PATH=./config/keycloak/import
```
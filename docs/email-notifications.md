# Техническое решение реализации почтовых уведомлений с шаблонами

Для почтовых уведомлений требуется использовать шаблоны сообщений и брокер сообщений.

## 1. Триггеры событий:
- При срабатывании триггера (пройден курс, защищен грейд, подтвержден навык, отправлен на пересдачу) создается задача на отправку уведомления через брокер сообщений.
- Отправка сообщений для подтверждения регистрации, сброс пароля и другие уведомления, направляемые на почту пользователя.

## 2. Брокер сообщений:
- Используется для асинхронной передачи событий на отправку уведомления, например, RabbitMQ.
- Использовать очередь email-notifications.

## 3. Email-сервис:
- Подписан на очередь сообщений и отвечает за отправку писем. Сервис получает событие, выбирает нужный шаблон и генерирует письмо.

## 4. Шаблоны писем:
- Для писем используются шаблонизаторы, например, VelocityEngine.

## 5. SMTP-сервер:
- Email-сервис использует мок SMTP-сервер на этапе разработки.

---

# Пример реализации шаблона:

```vm
<p>Для восстановления/установки пароля, пожалуйста, перейдите по ссылке:</p>
<a href="$link">Сбросить/установить пароль</a>
<p>Если вы не запрашивали сброс пароля, проигнорируйте это письмо.</p>
```

# Конфигурация MailHog и RabbitMQ в Docker Compose

```yaml
name: be-better
services:
  mailhog:
    image: mailhog/mailhog:v1.0.1
    ports:
      - "${SMTP_PORT}:${SMTP_PORT}"
      - "${SMTP_UI_PORT}:${SMTP_UI_PORT}"
    networks:
      - app-network

  rabbitmq:
    image: rabbitmq:4.0.3-management-alpine
    ports:
      - "${BROKER_PORT}:${BROKER_PORT}"
      - "${BROKER_UI_PORT}:${BROKER_UI_PORT}"
    environment:
      RABBITMQ_DEFAULT_USER: ${RABBITMQ_USER}
      RABBITMQ_DEFAULT_PASS: ${RABBITMQ_PASSWORD}
    networks:
      - app-network
```

# Переменные окружения

```dotenv
# SMTP
SMTP_HOST=localhost
SMTP_PORT=1025
SMTP_UI_PORT=8025
SMTP_USER=user123
SMTP_PASSWORD=admin
MAIL_SMTP_AUTH=false
MAIL_SMTP_STARTTLS_ENABLE=false

# RabbitMQ
BROKER_PORT=5672
BROKER_UI_PORT=15672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
```

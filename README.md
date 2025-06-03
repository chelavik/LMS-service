# LMS-система

## Постановка задачи

Проект представляет собой REST API для LMS (Learning Management System) — системы управления обучением. В системе предусмотрены роли пользователей (гости, студенты, преподаватели), которые могут взаимодействовать с курсами, темами и задачами.


### Авторизация

Авторизация со стороны клиента должна происходить следующим образом: в заголовки запроса добавляется `Authorization` со значением `Basic Логин:Пароль`, где пара `Логин:Пароль` кодирована в `Base64`.

Пример:

```
login = den23421
password = test_pa$$word

// Хидер передаётся с HTTP-запросом
Authorization: ZGVuMjM0MjE6dGVzdF9wYSQkd29yZA==
```

В случае ошибки отдается семантически корректный HTTP статус-код, а также JSON вида:

```json
{
    "errorCode": "error.code", 
    "errorMessage": "Описание ошибки"
}
```

### Проверка прав доступа

Во все контроллеры добавлена проверка прав доступа к эндпоинтам. Из хедера запроса, с помощью авторизационного сервиса определяется текущая роль пользователя и производятся необходимые проверки.

```text

Доступ только преподавателям и администраторам
- POST /students
- PATCH /students
- GET /students/{id} 
- DELETE /students/{id}
- POST /courses
- DELETE /courses/{id}
- POST /courses/{courseId}/enroll/{studentId}
- POST /courses/{courseId}/unenroll/{studentId}
- POST /courses/{id}/topics
- DELETE /topics/{id}
- POST /topics/{id}/problems
- DELETE /problems/{id}

Доступ всем пользователям (студентам, преподавателям и администраторам)
- GET /courses/{id}
- GET /courses/{id}
- GET /topics/{id}
- GET /problems/{id}
```

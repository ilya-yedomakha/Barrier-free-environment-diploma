API Gateway ендпоінти для системи "Безбар'єрний доступ України"
## 2. Локації та об'єкти безбар'єрності

### 2.1. Управління локаціями

#### `GET /api/locations`

**Опис**: Отримання списку локацій з фільтрацією **Параметри (query)**:

- `lat` (опціональний, Double) - Широта для пошуку поблизу
- `lng` (опціональний, Double) - Довгота для пошуку поблизу
- `radius` (опціональний, Integer, default: 5000) - Радіус пошуку в метрах
- `types` (опціональний, String) - Типи об'єктів, розділені комами
- `features` (опціональний, String) - Необхідні елементи безбар'єрності, розділені комами
- `minScore` (опціональний, Integer, min: 1, max: 5) - Мінімальний рейтинг доступності
- `status` (опціональний, enum: 'draft', 'pending', 'published', 'rejected') - Статус об'єкту
- `verified` (опціональний, Boolean) - Чи верифікований об'єкт
- `query` (опціональний, String) - Пошуковий запит за назвою або адресою
- `page` (опціональний, number, default: 1) - Номер сторінки
- `limit` (опціональний, number, default: 20, max: 100) - Кількість об'єктів на сторінці

**Успішна відповідь (200)**:

```json
[
    {
        "id": "76b6a917-12ad-4f39-b7e9-fe1c857444c7",
        "name": "object3",
        "address": "п'ятдесятна",
        "coordinates": {
            "lat": 50.0,
            "lng": 50.0
        },
        "type": "healthcare",
        "category": "cat1",
        "description": "desc1",
        "contacts": {
            "phone": "+38076392813",
            "email": "a@gmail.com",
            "website": "website.com"
        },
        "workingHours": {
            "monday": {
                "open": "09:00",
                "close": "18:00"
            },
            "tuesday": {
                "open": "09:00",
                "close": "18:00"
            },
            "wednesday": {
                "open": "09:00",
                "close": "18:00"
            },
            "thursday": {
                "open": "09:00",
                "close": "18:00"
            },
            "friday": {
                "open": "09:00",
                "close": "17:00"
            },
            "saturday": {
                "open": "09:00",
                "close": "17:00"
            },
            "sunday": {
                "open": "09:00",
                "close": "17:00"
            }
        },
        "createdBy": "c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555",
        "organizationId": "f07b1a9f-018c-454f-9e90-3520d4c1ac38",
        "status": "draft",
        "overallAccessibilityScore": null,
        "createdAt": "2025-04-13T10:05:01.4921",
        "updatedAt": "2025-04-13T10:05:01.4921",
        "lastVerifiedAt": "2025-04-13T00:00:00",
        "rejectionReason": null
    },
    {
        "id": "cf47b928-755b-49b6-bfd0-c528663dc045",
        "name": "об'єктна шняга",
        "address": "п'ятдесятна 22",
        "coordinates": {
            "lat": 50.0,
            "lng": 50.0
        },
        "type": "culture",
        "category": "cat2",
        "description": "desc1",
        "contacts": {
            "phone": "+38076392813",
            "email": "a@gmail.com",
            "website": "website.com"
        },
        "workingHours": {
            "monday": {
                "open": "09:00",
                "close": "18:00"
            },
            "tuesday": {
                "open": "09:00",
                "close": "18:00"
            },
            "wednesday": {
                "open": "09:00",
                "close": "18:00"
            },
            "thursday": {
                "open": "09:00",
                "close": "18:00"
            },
            "friday": {
                "open": "09:00",
                "close": "17:00"
            },
            "saturday": {
                "open": "09:00",
                "close": "17:00"
            },
            "sunday": {
                "open": "09:00",
                "close": "17:00"
            }
        },
        "createdBy": "c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555",
        "organizationId": "f07b1a9f-018c-454f-9e90-3520d4c1ac38",
        "status": "draft",
        "overallAccessibilityScore": null,
        "createdAt": "2025-04-13T10:42:20.160956",
        "updatedAt": "2025-04-13T10:42:20.160956",
        "lastVerifiedAt": "2025-04-13T00:00:00",
        "rejectionReason": null
    }
]
```

#### `GET /api/locations/{location_id}`

**Опис**: Отримання детальної інформації про конкретну локацію **Параметри (path)**:

- `location_id` (обов'язковий, UUID) - ID локації

**Успішна відповідь (200)**:

```json
{
    "id": "fca09385-5171-428d-80fe-708f4af8b4c6",
    "name": "object1",
    "address": "Шевченка 37-В",
    "coordinates": {
        "lat": 17.3,
        "lng": 24.5
    },
    "type": "healthcare",
    "category": "cat1",
    "description": "desc1",
    "contacts": {
        "phone": "+38076392813",
        "email": "a@gmail.com",
        "website": "website.com"
    },
    "workingHours": {
        "monday": {
            "open": "09:00",
            "close": "18:00"
        },
        "tuesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "wednesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "thursday": {
            "open": "09:00",
            "close": "18:00"
        },
        "friday": {
            "open": "09:00",
            "close": "17:00"
        },
        "saturday": {
            "open": "09:00",
            "close": "17:00"
        },
        "sunday": {
            "open": "09:00",
            "close": "17:00"
        }
    },
    "createdBy": "c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555",
    "organizationId": "f07b1a9f-018c-454f-9e90-3520d4c1ac38",
    "status": "draft",
    "overallAccessibilityScore": null,
    "createdAt": "2025-04-13T08:06:30.307548",
    "updatedAt": "2025-04-13T08:06:30.307548",
    "lastVerifiedAt": "2025-04-13T00:00:00",
    "rejectionReason": null
}
```

#### `POST /api/locations`

**Опис**: Створення нової локації **Параметри (body)**:

- `name` (обов'язковий, string, max 255) - Назва об'єкту
- `address` (обов'язковий, String, max 500) - Адреса об'єкту
- `coordinates` (обов'язковий, object) - Координати об'єкту
  - `lat` (обов'язковий, number) - Широта
  - `lng` (обов'язковий, number) - Довгота
- `type` (обов'язковий, Enum(government_building, business, healthcare education, culture, transport, recreation, other)) - Тип об'єкту
- `category` (опціональний, String, max 100) - Підкатегорія об'єкту
- `description` (опціональний, String) - Опис об'єкту
- `contacts` (опціональний, object) - Контактна інформація
  - `phone` (опціональний, String) - Телефон
  - `email` (опціональний, String) - Електронна пошта
  - `website` (опціональний, String) - Веб-сайт
- `workingHours` (опціональний, object) - Години роботи
  - `day(monday,tuesday,..)` - День
    - `open` - приклад "09:00"
    - `close` - приклад "18:00"
- `organizationId` (опціональний, UUID) - ID організації, яка управляє об'єктом
- `status` (опціональний, enum, default: 'draft') - Статус публікації (  draft, pending, published, rejected)

**Приклад body**:
```json
{
    "name": "об'єктна шняга",
    "address": "п'ятдесятна 22",
    "coordinates": {
        "lat": 50.0,
        "lng": 50.0
    },
    "type": "culture",
    "category": "cat2",
    "description": "desc1",
    "contacts": {
        "phone": "+38076392813",
        "email": "a@gmail.com",
        "website": "website.com"
    },
    "workingHours": {
        "monday": {
            "open": "09:00",
            "close": "18:00"
        },
        "tuesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "wednesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "thursday": {
            "open": "09:00",
            "close": "18:00"
        },
        "friday": {
            "open": "09:00",
            "close": "17:00"
        },
        "saturday": {
            "open": "09:00",
            "close": "17:00"
        },
        "sunday": {
            "open": "09:00",
            "close": "17:00"
        }
    },
    "organizationId":"f07b1a9f-018c-454f-9e90-3520d4c1ac38",
    "status": "draft",
    "createdBy": "c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555",
    "lastVerifiedAt": "2025-04-13T00:00:00"

}
```

**Успішна відповідь (201)**:

```json
{
{
    "name": "об'єктна шняга",
    "address": "п'ятдесятна 22",
    "coordinates": {
        "lat": 50.0,
        "lng": 50.0
    },
    "type": "culture",
    "category": "cat2",
    "description": "desc1",
    "contacts": {
        "phone": "+38076392813",
        "email": "a@gmail.com",
        "website": "website.com"
    },
    "workingHours": {
        "monday": {
            "open": "09:00",
            "close": "18:00"
        },
        "tuesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "wednesday": {
            "open": "09:00",
            "close": "18:00"
        },
        "thursday": {
            "open": "09:00",
            "close": "18:00"
        },
        "friday": {
            "open": "09:00",
            "close": "17:00"
        },
        "saturday": {
            "open": "09:00",
            "close": "17:00"
        },
        "sunday": {
            "open": "09:00",
            "close": "17:00"
        }
    },
    "createdBy": "c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555",
    "organizationId": "f07b1a9f-018c-454f-9e90-3520d4c1ac38",
    "status": "draft",
    "overallAccessibilityScore": null,
    "createdAt": "2025-04-13T10:42:20.1609562",
    "updatedAt": "2025-04-13T10:42:20.1609562",
    "lastVerifiedAt": "2025-04-13T00:00:00",
    "rejectionReason": null
}
}
```

### 2.2. Елементи безбар'єрності

#### `GET /api/locations/{locationId}/features`

**Опис**: Отримання списку елементів безбар'єрності для конкретної локації 

**Параметри (path)**:

- `locationId` (обов'язковий, UUID) - ID локації

**Успішна відповідь (200)**:

```json
{
 
}
```

**Відповідь з помилкою (404)**:

```json
{
  "error": "not_found",
  "message": "Локацію не знайдено"
}
```

#### `POST /api/locations/{locationId}/features`

**Опис**: Додавання нового елементу безбар'єрності для локації **Параметри**:

- **Path**:
  - `locationId` (обов'язковий, UUID) - ID локації
- **Body**:
  - `type` (обов'язковий, enum) - Тип елементу безбар'єрності
  - `subtype` (опціональний, string) - Підтип елементу
  - `description` (опціональний, string) - Опис елементу
  - `status` (обов'язковий, boolean) - Наявність елементу
  - `qualityRating` (опціональний, integer) - Оцінка якості
  - `standardsCompliance` (опціональний, boolean) - Відповідність ДБН

**Успішна відповідь (201)**:

```json

```


#### `PUT /api/locations/{locationId}/features/{featureId}`

**Опис**: Оновлення існуючого елементу безбар'єрності **Параметри**:

- **Path**:
  - `locationId` (обов'язковий, UUID) - ID локації
  - `featureId` (обов'язковий, UUID) - ID елементу безбар'єрності
- **Body**:
  - `type` (опціональний, enum) - Тип елементу безбар'єрності
  - `subtype` (опціональний, string) - Підтип елементу
  - `description` (опціональний, string) - Опис елементу
  - `status` (опціональний, boolean) - Наявність елементу
  - `qualityRating` (опціональний, integer) - Оцінка якості
  - `standardsCompliance` (опціональний, boolean) - Відповідність ДБН

**Успішна відповідь (200)**:

```json

```

**Відповідь з помилкою (404)**:

```json
{
  "error": "not_found",
  "message": "Локацію не знайдено"
}
```

**Відповідь з помилкою (404)**:

```json
{
  "error": "not_found",
  "message": "Елемент безбар'єрності не знайдено"
}
```

#### `DELETE /api/locations/{locationId}/features/{featureId}`

**Опис**: Видалення елементу безбар'єрності **Параметри (path)**:

- `locationId` (обов'язковий, UUID) - ID локації
- `featureId` (обов'язковий, UUID) - ID елементу безбар'єрності

**Успішна відповідь (200)**:

```json
{
  "message": "Елемент безбар'єрності успішно видалено"
}
```

**Відповідь з помилкою (404)**:

```json
{
  "error": "not_found",
  "message": "Локацію не знайдено"
}
```

**Відповідь з помилкою (404)**:

```json
{
  "error": "not_found",
  "message": "Елемент безбар'єрності не знайдено"
}
```
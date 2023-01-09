INSERT INTO news_source (name, url)
VALUES ('irbis.plus', 'https://irbis.plus.ru'),
       ('praktika.irbis.plus', 'https://praktika.irbis.plus.ru'),
       ('vacancies.irbis.plus', 'https://vacancies.irbis.plus.ru');

INSERT INTO news_topic (source_id, name)
VALUES (1, 'Помощь юр. лицам'),
       (1, 'Помощь физ. лицам'),
       (1, 'Бух. отчётность'),
       (1, 'О нас'),
       (2, 'Обновления сервиса'),
       (2, 'Панель данных'),
       (3, 'Открытые вакансии (г. Ижевск)'),
       (3, 'Открытые вакансии (удаленно)');

INSERT INTO news_body (topic_id, title, url_news, public_date)
VALUES (1, 'Обновления законодательства в 2020 году.', 'https://irbis.plus.ru/obnovleniya-v-zakonodatelstve-2020', '2019-12-26 11:20:59+03'),
       (1, 'Обновления законодательства в 2021 году.', 'https://irbis.plus.ru/obnovleniya-v-zakonodatelstve-2021', '2020-12-26 12:41:51+03'),
       (1, 'Обновления законодательства в 2022 году.', 'https://irbis.plus.ru/obnovleniya-v-zakonodatelstve-2022', '2021-12-26 12:05:15+03'),
       (1, 'Обновления законодательства в 2023 году.', 'https://irbis.plus.ru/obnovleniya-v-zakonodatelstve-2023', '2022-12-26 13:00:53+03'),
       (1, 'Среднесписочная численность сотрудников в 2022 году.', 'https://irbis.plus.ru/srednespisochnaya-chislennost-sotrudnikov-2022', '2021-12-20 10:18:14+03'),
       (1, 'Среднесписочная численность сотрудников в 2023 году.', 'https://irbis.plus.ru/srednespisochnaya-chislennost-sotrudnikov-2023', '2022-12-20 10:39:50+03'),
       (2, 'Рассказываем о том, как обезопасить себя от мошенников', 'https://irbis.plus.ru/kak-obezopasit-cebya-ot-moshennikov', '2022-10-18 19:37:59+03'),
       (2, 'База всех судов России с 2005 года с карточками дел.', 'https://irbis.plus.ru/baza-vseh-sydov', '2022-08-04 21:12:19+03'),
       (2, 'Залоги движимого имущества', 'https://irbis.plus.ru/zalogi-dvizhimogo-imyshestva', '2022-11-29 09:31:59+03'),
       (3, 'Чистая прибыль в 2021 году.', 'https://irbis.plus.ru/chistaya-pribyl-2022', '2021-12-26 12:30:59+03'),
       (3, 'Чистая прибыль в 2022 году.', 'https://irbis.plus.ru/chistaya-pribyl-2023', '2022-12-26 12:30:59+03'),
       (4, 'Рассказываем о том, как отдыхают наши работники', 'https://irbis.plus.ru/kak-otdyhayt-nashi-rabotniki', '2022-08-07 16:34:25+03'),
       (4, 'Знакомим с нашими клиентами. Часть 1', 'https://irbis.plus.ru/znakomim-s-klientami-1', '2022-09-15 12:32:59+03'),
       (4, 'Знакомим с нашими клиентами. Часть 2', 'https://irbis.plus.ru/znakomim-s-klientami-2', '2022-10-15 12:31:59+03'),
       (5, 'Знакомство с сервисом', 'https://praktika.irbis.plus.ru/znakomstvo-s-servisom', '2022-11-26 08:30:59+03'),
       (5, 'Нововведения во вкладке Суды', 'https://praktika.irbis.plus.ru/novovvedeniya-vo-vklade-sydy', '2022-02-06 13:30:09+03'),
       (6, 'Суды общей юрисдикции', 'https://praktika.irbis.plus.ru/sydy-obshie', '2022-03-26 12:30:59+03'),
       (6, 'Арбитражные суды', 'https://praktika.irbis.plus.ru/arbitrazhnye-sydy', '2022-03-26 12:30:59+03'),
       (6, 'Залоги', 'https://praktika.irbis.plus.ru/zalogi', '2022-12-26 03:30:59+03'),
       (7, 'Специалист отдела телемаркетинга', 'https://vacancies.irbis.plus.ru/cpeccialist-telemarketinga', '2022-12-26 12:30:59+03'),
       (7, 'Менеджер по продажам', 'https://vacancies.irbis.plus.ru/manager-saler', '2022-12-26 12:30:59+03'),
       (8, 'Java Разработчик', 'https://vacancies.irbis.plus.ru/java-developer', '2022-12-21 11:31:51+03');

INSERT INTO task_properties (cron_expression, options)
VALUES ('0 */1 * * * *', 'irbis.plus'),
       ('0 */1 * * * *', 'praktika.irbis.plus'),
       ('0 */1 * * * *', 'vacancies.irbis.plus');
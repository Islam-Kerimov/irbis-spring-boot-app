CREATE TABLE IF NOT EXISTS news_source
(
    source_id SERIAL PRIMARY KEY,
    name      VARCHAR(64) UNIQUE NOT NULL,
    url       VARCHAR UNIQUE     NOT NULL
);

CREATE TABLE IF NOT EXISTS news_topic
(
    topic_id  SERIAL PRIMARY KEY,
    source_id INTEGER      NOT NULL,
    name      VARCHAR(128) NOT NULL,
    FOREIGN KEY (source_id) REFERENCES news_source (source_id)
);

CREATE TABLE IF NOT EXISTS news_body
(
    id          SERIAL PRIMARY KEY,
    topic_id    INTEGER                  NOT NULL,
    title       VARCHAR(128)             NOT NULL,
    url_news    VARCHAR                  NOT NULL,
    public_date TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES news_topic (topic_id)
);

CREATE TABLE IF NOT EXISTS task_properties
(
    id              SERIAL PRIMARY KEY,
    cron_expression VARCHAR(32) NOT NULL,
    options         VARCHAR(128)
);
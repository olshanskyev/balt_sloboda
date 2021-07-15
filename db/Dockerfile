# Derived from official mysql image (our base image)
FROM postgres

ENV DATABASE_SCHEMA=BALTSLOBODA

RUN apt-get update -y \
    && apt-get install -y --no-install-recommends gettext \
    && rm -rf /var/lib/apt/lists/*
COPY ./sql-scripts/CreateTable.sql ./sql-scripts/InsertData.sql /docker-entrypoint-initdb.d_tmp/
RUN envsubst '$${DATABASE_SCHEMA}' < "/docker-entrypoint-initdb.d_tmp/CreateTable.sql" > "/docker-entrypoint-initdb.d/CreateTable.sql"
RUN envsubst '$${DATABASE_SCHEMA}' < "/docker-entrypoint-initdb.d_tmp/InsertData.sql" > "/docker-entrypoint-initdb.d/InsertData.sql"

EXPOSE 3399
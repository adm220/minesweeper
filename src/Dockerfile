FROM lpicanco/java11-alpine

ARG JAR_FILE

ENV ARTIFACT_NAME=$JAR_FILE

WORKDIR /app

COPY entrypoint.sh /app
COPY target/${JAR_FILE} /app


ENTRYPOINT ["/app/entrypoint.sh"]

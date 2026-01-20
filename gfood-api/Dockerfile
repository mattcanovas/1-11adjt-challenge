FROM maven:3.9.12-amazoncorretto-17-alpine AS build

WORKDIR /app

COPY . ./gfood-api

RUN --mount=type=cache,target=/root/.m2 \
	mvn clean package -f ./gfood-api/pom.xml -DskipTests && \
	cp ./gfood-api/target/*.jar ./app.jar

FROM amazoncorretto:17-alpine AS runtime

RUN apk add --no-cache \
	fontconfig \
	ttf-dejavu \
	wget \
	tzdata

RUN cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
	echo "America/Sao_Paulo" > /etc/timezone

RUN addgroup -g 1001 -S appgroup && \
	adduser -u 1001 -S appuser -G appgroup


WORKDIR /app
USER appuser

ENV USER_TIMEZONE="America/Sao_Paulo" \
	TZ=America/Sao_Paulo \
	LANG=pt_BR.UTF-8 \
	LANGUAGE=pt_BR:pt:en \
	LC_ALL=pt_BR.UTF-8 \
	LC_TIME=pt_BR.UTF-8 \
	LC_MONETARY=pt_BR.UTF-8 \
	SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

# Configuração otimizada para container
ENV JAVA_OPTS="-XX:+UseContainerSupport \
	-XX:MaxRAMPercentage=75.0 \
	-XX:+UseG1GC \
	-XX:+UseStringDeduplication \
	-Djava.security.egd=file:/dev/./urandom \
	-Dspring.backgroundpreinitializer.ignore=true \
	-Dfile.encoding=UTF-8 \
	-Duser.timezone=America/Sao_Paulo"

FROM runtime
ENV JAVA_OPTS="${JAVA_OPTS} \
	-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 \
	--add-opens java.base/java.lang=ALL-UNNAMED \
	--add-opens java.base/java.util=ALL-UNNAMED \
	--add-opens java.base/java.lang.reflect=ALL-UNNAMED"
COPY --from=build --chown=appuser:appgroup /app/app.jar /app/app.jar
EXPOSE 8080 8000
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
	CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# 后端应用Dockerfile
# 构建阶段
FROM maven:3.8.6-openjdk-8-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml并下载依赖
COPY pom.xml .
COPY settings.xml ./settings.xml

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline -B -s settings.xml

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests -s settings.xml

# 运行阶段
FROM openjdk:8-jre-alpine

# 创建应用用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 设置工作目录
WORKDIR /app

# 从构建阶段复制JAR文件
COPY --from=builder /app/target/*.jar app.jar

# 复制配置文件
COPY --chown=appuser:appgroup src/main/resources/ ./resources/

# 创建日志目录
RUN mkdir -p logs && chown -R appuser:appgroup logs

# 切换到应用用户
USER appuser

# 暴露端口
EXPOSE 8080

# 设置JVM参数
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
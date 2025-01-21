# ベースイメージとしてOpenJDKを使用
FROM openjdk:17-jdk-slim

# 作業ディレクトリを作成
WORKDIR /app

# 依存関係とアプリケーションコードをコピー
COPY ./build/libs/home-3.4.1-SNAPSHOT.jar app.jar

# ポートを公開
EXPOSE 8080

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "app.jar"]

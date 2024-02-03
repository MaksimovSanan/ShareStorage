# Запуск всех сервисов в фоновом режиме
java -jar EurekaServer/target/EurekaServer-0.0.1-SNAPSHOT.jar &
java -jar ItemsService/target/ItemsService-0.0.1-SNAPSHOT.jar &
java -jar UsersService/target/UsersService-0.0.1-SNAPSHOT.jar &
java -jar Aggregator/target/Aggregator-0.0.1-SNAPSHOT.jar &

# Запуск сервера авторизации
java -jar authorization-server/target/authorization-server-1.0-SNAPSHOT.jar &

# Ожидание доступности сервера авторизации
echo "Waiting for authorization server to become available..."
until $(curl --output /dev/null --silent --head --fail http://localhost:9000); do
    printf '.'
    sleep 1
done

echo "Authorization server is available. Starting WebClient."

# Запуск WebClient
java -jar WebClient/target/WebClient-0.0.1-SNAPSHOT.jar &

# Запуск сервера авторизации
java -jar authorization-server.jar &

# Ожидание доступности сервера авторизации
echo "Waiting for authorization server to become available..."
until $(curl --output /dev/null --silent --head --fail http://localhost:9000); do
    printf '.'
    sleep 1
done

echo "Authorization server is available. Starting WebClient."

# Запуск WebClient
java -jar web-client.jar


echo "Building JAR files"
cd  EurekaServer
./mvnw clean package
cd ..

cd UsersService
./mvnw clean package
cd ..

cd ItemsService
./mvnw clean package
cd ..

cd authorization-server
./mvnw clean package
cd ..

cd WebClient
./mvnw clean package
cd ..

cd APIGateway
./mvnw clean package
cd ..
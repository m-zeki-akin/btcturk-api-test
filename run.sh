docker pull openjdk:17-jdk-slim
docker run -d --name openjdk -p 4444:4444 -v --shm-size="2g" openjdk:17-jdk-slim
./mvnw clean test
read
docker stop openjdk
docker rm openjdk
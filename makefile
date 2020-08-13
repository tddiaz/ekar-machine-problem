build:
	./mvnw clean package

docker-image:
	docker build -t ekar-machine-problem:latest .

run:
	docker-compose up -d

stop:
	docker-compose down
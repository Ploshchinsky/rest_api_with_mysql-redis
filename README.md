"# REST-API + MYSQL-REDIS (DOCKER)" 

In this project, I am practicing skills to create a REST API application using the following technologies:
- Java 
- Maven
- Spring Boot (Jackson, Hibernate)
- MySQL (Docker container)
- Redis (Docker container)

Options for creating Docker containers:
MySQL:
docker run --rm --name=mysql_db -P 3307:3306 -d mysql

-Standard container with mysql, where I throw the port for connection from the main terminal to the application through 3307

Redis:
docker run --rm --name=redis_db -p 6379:6379/tcp -d redis

Standard container with Reids, where default ports are used

Translated with DeepL.com (free version)
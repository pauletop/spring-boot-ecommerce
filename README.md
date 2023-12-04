# Getting started

## Database
### Postgres
In this project, I use Postgres as my database.
Postgres is a relational database management system developed 
by PostgreSQL Global Development Group; compare to MySQL,
Postgres is more powerful and has more features, such as
supporting JSON data type, and it is more secure.
<br>
The database is hosted on Docker, and the Docker image is
pulled from Docker Hub. The Docker image is defined in
`docker-compose.yaml` file. To run the database, run
```shell
docker-compose up -d
```

## Have a nice day!
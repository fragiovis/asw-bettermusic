# Migrazione Database a PostgreSQL

## build.gradle
- album: sostituito `org.hsqldb:hsqldb` con `org.postgresql:postgresql`
- recensioni: sostituito `org.hsqldb:hsqldb` con `org.postgresql:postgresql`
- connessioni: sostituito `org.hsqldb:hsqldb` con `org.postgresql:postgresql`
- recensioni-seguite: sostituito `org.hsqldb:hsqldb` con `org.postgresql:postgresql`

## application.yml
- album: aggiunti `spring.datasource` (jdbc:postgresql://localhost:5433/albumdb) e `spring.jpa` (ddl-auto=update, dialect PostgreSQL)
- recensioni: aggiunti `spring.datasource` (jdbc:postgresql://localhost:5434/recensionidb) e `spring.jpa`
- connessioni: aggiunti `spring.datasource` (jdbc:postgresql://localhost:5435/connessionidb) e `spring.jpa`
- recensioni-seguite: aggiunti `spring.datasource` (jdbc:postgresql://localhost:5436/recseguitedb) e `spring.jpa`

## docker-compose.yml
- aggiunti i servizi:
  - album-db (porta 5433, db=albumdb, user=album)
  - recensioni-db (porta 5434, db=recensionidb, user=recensioni)
  - connessioni-db (porta 5435, db=connessionidb, user=connessioni)
  - recensioni-seguite-db (porta 5436, db=recseguitedb, user=recseg)
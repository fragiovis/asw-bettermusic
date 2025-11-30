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

# Integrazione Kafka per AlbumCreatedEvent

## album/build.gradle
- aggiunta `org.springframework.kafka:spring-kafka`
- perché: abilitare il producer Kafka per pubblicare `AlbumCreatedEvent`.

## album/src/main/resources/application.yml
- aggiunta sezione `spring.kafka.producer` con serializer JSON
- perché: serializzare correttamente eventi `AlbumCreatedEvent` e inviare a Kafka.

## album/src/main/java/asw/bettermusic/album/domain/AlbumServiceImpl.java
- autowire `AlbumEventPublisher` e pubblicazione evento dopo `save`
- perché: notificare la creazione di un album ai consumatori senza dipendenze sincrone.

## album/src/main/java/asw/bettermusic/album/events/AlbumEventPublisher.java (nuovo file)
- pubblica su topic `album.created` un `AlbumCreatedEvent`
- perché: adapter infrastrutturale esterno al dominio per messaging.

## album-api/src/main/java/asw/bettermusic/album/api/events/AlbumCreatedEvent.java (nuovo file)
- DTO evento con id, titolo, artista, generi
- perché: contratto condiviso tra producer e consumer.

## recensioni/build.gradle
- aggiunta `org.springframework.kafka:spring-kafka`
- perché: abilitare il consumer Kafka per ricevere `AlbumCreatedEvent`.

## recensioni/src/main/resources/application.yml
- aggiunta sezione `spring.kafka.consumer` (group-id, deserializer JSON, trusted packages)
- perché: deserializzare eventi e consumarli dal topic.

## recensioni/src/main/java/asw/bettermusic/recensioni/RecensioniApplication.java
- aggiunto `@EnableKafka`
- perché: abilitare l'elaborazione di `@KafkaListener` nel servizio recensioni.

## recensioni/src/main/java/asw/bettermusic/recensioni/domain/Album.java
- trasformato in entity JPA con `@Entity` e `@ElementCollection` per generi
- perché: mantenere gli album localmente nel DB di `recensioni`.

## recensioni/src/main/java/asw/bettermusic/recensioni/domain/AlbumRepository.java (nuovo file)
- repository JPA per album
- perché: porta di persistenza locale conforme all’architettura esagonale.

## recensioni/src/main/java/asw/bettermusic/recensioni/events/AlbumEventsConsumer.java (nuovo file)
- `@KafkaListener` su topic `album.created` che salva/aggiorna album locale
- perché: aggiornare il DB locale di `recensioni` sugli eventi.

## recensioni/src/main/java/asw/bettermusic/recensioni/domain/RecensioniServiceImpl.java
- rimosso uso di `AlbumClientPort` in creazione; usato `AlbumRepository.findByTitoloAndArtista`
- perché: evitare invocazioni remote nella creazione recensione; maggiore disponibilità e performance.

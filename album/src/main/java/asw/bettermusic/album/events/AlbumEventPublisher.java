package asw.bettermusic.album.events;

import asw.bettermusic.album.domain.Album;
import asw.bettermusic.album.api.events.AlbumCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class AlbumEventPublisher {
  private static final String TOPIC = "album.created";

  @Autowired
  private KafkaTemplate<String, AlbumCreatedEvent> kafkaTemplate;

  public void publishAlbumCreated(Album album) {
    AlbumCreatedEvent event = new AlbumCreatedEvent(album.getId(), album.getTitolo(), album.getArtista(), album.getGeneri());
    kafkaTemplate.send(TOPIC, event);
  }
}

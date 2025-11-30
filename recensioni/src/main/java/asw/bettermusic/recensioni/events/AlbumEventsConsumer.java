package asw.bettermusic.recensioni.events;

import asw.bettermusic.album.api.events.AlbumCreatedEvent;
import asw.bettermusic.recensioni.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class AlbumEventsConsumer {

  @Autowired
  private AlbumRepository albumRepository;

  @KafkaListener(topics = "album.created", groupId = "recensioni")
  public void handle(AlbumCreatedEvent event) {
    Album album = new Album(event.getId(), event.getTitolo(), event.getArtista(), event.getGeneri());
    albumRepository.save(album);
  }
}

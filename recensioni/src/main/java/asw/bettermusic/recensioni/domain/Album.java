package asw.bettermusic.recensioni.domain;

import lombok.*; 

import java.util.*; 
import jakarta.persistence.*; 

/* Un album. */  
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Album {

	/* id dell'album */ 
	@Id
	private Long id; 
	/* titolo dell'album */ 
	private String titolo; 
	/* artista dell'album */ 
	private String artista; 
	/* generi dell'album */ 
	@ElementCollection
	private Set<String> generi; 
	
}

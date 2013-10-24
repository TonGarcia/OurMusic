package ourmusic.controler;

import ourmusic.model.Musica;
import ourmusic.model.Player;

public class PlayerControler {

	private Player player;

	public PlayerControler(Musica musica) {	
		this.player = new Player(musica);
	}
	
	
	
}

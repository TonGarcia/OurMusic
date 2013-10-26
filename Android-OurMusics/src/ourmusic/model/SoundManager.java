package ourmusic.model;

import java.io.ByteArrayOutputStream;

public interface SoundManager {
	
	public ByteArrayOutputStream record();
	public void play(ByteArrayOutputStream baos);
	public void stop();
	public void pause();

}

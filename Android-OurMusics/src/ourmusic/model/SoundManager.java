package ourmusic.model;

public interface SoundManager {
	
	public byte[] record();
	public void play(byte[] baos);
	public void stop();
	public void pause();

}

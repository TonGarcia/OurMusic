package ourmusic.model;

import java.io.ByteArrayOutputStream;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class Player implements SoundManager {

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private AudioRecord recorder = null;
	private int bufferSize;
	private boolean stopFlag = false;
	private byte[] songInBytes = null;

	@Override
	public void record() {
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
				RECORDER_CHANNELS, AudioFormat.ENCODING_PCM_16BIT);
		// Instanciando array de bytes
		byte data[] = new byte[bufferSize];
		// Inicializando AudioRecord
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, bufferSize);
		if (recorder == null) {
			System.out.println("recorder nao foi instanciado");
		} else if (recorder != null) {
			System.out.println("recorder foi instanciado");
		}

		// Inicializando the ByteArrayOutPutStream
		ByteArrayOutputStream baos = null;
		baos = new ByteArrayOutputStream();
		// Verificando Status do objeto recorder
		int i = recorder.getState();
		System.out.println("recorder.getState: " + i);
		if (i == 1) {
			recorder.startRecording();
		}

		while (stopFlag == false) {
			int result = recorder.read(data, 0, data.length);
			if (result == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new RuntimeException();
			} else if (result == AudioRecord.ERROR_BAD_VALUE) {
				throw new RuntimeException();
			} else {
				baos.write(data, 0, result);
			}
		}

		recorder.stop();
		recorder.release();
		recorder = null;

		stopFlag = false;
		
		songInBytes = baos.toByteArray();
	}

	@Override
	public void play() {
		if(songInBytes != null){
			AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC,
					RECORDER_SAMPLERATE, RECORDER_CHANNELS,
					RECORDER_AUDIO_ENCODING, songInBytes.length, AudioTrack.MODE_STATIC);
			at.write(songInBytes, 0, songInBytes.length);
			at.play();
			if (stopFlag == true) {
				at.stop();
			}
		}
		stopFlag = false;
	}

	@Override
	public void stop() {
		stopFlag = true;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

}

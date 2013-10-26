package ourmusic.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class Player implements SoundManager {

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private AudioRecord recorder = null;
	private int bufferSize = 0;

	@Override
	public ByteArrayOutputStream  record() {
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		// Instanciando array de bytes
		byte data[] = new byte[bufferSize];
		// Inicializando AudioRecord
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, bufferSize);

		// Inicializando the ByteArrayOutPutStream
		ByteArrayOutputStream baos = null;
		baos = new ByteArrayOutputStream();
		// Verificando Status do objeto recorder
		int i = recorder.getState();
		if (i == 1) {
			recorder.startRecording();
		}

		long tempoInicio = System.currentTimeMillis();
		long tempoDecorrido = 0;
		while (tempoDecorrido <= 4000) {
			int result = recorder.read(data, 0, data.length);
			if (result == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new RuntimeException();
			} else if (result == AudioRecord.ERROR_BAD_VALUE) {
				throw new RuntimeException();
			} else {
				baos.write(data, 0, result);
			}
			tempoDecorrido = System.currentTimeMillis() - tempoInicio;
		}

		recorder.stop();
		recorder.release();
		recorder = null;

		return baos;
		
	}

	@Override
	public void play(ByteArrayOutputStream baos) {
		MediaPlayer mp = new MediaPlayer();
		

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

}

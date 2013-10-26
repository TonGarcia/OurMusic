package ourmusic.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe.SourceChannel;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class Player implements SoundManager {

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private AudioRecord recorder = null;
	private int bufferSize;

	@Override
	public byte[] record() {
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		System.out.println("buffer size: "+bufferSize);
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
			System.out.println("recorder instanciado!!!");
		}

		long tempoInicio = System.currentTimeMillis();
		long tempoDecorrido = 0;
		while (tempoDecorrido <= 4000) {
			int result = recorder.read(data, 0, data.length);
			System.out.println("data.length: " + data.length);
			System.out.println("result: " + result);
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

		return baos.toByteArray();

	}

	@Override
	public void play(byte[] somEmBytes) {
		AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, somEmBytes.length, AudioTrack.MODE_STATIC);
		at.write(somEmBytes, 0, somEmBytes.length);
		at.play();

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

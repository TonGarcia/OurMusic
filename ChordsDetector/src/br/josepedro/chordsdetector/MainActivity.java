package br.josepedro.chordsdetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.R.drawable;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.jp.example.soundrecordingexample2.R;

public class MainActivity extends Activity {

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private MyFFT myfft;
	private TextView chordTV;
	private ImageButton btnSetSoundGetChord;
	private RecordingAndSetChord asyncTask = null;
	private boolean started = false;

	private AudioRecord recorder = null;
	private int bufferSize = 0;

	private ProgressBar pbC;
	private ProgressBar pbCs;
	private ProgressBar pbD;
	private ProgressBar pbEb;
	private ProgressBar pbE;
	private ProgressBar pbF;
	private ProgressBar pbFs;
	private ProgressBar pbG;
	private ProgressBar pbGs;
	private ProgressBar pbA;
	private ProgressBar pbBb;
	private ProgressBar pbB;
	private ProgressBar[] barraCores;

	private Drawable microphoneButton;
	
	private Shader textGradient;

	private static String[] notas = { "C", "C#", "D", "Eb", "E", "F", "F#",
			"G", "G#", "A", "Bb", "B" };
	private static int[] coresNotas = { Color.rgb(95, 158, 160),
			Color.rgb(218, 165, 32), Color.rgb(0, 0, 255),
			Color.rgb(50, 205, 50), Color.rgb(123, 104, 238),
			Color.rgb(255, 20, 147), Color.rgb(238, 130, 238),
			Color.rgb(255, 0, 0), Color.rgb(165, 42, 42),
			Color.rgb(255, 165, 0), Color.rgb(205, 133, 63),
			Color.rgb(0, 206, 209) };

	private static String[] nomeAcordes = { "F", "F m", "F aum", "F dim", "F#",
			"F# m", "F# aum", "F# dim", "G", "G m", "G aum", "G dim", "G#",
			"G# m", "G# aum", "G# dim", "A", "A m", "A aum", "A dim", "Bb",
			"Bb m", "Bb aum", "Bb dim", "B", "B m", "B aum", "B dim", "C",
			"C m", "C aum", "C dim", "C#", "C# m", "C# aum", "C# dim", "D",
			"D m", "D aum", "D dim", "Eb", "Eb m", "Eb aum", "Eb dim", "E",
			"E m", "E aum", "E dim" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);

		myfft = new MyFFT();
		chordTV = (TextView) findViewById(R.id.Chord);
		chordTV.setTextSize(70);
		// Colocando o botao do microfone branco
		microphoneButton = this.getResources().getDrawable(
				drawable.ic_btn_speak_now);
		ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
		microphoneButton.setColorFilter(filter);

		btnSetSoundGetChord = (ImageButton) findViewById(R.id.btnSetSoundGetChord);
		btnSetSoundGetChord.setImageDrawable(microphoneButton);
		btnSetSoundGetChord.setOnClickListener(btnClick);

		barraCores = new ProgressBar[12];

		pbC = (ProgressBar) findViewById(R.id.vertical_progressbarC);
		pbC.setProgress(0);
		barraCores[0] = pbC;
		pbCs = (ProgressBar) findViewById(R.id.vertical_progressbarCs);
		pbCs.setProgress(0);
		barraCores[1] = pbCs;
		pbD = (ProgressBar) findViewById(R.id.vertical_progressbarD);
		pbD.setProgress(0);
		barraCores[2] = pbD;
		pbEb = (ProgressBar) findViewById(R.id.vertical_progressbarEb);
		pbEb.setProgress(0);
		barraCores[3] = pbEb;
		pbE = (ProgressBar) findViewById(R.id.vertical_progressbarE);
		pbE.setProgress(0);
		barraCores[4] = pbE;
		pbF = (ProgressBar) findViewById(R.id.vertical_progressbarF);
		pbF.setProgress(0);
		barraCores[5] = pbF;
		pbFs = (ProgressBar) findViewById(R.id.vertical_progressbarFs);
		pbFs.setProgress(0);
		barraCores[6] = pbFs;
		pbG = (ProgressBar) findViewById(R.id.vertical_progressbarG);
		pbG.setProgress(0);
		barraCores[7] = pbG;
		pbGs = (ProgressBar) findViewById(R.id.vertical_progressbarGs);
		pbGs.setProgress(0);
		barraCores[8] = pbGs;
		pbA = (ProgressBar) findViewById(R.id.vertical_progressbarA);
		pbA.setProgress(0);
		barraCores[9] = pbA;
		pbBb = (ProgressBar) findViewById(R.id.vertical_progressbarBb);
		pbBb.setProgress(0);
		barraCores[10] = pbBb;
		pbB = (ProgressBar) findViewById(R.id.vertical_progressbarB);
		pbB.setProgress(0);
		barraCores[11] = pbB;

		// barraCores = {pbC,pbCs,pbD,pbEb,pbE,pbF,pbFs,pbG,pbGs,pbA,pbBb,pbB};

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (asyncTask != null) {
			started = false;
			asyncTask.cancel(true);
			asyncTask = null;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.btnSetSoundGetChord: {
				if (started) {
					started = false;
					asyncTask.cancel(true);
					btnSetSoundGetChord.setImageDrawable(microphoneButton);

				} else {
					System.out.println("Botao para come�ar apertado");
					started = true;
					asyncTask = null;
					asyncTask = new RecordingAndSetChord();
					asyncTask.execute();
					btnSetSoundGetChord
							.setImageResource(drawable.ic_media_pause);

				}
				break;
			}
			}
		}
	};

	private class RecordingAndSetChord extends AsyncTask<Void, float[], Void> {

		@Override
		protected Void doInBackground(Void... params) {

			System.out.println("Do in background!!!");

			while (started) {
				long tIn = System.currentTimeMillis();
				// Inicializando vari�veis para a grava��o
				bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
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
				while (tempoDecorrido <= 800) {
					int result = recorder.read(data, 0, data.length);
					if (result == AudioRecord.ERROR_INVALID_OPERATION) {
						throw new RuntimeException();
					} else if (result == AudioRecord.ERROR_BAD_VALUE) {
						throw new RuntimeException();
					} else {

						baos.write(data, 0, result);
						// amplitude = (data[data.length/2] & 0xff) << 8 |
						// data[data.length/2+1]; 50692
						// amplitude = (data[0] & 0xff) << 8 | data[1];
						// amplitude = Math.abs(amplitude);

					}
					tempoDecorrido = System.currentTimeMillis() - tempoInicio;
				}

				recorder.stop();
				recorder.release();
				recorder = null;

				byte[] byteArrayBaos = baos.toByteArray();

				float[] songInFloat = new float[byteArrayBaos.length / 2];
				for (int j = 0; j < songInFloat.length; j++) {
					songInFloat[j] = ((byteArrayBaos[j * 2] & 0XFF) << 8 | (byteArrayBaos[j * 2 + 1])) / 32768.0F;
				}

				myfft.setByteArraySong(byteArrayBaos);
				float[] S1 = myfft.getS1();

				publishProgress(S1);

				try {

					baos.close();
					baos = null;

				} catch (IOException e) {
					e.printStackTrace();
				}

				/**
				 * Abrindo o arquivo para dizer qual acorde que
				 * �----------------------------------------------
				 */

				long tFIN = System.currentTimeMillis() - tIn;
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(float[]... values) {

			float[] valores = new float[12];
			for (int l = 0; l < values[0].length; l++) {
				valores[l] = values[0][(l + 7) % values[0].length];
			}

			System.out.println("---------------------");
			for (int i = 0; i < 12; i++) {
				// int x = i * 21 + 2;
				int ampli = (int) ((int) 100 * valores[i]);
				barraCores[i].setProgress(ampli);
				// int ampli2 = 100 - ampli;
				// paint.setColor(coresNotas[i]);
				// int j = 0;
				// while (j < 19) {
				// canvas.drawLine(x + j, ampli2, x + j, 100, paint);
				// j++;
				// }

			}

			myfft.setS1(values[0]);

			int numAcorde = myfft.getAcorde();

			chordTV.setText(nomeAcordes[numAcorde]);
			textGradient = new LinearGradient(0, 0, 0, 250, new int[] {
					coresNotas[(int) (numAcorde / 4)], Color.rgb(153, 47, 47) }, new float[] {
					0, 1 }, TileMode.CLAMP);

			chordTV.getPaint().setShader(textGradient);

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}
}
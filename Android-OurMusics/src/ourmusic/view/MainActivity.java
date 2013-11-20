package ourmusic.view;

import ourmusic.androidapp.R;
import ourmusic.model.Player;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Player player = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		player = new Player();
		
		Button buttonRecord = (Button) this.findViewById(R.id.record);
		buttonRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.record();
			}
		});
		
		Button buttonStop = (Button) this.findViewById(R.id.stop);
		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.stop();
			}
		});
		
		Button buttonPlay = (Button) this.findViewById(R.id.play);
		buttonPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.play();
			}
		});
		
		Button buttonPause = (Button) this.findViewById(R.id.pause);
		buttonPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
			}
		});
		
	}
	
	

}

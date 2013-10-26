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
		
		Button btn = (Button) this.findViewById(R.id.buttonRecordingPlaying);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				player = new Player();
				byte[] somEmBytes = player.record();
				player.play(somEmBytes);
			}
		});
		
	}
	
	

}

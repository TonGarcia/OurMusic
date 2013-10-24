package ourmusic.view;

import ourmusic.androidapp.R;
import ourmusic.controler.PlayerControler;
import android.app.Activity;
import android.os.Bundle;

public class PlayerView extends Activity {
	
	private PlayerControler player_controler; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
}

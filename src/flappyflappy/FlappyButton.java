package flappyflappy;

//import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlappyButton {
	
	private ImageView _play;
	private ImageView _retry;
	
	public FlappyButton() {
		_play = new ImageView(new Image("play.png"));
		_retry = new ImageView(new Image("retry.png"));
	}
	
	public ImageView getPlayImage() {
		return _play;
	}
	
	public ImageView getRetryImage() {
		return _retry;
	}
	
	
}

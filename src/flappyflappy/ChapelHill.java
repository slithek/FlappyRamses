package flappyflappy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChapelHill {

	private ImageView _dayImage;
	private ImageView _nightImage;
	private boolean day;
	
	public ChapelHill() {
		_dayImage = new ImageView();
		_nightImage = new ImageView();
		_dayImage.setImage(new Image("chapelHillDay.png"));
		_nightImage.setImage(new Image("chapelHillNight.png"));
		day = (Math.random()<.5);
	}
	
	public Image getImage() {
		if (day) {
			return _dayImage.getImage();
		} else {
			return _nightImage.getImage();
		}
	}
	
}

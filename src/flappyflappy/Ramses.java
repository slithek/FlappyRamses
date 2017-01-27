package flappyflappy;

import java.io.File;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;

public class Ramses {

	private ImageView _ramsesImage;
	
	private double _velocity;
	private static final double GRAVITY = .014; //.014
	private static final double INITIAL_VELOCITY = 0;
	private static final double FLAP_VELOCITY = 7; //7
	private static final double ROTATE_VELOCITY = .45;
	private static final int INITIAL_ROTATION = -18;
	private static final double WEIGHT = .08; //.08
	private static final int HORIZONTAL_LINEANCY = 10;
	private static final int VERTICAL_LINEANCY = 10;
	private Rotate _rotate;
	private Media _flapSound;
	private MediaPlayer _flapPlayer;
	private Media _dieSound;
	private MediaPlayer _diePlayer;
	private Image _ramsesMid;
	private Image _ramsesUp;
	private Image _ramsesDown;
	
	private int _ticksSinceLastFlap;
	
	public Ramses(double y) {
		_ramsesImage = new ImageView();
		_ramsesImage.setY(y);
		_ramsesMid = new Image("ramses.png");
		_ramsesUp = new Image("ramsesUp.png");
		_ramsesDown = new Image("ramsesDown.png");
		_ramsesImage.setImage(_ramsesUp);
		_ramsesImage.setFitWidth(_ramsesImage.getImage().getWidth());
		_ramsesImage.setFitHeight(_ramsesImage.getImage().getHeight());
		_ramsesImage.setRotate(0);
		_velocity = INITIAL_VELOCITY;
		_ticksSinceLastFlap = 0;
		_flapSound = new Media(new File("src/media/bahh.mp3").toURI().toString());
		_flapPlayer = new MediaPlayer(_flapSound);
		_dieSound = new Media(new File("src/media/other.mp3").toURI().toString());
		_diePlayer = new MediaPlayer(_dieSound);
	}
	
	public void bounce() {
		_ramsesImage.setY(_ramsesImage.getY()+_velocity);
		_velocity = INITIAL_VELOCITY;
	}
	
	public void flap() {
		makeFlappingNoise();
		_ticksSinceLastFlap = 0;
		_velocity = _velocity*(WEIGHT)+(1-WEIGHT)*FLAP_VELOCITY;
		_ramsesImage.setRotate(INITIAL_ROTATION + 12);
	}
	
	public void update() {
		_ticksSinceLastFlap++;
		_velocity -= (GRAVITY)*_ticksSinceLastFlap;
		_ramsesImage.setY(_ramsesImage.getY()-_velocity);
		
		rotateRamses();
	}
	
	public ImageView getImage() {
		if (_ramsesImage.getRotate() < -33) {
			_ramsesImage.setImage(_ramsesUp);
		} else if (_ramsesImage.getRotate() < -25) {
			_ramsesImage.setImage(_ramsesMid);
		} else {
			_ramsesImage.setImage(_ramsesDown);
		}
		return _ramsesImage;
	}
	
	public boolean intersects(ImageView iV) {
		return ( (iV.contains(_ramsesImage.getX()+HORIZONTAL_LINEANCY, _ramsesImage.getY()+VERTICAL_LINEANCY))  //Top left
				|| (iV.contains(_ramsesImage.getX()+_ramsesImage.getFitWidth()-HORIZONTAL_LINEANCY, _ramsesImage.getY()+_ramsesImage.getFitHeight()-VERTICAL_LINEANCY))  //Bottom right
				|| (iV.contains(_ramsesImage.getX()+HORIZONTAL_LINEANCY, _ramsesImage.getY()+_ramsesImage.getFitHeight()-VERTICAL_LINEANCY))  //Bottom left
				|| (iV.contains(_ramsesImage.getX()+_ramsesImage.getFitWidth()-HORIZONTAL_LINEANCY, _ramsesImage.getY()+VERTICAL_LINEANCY)) ); //Top right
	}
	
	public void draw(GraphicsContext graphics) {
		graphics.save();
		rotate(graphics, _ramsesImage.getRotate(), _ramsesImage.getX()+_ramsesImage.maxWidth(0)/2.0, _ramsesImage.getY()+_ramsesImage.maxHeight(0)/2.0);
		graphics.drawImage(_ramsesImage.getImage(), _ramsesImage.getX(), _ramsesImage.getY());
		graphics.restore();
	}
	
	private void rotate(GraphicsContext graphics, double angle, double xPivot, double yPivot) {
		_rotate = new Rotate(angle, xPivot, yPivot);
		graphics.setTransform(_rotate.getMxx(), _rotate.getMyx(), _rotate.getMxy(), _rotate.getMyy(), _rotate.getTx(), _rotate.getTy());
	}
	
	public void setX(double x) {
		_ramsesImage.setX(x);
	}
	
	private void rotateRamses() {
		if (_ticksSinceLastFlap == 1) {
			_ramsesImage.setRotate(INITIAL_ROTATION + 6);
		} else if (_ticksSinceLastFlap == 2) {
			_ramsesImage.setRotate(INITIAL_ROTATION);
		} else {
			//Rotate ramses with velocity and prevent him from going haywire
			if (_ramsesImage.getRotate() >= -35 && _ramsesImage.getRotate() <= 90) {
				_ramsesImage.setRotate(_ramsesImage.getRotate()-(_velocity*ROTATE_VELOCITY));
			} else if (_ramsesImage.getRotate() < -35) {
				_ramsesImage.setRotate(-35);
			} else {
				_ramsesImage.setRotate(90);
			}
		}
	}
	
	private void makeFlappingNoise() {
		_flapPlayer.stop();
		_flapPlayer.play();
	}
	
	public void makeDyingSound() {
		_diePlayer.play();
	}
	
	public void fall() {
		_ramsesImage.setRotate(90);
	}
	
}

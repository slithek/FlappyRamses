package flappyflappy;


import javafx.scene.image.ImageView;

public class BellTowerGenerator {

	//Three different heights, rightside up and upside down
	private ImageView _1;
	private ImageView _2;
	private ImageView _3;
	private ImageView _1i;
	private ImageView _2i;
	private ImageView _3i;
	
	private int _rightSideUpHeight;
	
	private double _rand;
	
	public BellTowerGenerator() {
		_1 = new ImageView("tower1.png");
		_2 = new ImageView("tower2.png");
		_3 = new ImageView("tower3.png");
		_1i = new ImageView("tower1i.png");
		_2i = new ImageView("tower2i.png");
		_3i = new ImageView("tower3i.png");
	}
	
	//Wtf method name lol
	public ImageView generateRandomUpsideDownBellTower() {
		//Returns the corresponding upside down tower
		if (_rightSideUpHeight == 3) {
			return new ImageView(_1i.getImage());
		} else if (_rightSideUpHeight == 2) {
			return new ImageView(_2i.getImage());
		} else {
			return new ImageView(_3i.getImage());
		}
	}
	
	public ImageView generateRandomRightsideUpBellTower() {
		_rand = Math.random()*3;
		
		if (_rand<1) {
			_rightSideUpHeight = 1;
			return new ImageView(_1.getImage());
		} else if (_rand<2) {
			_rightSideUpHeight = 2;
			return new ImageView(_2.getImage());
		} else {
			_rightSideUpHeight = 3;
			return new ImageView(_3.getImage()); 
		}
	}
	
}

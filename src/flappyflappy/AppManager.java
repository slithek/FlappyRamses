package flappyflappy;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class AppManager extends AnimationTimer {

	private static int GAME_VELOCITY = 3;
	
	private GraphicsContext _graphics;
	private int _appWidth;
	private int _appHeight;
	private ChapelHill _chapelHill;
	private int _ticks;
	private int _bellTowerFreq;
	private ArrayList<ImageView> _bellTowers;
	private int _velocity;
	private Ramses _ramses;
	private int _score;
	private int _highScore;
	private BellTowerGenerator _bellTowerGenerator;
	private boolean _playerHasControl;
	private boolean _gameOver;
	private FlappyButton _flappyButton;
	private boolean _hasPlayedOnce;
	
	public AppManager(GraphicsContext graphics, int appWidth, int appHeight) {
		_bellTowerGenerator = new BellTowerGenerator();
		_bellTowers = new ArrayList<ImageView>();
		_graphics = graphics;
		_appWidth = appWidth;
		_appHeight = appHeight;
		_ticks = 0;
		_score = 0;
		_graphics.setFont(new Font("Impact", _appHeight*.08));
		_graphics.setTextAlign(TextAlignment.CENTER);
		_ramses = new Ramses(_appHeight/2.0);
		_ramses.setX(_appWidth/2.0-_ramses.getImage().maxWidth(0)/2.0);
		_playerHasControl = false;
		_gameOver = true;
		_flappyButton = new FlappyButton();
		_hasPlayedOnce = false;
		_highScore = 0;
	}

	public void setVelocity(int velocity) {
		_velocity = velocity;
	}
	
	public void setBellTowerFrequency(int bellTowerFreq) {
		_bellTowerFreq = bellTowerFreq;
	}
	
	public int getAppWidth() {
		return _appWidth;
	}
	
	public int getAppHeight() {
		return _appHeight;
	}
	
	public void setBackground(ChapelHill chapelHill) {
		_chapelHill = chapelHill;
	}
	
	public int getScore() {
		return _score/2;
	}
	
	public void handleKeyPressed(KeyEvent e) {
		
		//Flap _ramses if space bar pressed or restart game if ramses is dead
		if (e.getCode().toString().equals("SPACE") && _playerHasControl) {
			_ramses.flap();
		} else if (e.getCode().toString().equals("SPACE") && !_playerHasControl && _gameOver) {
			_ticks = 0;
			_score = 0;
			play();
		}
	}
	
	private void drawBackground() {
		_graphics.drawImage(_chapelHill.getImage(), 0, 0, _appWidth, _appHeight);
	}
	
	private void drawBellTowers() {
		if (_ticks % _bellTowerFreq == 0) {
			//***Must generate rightside up tower first!!!***//
			//(the upside down ones depend on the rightside ones generating first)
			_bellTowers.add(_bellTowerGenerator.generateRandomRightsideUpBellTower());
			_bellTowers.add(_bellTowerGenerator.generateRandomUpsideDownBellTower());
			_bellTowers.get(_bellTowers.size()-2).setX(_appWidth);
			_bellTowers.get(_bellTowers.size()-1).setX(_appWidth);
			_bellTowers.get(_bellTowers.size()-2).setY(_appHeight-_bellTowers.get(_bellTowers.size()-2).maxHeight(0));
			_bellTowers.get(_bellTowers.size()-1).setY(0);
		}
		for (int i=0; i<_bellTowers.size(); i++) { //Using a for each creates weird concurrentmodification issues
			_graphics.drawImage(_bellTowers.get(i).getImage(), _bellTowers.get(i).getX(), _bellTowers.get(i).getY());
			_bellTowers.get(i).setX(_bellTowers.get(i).getX()-_velocity);
			if (_bellTowers.get(i).getX()+_bellTowers.get(i).maxWidth(0)*.5 < _appWidth/2.0+2 && _bellTowers.get(i).getX()+_bellTowers.get(i).maxWidth(0)*.5 >= _appWidth/2.0-2 && _playerHasControl) {
				_score++; //Increment score if bell towers cross midway point
			}
			if (_bellTowers.get(i).getX()<(0-_bellTowers.get(i).maxWidth(0))) {
				_bellTowers.remove(i);  //Remove bell tower from list if it leaves the screen
			}
		}
	}
	
	private void drawRamses() {
		if (_ramses.getImage().getY()>0 && _ramses.getImage().getY()<_appHeight-_ramses.getImage().maxHeight(0)) {
			_ramses.update();
		} else if (_ramses.getImage().getY()>0) {
			endGame();
		} else if (_ramses.getImage().getY()<_appHeight-_ramses.getImage().maxHeight(0)) {
			_ramses.bounce();
		}
		_ramses.draw(_graphics);
	}
	
	private void checkForCollision() {
		for (int i=0; i<_bellTowers.size(); i++) {
			if (_ramses.intersects(_bellTowers.get(i))) {
				_velocity = 0;
				_playerHasControl = false;
			}
		}
	}
	
	private void displayScore() {
		_graphics.setFill(Color.BLACK);
		_graphics.fillText(""+this.getScore(), _appWidth*.5+4, _appHeight*.2+4);
		_graphics.setFill(Color.WHITE);
		_graphics.fillText(""+this.getScore(), _appWidth*.5, _appHeight*.2);
	}
	
	private void gameOver() {
		_graphics.setFill(Color.BLACK);
		_graphics.fillText("Game Over", _appWidth*.5+4, _appHeight*.48+4);
		_graphics.setFill(Color.WHITE);
		_graphics.fillText("Game Over", _appWidth*.5, _appHeight*.48);
		if (getScore() > _highScore) {
			highScore(true);
		} else {
			highScore(false);
		}
	}
	
	private void highScore(boolean newHighScore) {
		_graphics.setFont(new Font("Impact", _appHeight*.06));
		if (newHighScore) {
			_highScore = getScore();
			_graphics.setFill(Color.BLACK);
			_graphics.fillText("New High Score: "+_highScore, _appWidth*.5+4, _appHeight*.33+4);
			_graphics.setFill(Color.YELLOW);
			_graphics.fillText("New High Score: "+_highScore, _appWidth*.5, _appHeight*.33);
		} else {
			_graphics.setFill(Color.BLACK);
			_graphics.fillText("High Score: "+_highScore, _appWidth*.5+4, _appHeight*.33+4);
			_graphics.setFill(Color.WHITE);
			_graphics.fillText("High Score: "+_highScore, _appWidth*.5, _appHeight*.33);
		}
		_graphics.setFont(new Font("Impact", _appHeight*.08));
	}
	
	private void tick() {
		_ticks++;
	}
	
	public void play() {
		_bellTowerGenerator = new BellTowerGenerator();
		_bellTowers = new ArrayList<ImageView>();
		_ramses = new Ramses(_appHeight/2.0);
		_ramses.getImage().setX(_appWidth/2.0-_ramses.getImage().maxWidth(0)/2.0);
		_chapelHill = new ChapelHill();
		_hasPlayedOnce = true;
		_gameOver = false;
		_ramses.flap();
		_playerHasControl = true;
		_velocity = GAME_VELOCITY;
		this.start();
	}

	@Override
	public void handle(long currentNanoTime) {
		
		drawBackground();

		drawBellTowers();

		drawRamses();

		checkForCollision();

		displayScore();

		tick();
	}
	
	public void initialize() {
		drawBackground();
		_graphics.drawImage(_flappyButton.getPlayImage().getImage(), _appWidth/2.0-_flappyButton.getPlayImage().maxWidth(0)/2.0, _appHeight/2.0-_flappyButton.getPlayImage().maxHeight(0)/2.0);
		_graphics.getCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
	}
	
	private void handleMouseClicked(MouseEvent e) {
		if (e.getX() <= _appWidth /2.0 + _flappyButton.getPlayImage().maxWidth(0)/2.0 
				&& e.getX() >= _appWidth/2.0 - _flappyButton.getPlayImage().maxWidth(0)/2.0
				&& e.getY() >= _appHeight/2.0 - _flappyButton.getPlayImage().maxHeight(0)/2.0
				&& e.getY() <= _appHeight/2.0 + _flappyButton.getPlayImage().maxHeight(0)/2.0
				&& !_playerHasControl
				&& _gameOver
				&& !_hasPlayedOnce) {
			_ticks = 0;
			_score = 0;
			play();
		} else if (e.getX() <= _appWidth /2.0 + _flappyButton.getRetryImage().maxWidth(0)/2.0 
				&& e.getX() >= _appWidth/2.0 - _flappyButton.getRetryImage().maxWidth(0)/2.0
				&& e.getY() >= _appHeight*.6
				&& e.getY() <= _appHeight*.6 + _flappyButton.getRetryImage().maxHeight(0)
				&& !_playerHasControl
				&& _gameOver) {
			_ticks = 0;
			_score = 0;
			play();
		}
	}
	
	public void endGame() {
		stop();
		gameOver();
		
		_playerHasControl = false;
		_gameOver = true;
		_ramses.makeDyingSound();
		
		_graphics.drawImage(_flappyButton.getRetryImage().getImage(), _appWidth/2.0-_flappyButton.getRetryImage().maxWidth(0)/2.0, _appHeight*.6);
		_graphics.getCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
	}
	
}

package flappyflappy;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class AppRunner extends Application {
	
	private AppManager _appManager;
	
	static final int HEIGHT = 600;
	static final int WIDTH = 800;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		//Give the app a title
		stage.setTitle("Flappy Ramses");
		
		//Create a new scene in root group
		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		//Construct a new canvas and give it width and height, add to scene
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
		
		//Create graphics context
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		
		//Construct _appManager and set appWidth/appHeight
		_appManager = new AppManager(graphics, WIDTH, HEIGHT);
		
		//Create Chapel Hill and set the frequency at which bell towers are generated
		ChapelHill chapelHill = new ChapelHill();
		_appManager.setBackground(chapelHill);
		_appManager.setBellTowerFrequency(110); //CPU Hat
		
		//Initialize App Manager
		_appManager.initialize();
		
		//KeyEvent Handler
		scene.setOnKeyPressed(_appManager::handleKeyPressed);
		
		//Show the stage
		stage.show();
	}
	
	
}

package enemies;

import javafx.scene.paint.Color;

public class Rocket extends Enemy{
	private static final double SPEED = 10.0;
	
	public Rocket(double sceneWidth, double sceneHeight) {
		super(sceneWidth, sceneHeight);
		this.getObject().setFill(Color.RED);
	}
	
	@Override
	protected double getSpeed() {
		return SPEED;
	}
}

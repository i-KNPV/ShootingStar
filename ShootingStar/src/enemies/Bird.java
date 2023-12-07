package enemies;

import javafx.scene.paint.Color;

public class Bird extends Enemy{
	private static final double SPEED = 3.0;
	
	public Bird(double sceneWidth, double sceneHeight) {
		super(sceneWidth, sceneHeight);
		this.getObject().setFill(Color.ORANGE);
	}
	
	@Override
	protected double getSpeed() {
		return SPEED;
	}
}

package creatures;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Creature {
	protected Body body;
	protected Vector2 moveDir;
	protected float accel_force;
	protected float friction_ground;
	protected float density;
	protected float friction;
	protected float restitution;
	protected float size;
	
	// stats
	protected float strength;
	protected float intelligence;
	protected float dexterity;
	
	public abstract void update();
}

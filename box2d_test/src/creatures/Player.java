package creatures;

import static handlers.B2DVars.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Player extends Creature {

	public Player(World world, Vector2 pos){
		
		this.size = 8f /PPM; // radius of circle
		this.density = 4f; // kilograms?
		this.friction = 1f; // 0 -> 1
		this.restitution = 0.6f; // 'bounciness' 0 -> 1+
		this.accel_force = 1; // depends on density
		this.friction_ground = 5f; // 0 -> 1 

		this.moveDir = new Vector2(0, 0);
		
		this.strength = 1f;
		this.intelligence = 1f;
		this.dexterity = 1f;

		this.body = createBody(world, pos);
	}
	
	private Body createBody(World world, Vector2 pos) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos.cpy().scl(1/PPM));
		bdef.type = BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(size);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = density;
		fdef.friction = friction;
		fdef.restitution = restitution;
		
		Body b = world.createBody(bdef);
		b.createFixture(fdef);
		return b;
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	// adds to the normalized moveDir vector
	public void addMoveDir(Vector2 dir) {
		moveDir.add(dir);
	}

	public void update() {
		// movement
		body.applyForceToCenter(moveDir.cpy().nor().scl(accel_force), true);
		body.setLinearDamping(friction_ground);
		body.setAngularDamping(friction_ground);
	}

}

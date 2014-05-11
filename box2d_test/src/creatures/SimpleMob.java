package creatures;

import static handlers.B2DVars.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SimpleMob extends Creature {

	public SimpleMob(World world, Vector2 pos){
		this.size = 7f /PPM; // radius of circle
		this.density = 3f; // kilograms?
		this.friction = 1f; // 0 -> 1
		this.restitution = 0.6f; // 'bounciness' 0 -> 1+
		this.accel_force = 1; // depends on density
		this.friction_ground = 1f; // 0 -> 1 

		this.moveDir = new Vector2(0, 0);
		
		this.strength = 2f;
		this.intelligence = 0.3f;
		this.dexterity = 0.6f;

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
	
	public void update() {
		body.setLinearDamping(friction_ground);
		body.setAngularDamping(friction_ground);
	}

}

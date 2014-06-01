package creatures;

import static handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Creature {

	private ShapeRenderer sr;

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
		
		this.sr = new ShapeRenderer();
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
		b.setUserData("player");
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

	public void draw(OrthographicCamera b2dCam) {
		Vector2 pos = body.getPosition();

		sr.setProjectionMatrix(b2dCam.combined);
		sr.begin(ShapeType.Filled);
		sr.identity();
		sr.setColor(Color.GREEN);
		sr.circle(pos.x,pos.y, size, 10);
		sr.end();
	}

}

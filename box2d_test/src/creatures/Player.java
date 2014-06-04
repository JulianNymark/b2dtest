package creatures;

import static handlers.B2DVars.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Creature {

	private static final float VISION_DISTANCE = 100 / PPM;
	private ShapeRenderer sr;
	private OrthographicCamera b2dCam;
	private RayCastCallback callback;
	private ArrayList<Vector2> vision_rays;

	public Player(World world, OrthographicCamera b2dCam, Vector2 pos) {

		this.size = 8f / PPM; // radius of circle
		this.density = 4f; // kilograms?
		this.friction = 1f; // 0 -> 1
		this.restitution = 0.6f; // 'bounciness' 0 -> 1+
		this.accel_force = 1; // depends on density
		this.friction_ground = 5f; // 0 -> 1

		this.moveDir = new Vector2(0, 0);

		this.strength = 1f;
		this.intelligence = 1f;
		this.dexterity = 1f;

		this.world = world;
		this.b2dCam = b2dCam;
		this.body = createBody(pos);

		this.sr = new ShapeRenderer();

		this.callback = new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point,
					Vector2 normal, float fraction) {
				vision_rays.add(point);
				return 0;
			}
		};

		this.vision_rays = new ArrayList<Vector2>();
	}

	private Body createBody(Vector2 pos) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos.cpy().scl(1 / PPM));
		bdef.type = BodyType.DynamicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(size);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = density;
		fdef.friction = friction;
		fdef.restitution = restitution;

		Body b = this.world.createBody(bdef);
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

		// vision (rayCast)
		cast_rays();
	}

	private void cast_rays() {
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		Vector3 mouse_world = b2dCam.unproject(new Vector3(mouse, 0));
		world.rayCast(callback, getPosition(), new Vector2(mouse_world.x,
				mouse_world.y));
	}

	public void draw() {
		Vector2 pos = body.getPosition();
		sr.setProjectionMatrix(this.b2dCam.combined);
		
		// draw vision
		sr.begin(ShapeType.Line);
		sr.identity();
		sr.setColor(Color.WHITE);
		for (Vector2 v : vision_rays){
			//Vector3 v_cam = b2dCam.project(new Vector3(v.x, v.y, 0));
			sr.line(pos, v);
		}
		vision_rays.clear();
		sr.end();
		
		// draw player
		sr.begin(ShapeType.Filled);
		sr.identity();
		sr.setColor(Color.GREEN);
		sr.circle(pos.x, pos.y, size, 10);
		sr.end();
	}

}

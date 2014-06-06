package creatures;

import static handlers.B2DVars.PPM;
import interfaces.IVisible;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import data_containers.VisionData;

public class Player extends Creature implements IVisible {
	private static final float VISION_DISTANCE = 2000 / PPM;
	private static final int RAY_COUNT = 1500;

	private ShapeRenderer sr;
	private OrthographicCamera b2dCam;

	private RayCastCallback callback;
	private HashMap<Integer, VisionData> vision_rays;
	private int current_i;

	public Player(World world, OrthographicCamera b2dCam, Vector2 pos) {

		this.size = 8f / PPM; // radius of circle
		this.density = 4f; // kilograms?
		this.friction = 0.3f; // 0 -> 1
		this.restitution = 0.3f; // 'bounciness' 0 -> 1+
		this.accel_force = 1.2f; // depends on density
		this.friction_ground = 3f;

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
				if (!vision_rays.containsKey(current_i)) {
					vision_rays.put(current_i, new VisionData(point.cpy(),
							fixture));
				} else if (vision_rays.get(current_i).point.dst(getPosition()) > point
						.dst(getPosition())) {
					vision_rays.put(current_i, new VisionData(point.cpy(),
							fixture));
				}
				return 1;
			}
		};

		this.vision_rays = new HashMap<Integer, VisionData>();
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
		b.setUserData(this);
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
		// Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		// Vector3 mouse_world = b2dCam.unproject(new Vector3(mouse, 0));
		// world.rayCast(callback, getPosition(), new Vector2(mouse_world.x,
		// mouse_world.y));
		double rot_step = (Math.PI * 2) / RAY_COUNT;
		for (int i = 0; i < RAY_COUNT; ++i) {
			double rot_amount = rot_step * i;
			Vector2 v = new Vector2(0, 1).scl(VISION_DISTANCE);
			v.rotateRad((float) rot_amount);
			v.add(getPosition());

			current_i = i;
			world.rayCast(callback, getPosition(), v);
		}

		// set visibility of visible things that are struck
		for (int i = 0; i < RAY_COUNT; ++i){
			if (vision_rays.containsKey(i)){
				Fixture f = vision_rays.get(i).fixture;
				IVisible v = (IVisible) f.getBody().getUserData();
				v.setVisible(true);
			}
		}

	}

	public void draw() {
		Vector2 pos = body.getPosition();
		sr.setProjectionMatrix(this.b2dCam.combined);

		// draw vision
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Line);
		sr.identity();
		sr.setColor(1, 1, 1, 0.1f);
		Gdx.gl20.glLineWidth(10); // TODO it's like, capped at 5 pixels... D:
		for (Integer i : vision_rays.keySet()) {
			sr.line(pos, vision_rays.get(i).point);
		}

		double rot_step = (Math.PI * 2) / RAY_COUNT;
		for (int i = 0; i < RAY_COUNT; ++i) {
			if (!vision_rays.containsKey(i)) {
				double rot_amount = rot_step * i;
				Vector2 v = new Vector2(0, 1).scl(VISION_DISTANCE);
				v.rotateRad((float) rot_amount);
				v.add(getPosition());
				sr.line(pos, v);
			}
		}
		sr.end();
		
		vision_rays.clear();
		
		Gdx.gl20.glLineWidth(1);

		// draw player
		sr.begin(ShapeType.Filled);
		sr.identity();
		sr.setColor(Color.GREEN);
		sr.circle(pos.x, pos.y, size, 10);
		sr.end();
	}

	@Override
	public void setVisible(boolean v) {
		this.visible = v;
	}

}

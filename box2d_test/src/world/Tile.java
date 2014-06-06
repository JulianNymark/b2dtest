package world;

import static handlers.B2DVars.PPM;
import interfaces.IVisible;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tile implements IVisible {
	public static final float TILESIZE = 200 / PPM;

	public Body b;
	private boolean visible;
	// TODO texture / image file?

	private ShapeRenderer sr;

	private World world;
	
	public Tile(World world, Vector2 vector2, ShapeRenderer sr) {
		this.visible = false;
		this.sr = sr;
		this.world = world;
		this.b = createBody(world, vector2);
	}

	@Override
	public void setVisible(boolean v) {
		this.visible = v;
	}
	
	public void draw(OrthographicCamera b2dCam){
		if (visible){
			sr.setProjectionMatrix(b2dCam.combined);
			sr.begin(ShapeType.Filled);
			sr.identity();

			Vector2 pos = b.getPosition();
			sr.setColor(0.3f, 0.3f, 0.3f, 1);
			sr.rect(pos.x - TILESIZE / 2, pos.y - TILESIZE / 2, TILESIZE,
					TILESIZE);

			sr.end();
			
			visible = false;
		}
	}
	
	private Body createBody(World world, Vector2 pos) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos.cpy().scl(1 / PPM));
		bdef.type = BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(TILESIZE*0.5f, TILESIZE*0.5f); // setAsBox uses halfWidth, halfHeight

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.friction = 0.7f;
		fdef.restitution = 0.5f;

		Body b = world.createBody(bdef);
		b.createFixture(fdef);
		b.setUserData(this);
		return b;
	}
}

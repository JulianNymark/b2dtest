package world;

import static handlers.B2DVars.PPM;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
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

public class Map {
	public static final float TILESIZE = 200 / PPM;
	
	Random r;
	World world;
	ArrayList<Body> tiles;
	private ShapeRenderer sr;

	public Map(World world, Random r) {
		this.r = r;
		this.world = world;

		tiles = new ArrayList<Body>();

		this.sr = new ShapeRenderer();
	}

	public void genTiles() {
		for (int i = 0; i < 3000; ++i) {
			Body b = createBody(world, new Vector2(
					r.nextFloat() * 10000 - 5000, r.nextFloat() * 10000 - 5000));
			tiles.add(b);
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
		b.setUserData("wall");
		return b;
	}

	// TODO optimize, draw only what camera sees
	public void draw(OrthographicCamera b2dCam) {
		
		sr.setProjectionMatrix(b2dCam.combined);
		sr.begin(ShapeType.Filled);
		sr.identity();
			
		for (Body b : tiles) {
			Vector2 pos = b.getPosition(); 
			sr.setColor(Color.BLACK);
			sr.rect(pos.x - TILESIZE/2, pos.y - TILESIZE/2, TILESIZE, TILESIZE);
		}

		sr.end();

	}
}

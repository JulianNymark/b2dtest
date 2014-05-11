package states;

import static handlers.B2DVars.PPM;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.PlayInputProcessor;

import java.util.ArrayList;
import java.util.Random;

import main.Game;
import main.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import creatures.Creature;
import creatures.Player;
import creatures.SimpleMob;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;

	private OrthographicCamera b2dCam;

	private Random r;

	// creatures
	private Player player;
	private ArrayList<SimpleMob> mobs;

	public Play(GameStateManager gsm) {
		super(gsm);
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new MyContactListener());
		b2dr = new Box2DDebugRenderer();

		// seed random
		r = new Random(42l);

		// create player
		player = new Player(world, new Vector2(0, 0));

		// create some mobs
		mobs = new ArrayList<SimpleMob>();
		for (int i = 0; i < 10; ++i) {
			SimpleMob mob = new SimpleMob(world, new Vector2(
					50 + r.nextFloat() * 50, r.nextFloat() * 50));
			mobs.add(mob);
		}

		// create cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.WIDTH / PPM, Game.HEIGHT / PPM);
		
		// input processor
		PlayInputProcessor inputProcessor = new PlayInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);

	}

	@Override
	public void update(float dt) {
		// player movement
		player.update();
		
		// update mobs
		for (Creature m : mobs){
			m.update();
		}
		world.step(dt, 6, 2);
	}

	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// camera position
		Vector2 mouse = new Vector2(Gdx.input.getX() / PPM, Gdx.input.getY() / PPM);
		mouse.y = Game.HEIGHT / PPM - mouse.y;
		Vector2 center = new Vector2(Game.WIDTH / PPM / 2, Game.HEIGHT / PPM / 2);
		Vector2 mouseOffsetCenter = mouse.cpy();
		mouseOffsetCenter.sub(center.cpy());
		b2dCam.position.set(player.getPosition().add(mouseOffsetCenter.scl(Settings.MOUSE_VIEW_STICKINESS)), 1);
		b2dCam.update();


		// draw box2d world
		b2dr.render(world, b2dCam.combined);
	}

	@Override
	public void dispose() {
	}

	public Player getPlayer() {
		return player;
	}

}

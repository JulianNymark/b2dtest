package main;

import handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener{

	public static final String TITLE = "b2dTest!!!!";
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	
	public static final float STEP = 1/60f;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;
	private float time_accumulative;
	
	@Override
	public void create() {
		sb = new SpriteBatch();
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, WIDTH, HEIGHT);
		gsm = new GameStateManager(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		time_accumulative += Gdx.graphics.getDeltaTime();
		while (time_accumulative >= STEP){
			time_accumulative -= STEP;
			gsm.update(STEP);
			gsm.render();
		}
	}

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
	
	// probably won't need
	public void pause() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}

}

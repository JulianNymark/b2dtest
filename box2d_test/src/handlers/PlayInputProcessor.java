package handlers;

import states.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import creatures.Player;

public class PlayInputProcessor implements InputProcessor {

	Play play;
	
	public PlayInputProcessor(Play play) {
		this.play = play;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		Player p = play.getPlayer();
		
		switch (keycode) {
		case Input.Keys.ESCAPE:
			Gdx.app.exit();
			break;
		case Input.Keys.COMMA:
			p.addMoveDir(new Vector2(0, 1));
			break;
		case Input.Keys.A:
			p.addMoveDir(new Vector2(-1, 0));
			break;
		case Input.Keys.O:
			p.addMoveDir(new Vector2(0, -1));
			break;
		case Input.Keys.E:
			p.addMoveDir(new Vector2(1, 0));
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Player p = play.getPlayer();
		
		switch (keycode) {
		case Input.Keys.COMMA:
			p.addMoveDir(new Vector2(0, -1));
			break;
		case Input.Keys.A:
			p.addMoveDir(new Vector2(1, 0));
			break;
		case Input.Keys.O:
			p.addMoveDir(new Vector2(0, 1));
			break;
		case Input.Keys.E:
			p.addMoveDir(new Vector2(-1, 0));
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		if (button == 1) {
//			tb.mouseDown = new Vector2(screenX, TestBox2d.H - screenY);
//		}
//		if (button == 0) {
//			tb.player.aimedProjectile("Fireball", 1000);
//		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		if (button == 1) {
//			tb.mouseDown = null;
//		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (amount == 1) {
			play.ZOOM_LEVEL += 0.05;
		} else {
			play.ZOOM_LEVEL -= 0.05;
		}
		return false;
	}
}

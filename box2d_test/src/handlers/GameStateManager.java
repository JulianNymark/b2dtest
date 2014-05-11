package handlers;

import java.util.Stack;

import main.Game;
import states.GameState;
import states.Play;

public class GameStateManager {

	private Game game;

	private Stack<GameState> gamestates;

	public static final int PLAY = 0;

	public GameStateManager(Game game) {
		this.game = game;
		this.gamestates = new Stack<GameState>();
		pushState(PLAY);
	}

	public Game game() {
		return game;
	}

	public void update(float dt) {
		gamestates.peek().update(dt);
	}

	public void render() {
		gamestates.peek().render();
	}

	private GameState getState(int state) {
		if (state == PLAY) {
			return new Play(this);
		}
		return null;
	}

	public void setState(int state) {
		popState();
		pushState(state);
	}

	public void pushState(int state) {
		gamestates.push(getState(state));
	}

	public void popState() {
		GameState g = gamestates.pop();
		g.dispose();
	}
}

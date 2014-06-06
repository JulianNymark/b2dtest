package world;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
	Random r;
	World world;
	ArrayList<Tile> tiles;
	
	private ShapeRenderer sr;

	public Map(World world, Random r) {
		this.r = r;
		this.world = world;
		this.sr = new ShapeRenderer();

		this.tiles = new ArrayList<Tile>();
	}

	public void genTiles() {
		for (int i = 0; i < 2000; ++i) {
			Tile t = new Tile(world, new Vector2(r.nextFloat() * 10000 - 5000,
					r.nextFloat() * 10000 - 5000), sr);
			tiles.add(t);
		}
	}

	// TODO optimize, draw only what camera sees
	public void draw(OrthographicCamera b2dCam) {
		for (Tile t : tiles) {
			t.draw(b2dCam);
		}
	}
}

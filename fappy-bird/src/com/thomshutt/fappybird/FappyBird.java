package com.thomshutt.fappybird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.thomshutt.fappybird.drawable.Background;
import com.thomshutt.fappybird.drawable.Bird;
import com.thomshutt.fappybird.drawable.PipeFactory;

import java.util.ArrayList;
import java.util.List;

public class FappyBird implements ApplicationListener {

    public static final String IMAGE_BIRD = "data/bird.png";
    private static final String IMAGE_BACKGROUND = "data/background.png";
    private static final String IMAGE_FLOOR = "data/floor.png";
    private static final String IMAGE_PIPE = "data/pipe.png";
    private static final String IMAGE_PIPE_TOP = "data/pipe_top.png";
    private Bird bird;

    private static enum STATES {RUNNING, LOST};

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private List<Drawable> drawables;
    private STATES state = STATES.RUNNING;

    @Override
	public void create() {
		batch = new SpriteBatch();
        drawables = new ArrayList<Drawable>();
        bird = new Bird(Gdx.files.internal(IMAGE_BIRD));

        drawables.add(new Background(Gdx.files.internal(IMAGE_BACKGROUND), 80));
        drawables.add(new PipeFactory(Gdx.files.internal(IMAGE_PIPE), Gdx.files.internal(IMAGE_PIPE_TOP)));
        drawables.add(bird);
        drawables.add(new Background(Gdx.files.internal(IMAGE_FLOOR), 200));
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		doUpdate();
        doRender();
	}

    private void doUpdate(){
        if(this.state == STATES.LOST){
            return;
        }
        for (Drawable drawable : drawables) {
            drawable.tick(Gdx.graphics.getDeltaTime());
            if(Gdx.input.justTouched()) { drawable.screenTouched(); }
            if(drawable.isInCollisionWithBird(bird)){
                this.state = STATES.LOST;
            }
        }
    }

    private void doRender(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Drawable drawable : drawables) {
            drawable.draw(batch);
        }
        batch.end();
    }

	@Override
	public void resize(int width, int height) {
        camera = new OrthographicCamera(width, height);
        for (Drawable drawable : drawables) {
            drawable.resize(width, height);
        }
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

}

package com.thomshutt.fappybird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private static final String IMAGE_BIRD = "data/bird.png";
    private static final String IMAGE_BACKGROUND = "data/background.png";
    private static final String IMAGE_FLOOR = "data/floor.png";
    private static final String IMAGE_PIPE = "data/pipe.png";
    private static final String IMAGE_PIPE_TOP = "data/pipe_top.png";
    private static final int SCROLL_SPEED_FLOOR = 200;
    private static final int SCROLL_SPEED_BACKGROUND = 80;

    private Bird bird;
    private Background floor;
    private int width;
    private int height;

    private static enum STATES {RUNNING, LOST, DEATH_THROES};

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private List<Drawable> drawables;
    private STATES state = STATES.RUNNING;

    @Override
	public void create() {
        this.batch = new SpriteBatch();
        this.drawables = new ArrayList<Drawable>();
        this.bird = new Bird(Gdx.files.internal(IMAGE_BIRD));
        this.floor = new Background(Gdx.files.internal(IMAGE_FLOOR), SCROLL_SPEED_FLOOR);

        drawables.add(new Background(Gdx.files.internal(IMAGE_BACKGROUND), SCROLL_SPEED_BACKGROUND));
        drawables.add(new PipeFactory(Gdx.files.internal(IMAGE_PIPE), Gdx.files.internal(IMAGE_PIPE_TOP)));
        drawables.add(bird);
        drawables.add(floor);
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
            if(inputTouched()){
                this.create();
                this.resize(this.width, this.height);
                this.state = STATES.RUNNING;
            }
            return;
        }
        if(this.state == STATES.DEATH_THROES){
            bird.tick(Gdx.graphics.getDeltaTime());
        } else {
            for (Drawable drawable : drawables) {
                drawable.tick(Gdx.graphics.getDeltaTime());
                if(inputTouched()) { drawable.screenTouched(); }
                if(drawable.isInCollisionWithBird(bird)){
                    this.state = STATES.DEATH_THROES;
                }
            }
        }
        if(floor.isInCollisionWithBird(bird)){
            this.state = STATES.LOST;
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
        this.width = width;
        this.height = height;
        camera = new OrthographicCamera(width, height);
        for (Drawable drawable : drawables) {
            drawable.resize(width, height);
        }
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

    private boolean inputTouched(){
        return Gdx.input.justTouched()
                || Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyPressed(Input.Keys.UP);
    }

}

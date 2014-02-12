package com.thomshutt.fappybird.drawable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.thomshutt.fappybird.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeFactory implements Drawable {

    private static final int PIPE_INTERVAL_PIXELS = 400;
    private static final int MIN_PIPES_IN_MEMORY = 5;
    private static final int MIN_PIPE_Y = 200;
    private static final int MAX_PIPE_Y = 380;

    private final Texture texturePipe;
    private final Texture texturePipeTop;

    private float screenWidth;
    private float screenHeight;
    private float pipeWidth;
    private float textureScale;

    private float pipeHeight;
    private List<Pipe> pipes = new ArrayList<Pipe>();

    private float pipeGap = 0;

    private final Random random = new Random(System.currentTimeMillis());

    public PipeFactory(FileHandle imageBottom, FileHandle imageTop) {
        this.texturePipe = new Texture(imageBottom);
        this.texturePipeTop = new Texture(imageTop);
        this.pipes.add(new Pipe(2 * PIPE_INTERVAL_PIXELS, generatePipeY()));
    }

    private int generatePipeY(){
        return -(this.random.nextInt(MAX_PIPE_Y - MIN_PIPE_Y) + MIN_PIPE_Y);
    }

    @Override
    public void tick(float deltaTime) {
        for (Pipe pipe : pipes) {
            pipe.x -= 200f * deltaTime;
        }
        while(this.pipes.size() > 0 && this.pipes.get(0).hasMovedOffScreen(this.screenWidth)){
            this.pipes.remove(0);
        }
        while(this.pipes.size() < MIN_PIPES_IN_MEMORY){
            this.pipes.add(new Pipe(this.pipes.get(this.pipes.size() - 1).x + PIPE_INTERVAL_PIXELS, generatePipeY()));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Pipe pipe : this.pipes) {
            batch.draw(this.texturePipe, pipe.x, pipe.y, pipeWidth, pipeHeight);
            batch.draw(this.texturePipeTop, pipe.x, pipe.y + this.pipeHeight + pipeGap, pipeWidth, pipeHeight);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.pipeWidth = this.screenHeight / 10;
        this.textureScale = this.pipeWidth / this.texturePipe.getWidth() ;
        this.pipeHeight = this.texturePipe.getHeight() * textureScale;
        this.pipeGap = (this.screenHeight / 10) * 4;
    }

    @Override
    public void screenTouched() {
        // Pipes don't care about the screen being touched
    }

    @Override
    public boolean isInCollisionWithBird(Bird bird) {
        for (Pipe pipe : pipes) {
            if(Intersector.intersectRectangles(bird.getRectangle(), pipe.getRectangle(pipeWidth, pipeHeight), new Rectangle())) return true;
        }
        return false;
    }

    private class Pipe {
        float x;
        float y;

        private Pipe(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Rectangle getRectangle(float pipeWidth, float pipeHeight) {
            return new Rectangle(this.x, this.y, pipeWidth, pipeHeight);
        }

        public boolean hasMovedOffScreen(float screenWidth) {
            return this.x < -screenWidth;
        }

        @Override
        public String toString() {
            return "Pipe{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

    }

}

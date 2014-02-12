package com.thomshutt.fappybird.drawable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.thomshutt.fappybird.Drawable;

import java.util.ArrayList;
import java.util.List;

public class PipeFactory implements Drawable {

    private final Texture texturePipe;
    private final Texture texturePipeTop;

    private float screenWidth;
    private float screenHeight;
    private float pipeWidth;
    private float textureScale;
    private float pipeHeight;

    private List<Pipe> pipes = new ArrayList<Pipe>();
    private float pipeGap = 0;

    public PipeFactory(FileHandle imageBottom, FileHandle imageTop) {
        this.texturePipe = new Texture(imageBottom);
        this.texturePipeTop = new Texture(imageTop);
        this.pipes.add(new Pipe(2700, -300));
        this.pipes.add(new Pipe(2300, -260));
        this.pipes.add(new Pipe(1900, -220));
        this.pipes.add(new Pipe(1500, -260));
        this.pipes.add(new Pipe(1100, -230));
    }

    @Override
    public void tick(float deltaTime) {
        for (Pipe pipe : pipes) {
            pipe.x -= 200f * deltaTime;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Pipe pipe : pipes) {
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
    }

}

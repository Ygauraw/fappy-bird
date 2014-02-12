package com.thomshutt.fappybird.drawable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.thomshutt.fappybird.Drawable;

public class Bird implements Drawable {

    private float velocityY = 0;
    private float y = 0;

    private static final int GRAVITY = 25;

    private Texture textureBird;
    private Sprite spriteBird;
    private float x;
    private float birdHeight;
    private float birdWidth;

    public Bird(FileHandle image) {
        textureBird = new Texture(image);
        textureBird.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteBird = new Sprite(new TextureRegion(textureBird, 0, 0, 128, 128));
    }

    @Override
    public void tick(final float deltaTime){
        velocityY += GRAVITY * deltaTime;
        y = y - (0.3f * velocityY);
    }

    @Override
    public void screenTouched(){
        velocityY = -0.6f * GRAVITY;
    }

    @Override
    public void draw(SpriteBatch batch) {
        spriteBird.setPosition(this.x, this.y);
        spriteBird.setRotation(getTiltDegrees());
        spriteBird.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        this.birdWidth = height / 10f;
        this.birdHeight = height / 10f;
        this.x = -(width / 4);
        spriteBird.setSize(birdWidth, birdHeight);
        spriteBird.setOrigin(spriteBird.getWidth() / 2, spriteBird.getHeight() / 2);
    }

    @Override
    public boolean isInCollisionWithBird(Bird bird) {
        return false;
    }

    private float getTiltDegrees() {
        if(this.velocityY < 0) return 25;
        if(this.velocityY > 0) return Math.max(-80, -this.velocityY);
        return 0;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public Rectangle getRectangle(){
        return new Rectangle(this.x, this.y, this.birdWidth, this.birdHeight);
    }

}

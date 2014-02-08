package com.thomshutt.fappybird.drawable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thomshutt.fappybird.Drawable;

public class Bird implements Drawable {

    private float velocityY = 0;
    private float y = 0;

    private static final int GRAVITY = 25;

    private Texture textureBird;
    private Sprite spriteBird;

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
        spriteBird.setPosition(-spriteBird.getWidth()/2, -spriteBird.getHeight()/2 + this.y);
        spriteBird.setRotation(getTiltDegrees());
        spriteBird.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        spriteBird.setSize(height / 10, height / 10);
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
}

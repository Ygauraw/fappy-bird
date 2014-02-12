package com.thomshutt.fappybird.drawable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thomshutt.fappybird.Drawable;

public class Background implements Drawable {

    private final Texture textureBackground;
    private final int speed;

    private float scrollingX = 0;
    private float canvasHeight;
    private float canvasWidth;
    private float backgroundY;
    private float textureScale;
    private float textureWidthScaled;
    private float floorHeight;

    public Background(FileHandle image, int speed) {
        this.textureBackground = new Texture(image);
        this.textureBackground.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.speed = speed;
    }

    @Override
    public void tick(float deltaTime) {
        scrollingX -= (deltaTime * speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(textureBackground, scrollingX, backgroundY, this.textureWidthScaled, this.canvasHeight);
        batch.draw(textureBackground, scrollingX - this.textureWidthScaled, backgroundY, this.textureWidthScaled, this.canvasHeight);

        if(scrollingX < (-this.canvasWidth / 2)){
            scrollingX += this.textureWidthScaled;
        }
    }

    @Override
    public void resize(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.backgroundY = 0 - (this.canvasHeight / 2);
        this.textureScale = this.canvasHeight / this.textureBackground.getHeight() ;
        this.textureWidthScaled = this.textureBackground.getWidth() * textureScale;
        this.floorHeight = -((this.canvasHeight / 10) * 3.3f);
    }

    @Override
    public void screenTouched() {
        // Backgrounds don't care about the screen being touched
    }

    @Override
    public boolean isInCollisionWithBird(Bird bird) {
        return bird.getY() < this.floorHeight;
    }

}

package com.nikit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


import static com.nikit.game.Constants.*;

public class MainMenuScreen implements Screen {

    final BubbleGame game;
    OrthographicCamera camera;
    ExtendViewport viewport;

    GlyphLayout glyphLayout;

    TextureAtlas textureAtlas;
    Sprite touchSprite;
    Circle touchButton;

    public MainMenuScreen(BubbleGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_PORT_WIDT, VIEW_PORT_HEIGHT, camera);

        game.batch.setProjectionMatrix(camera.combined);

        glyphLayout = new GlyphLayout();

        textureAtlas = new TextureAtlas("texture.txt");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        processInputs();

        game.batch.begin();

        glyphLayout.setText(game.font, "Welcome to Drop!");
        game.font.draw(game.batch, glyphLayout, 0, camera.viewportHeight / 2);

        glyphLayout.setText(game.font, "Tap anywhere to begin!");
        game.font.draw(game.batch, glyphLayout, 0, (camera.viewportHeight / 2) + (glyphLayout.height * 1.3f));

        touchSprite.draw(game.batch);

        game.batch.end();


    }

    private void createTouchButton() {
        touchSprite = textureAtlas.createSprite("touch_circle");
        touchSprite.setOrigin(0, 0);

        float width = touchSprite.getWidth() * 0.5f;
        float height = touchSprite.getHeight() * 0.5f;
        touchSprite.setSize(width, height);

        touchButton = new Circle(camera.viewportWidth / 2 - touchSprite.getWidth() / 2, 0, touchSprite.getWidth() / 2);


        touchSprite.translate(touchButton.x, touchButton.y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);

        createTouchButton();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }

    Circle userTouch = new Circle(0,0,10);
    private void processInputs() {
        if (Gdx.input.isTouched()) {
            Vector3 vector3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            userTouch.x = vector3.x;
            userTouch.y = vector3.y;

            if (touchButton.overlaps(userTouch)) {
                game.setScreen(new GameScreen(game));
            }
        }
    }
}

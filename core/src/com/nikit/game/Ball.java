package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

public class Ball extends Drawable {

    public Ball(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, World world, float scale, Vector2 pos) {
        Sprite sprite = textureAtlas.createSprite("ball");
        float width = sprite.getWidth() * scale;
        float height = sprite.getHeight() * scale;

        sprite.setSize(width, height);
        sprite.setOrigin(0, 0);

        Body body = physicsShapeCache.createBody("ball", world, scale, scale);
        body.setTransform(pos, 0);
        body.setUserData(Constants.OBJECT_TYPE_BALL);
        body.getMassData().mass = Constants.BALL_MASS;
        sprite.setPosition(body.getPosition().x, body.getPosition().y);

        sprites = new Sprite[1];
        sprites[0] = sprite;
        bodies = new Body[1];
        bodies[0] = body;

    }

    public void draw(SpriteBatch batch) {

        for (int i = 0; i < bodies.length; i++) {

            Body body = bodies[i];
            Sprite sprite = sprites[i];

            Vector2 poss = body.getPosition();
            sprite.setPosition(poss.x, poss.y);
            float degree = (float) Math.toDegrees(body.getAngle());

            sprite.setRotation(degree);
            sprite.draw(batch);
        }

    }

    @Override
    public void create(World world) {

    }


}

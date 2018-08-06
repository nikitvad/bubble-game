package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.Objects;

/**
 * Created by nikit on 05.08.2018.
 */

public class Stick extends Drawable {


    public Stick(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, World world, Float scale, Vector2 initialPos, float angle) {
        Sprite sprite = textureAtlas.createSprite("stick");

        float width = sprite.getWidth() * scale;
        float height = sprite.getHeight() * scale;

        sprite.setSize(width, height);
        sprite.setOrigin(0, 0);

        Body body = physicsShapeCache.createBody("stick", world, scale, scale);
        body.getMassData().mass = Constants.STICK_MASS;
        body.setUserData(Constants.OBJECT_TYPE_STICK);
        float degree = (float) Math.toRadians(angle);
        body.setTransform(initialPos, degree);

        sprites = new Sprite[]{sprite};
        bodies = new Body[]{body};

    }

    public void draw(SpriteBatch batch) {

        for (int i = 0; i < bodies.length; i++) {
            Sprite sprite = sprites[i];
            Body body = bodies[i];

            Vector2 pos = body.getPosition();
            sprite.setPosition(pos.x, pos.y);
            sprite.draw(batch);

            float degree = (float) Math.toDegrees(body.getAngle());

            sprite.setRotation(degree);
            sprite.draw(batch);

        }
    }

    @Override
    public void create(World world) {

    }
}

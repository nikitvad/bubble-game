package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

/**
 * Created by nikit on 05.08.2018.
 */

public class Stick {
    Body body;
    Sprite sprite;

    public Stick(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, World world, Float scale, Vector2 initialPos) {
        sprite = textureAtlas.createSprite("stick");

        float width = sprite.getWidth() * scale;
        float height = sprite.getHeight() * scale;

        sprite.setSize(width, height);
        sprite.setOrigin(0, 0);

        body = physicsShapeCache.createBody("stick", world, scale, scale);
        body.setTransform(initialPos, 0);

        body.setUserData(Constants.OBJECT_TYPE_STICK);

    }

    public void draw(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);

        float degree = (float) Math.toDegrees(body.getAngle());

        sprite.setRotation(degree);
        sprite.draw(batch);

    }

}

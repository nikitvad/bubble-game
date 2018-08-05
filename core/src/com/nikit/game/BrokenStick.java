package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

/**
 * Created by nikit on 05.08.2018.
 */

public class BrokenStick {
    Body[] bodies;
    Sprite[] sprites;

    public BrokenStick(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, World world, Vector2 initialPos, float scale, float angle) {


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodies = new Body[Constants.BROKEN_STICK_SPRITE_NAMES.length];
        sprites = new Sprite[Constants.BROKEN_STICK_SPRITE_NAMES.length];

        double prevX = initialPos.x;
        double prevY = initialPos.y;

        for (int i = 0; i < Constants.BROKEN_STICK_SPRITE_NAMES.length; i++) {
            String item = Constants.BROKEN_STICK_SPRITE_NAMES[i];

            Sprite sprite = textureAtlas.createSprite(item);

            float width = sprite.getWidth() * scale;
            float height = sprite.getHeight() * scale;

            sprite.setOrigin(0, 0);
            sprite.setSize(width, height);


            Body body = physicsShapeCache.createBody(item, world, bodyDef, scale, scale);
            body.setTransform((float) prevX, (float) prevY, (float) Math.toRadians(angle));

            prevX += Math.cos(Math.toRadians(angle)) * width;
            prevY += Math.sin(Math.toRadians(angle)) * height;

            bodies[i] = body;
            sprites[i] = sprite;
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        float degree = 0;
        Vector2 pos;
        for (int i = 0; i < sprites.length; i++) {

            Sprite sprite = sprites[i];
            Body body = bodies[i];

            pos = body.getPosition();

            sprite.setPosition(pos.x, pos.y);
            degree = (float) Math.toDegrees(body.getAngle());
            sprite.setRotation(degree);
            sprite.draw(spriteBatch);

        }
    }
}

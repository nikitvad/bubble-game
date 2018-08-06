package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.Arrays;

/**
 * Created by nikit on 05.08.2018.
 */

public class BrokenStick extends Drawable {

    private final TextureAtlas textureAtlas;
    private final PhysicsShapeCache physicsShapeCache;
    private final World world;
    private final Vector2 initialPos;
    private final float scale;
    private final float angle;

    public BrokenStick(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, World world, Vector2 initialPos, float scale, float angle) {


        this.textureAtlas = textureAtlas;
        this.physicsShapeCache = physicsShapeCache;
        this.world = world;
        this.initialPos = initialPos;
        this.scale = scale;
        this.angle = angle;
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

    @Override
    public void create(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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
            body.setUserData(Constants.OBJECT_TYPE_STICK_FRAGMENT);

            body.getMassData().mass = Constants.STICK_MASS * Constants.BROKEN_STICK_MASS_SCALE[i];

            prevX += Math.cos(Math.toRadians(angle)) * (width + Constants.BROKEN_STICK_SPRITE_OFFSETS[i] * scale) ;
            prevY += Math.sin(Math.toRadians(angle)) * (width + Constants.BROKEN_STICK_SPRITE_OFFSETS[i] * scale);

            bodies[i] = body;
            sprites[i] = sprite;
        }
    }
}

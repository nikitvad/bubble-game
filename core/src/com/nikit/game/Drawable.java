package com.nikit.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

public abstract class Drawable {

    public Sprite[] sprites;
    public Body[] bodies;

    public abstract void draw(SpriteBatch batch);

    public abstract void create(World world);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drawable drawable = (Drawable) o;
        return Arrays.equals(bodies, drawable.bodies);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bodies);
    }
}

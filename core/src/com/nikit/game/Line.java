package com.nikit.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Line {

    Vector2 startPos = new Vector2(0, 0);
    Vector2 endPos = new Vector2(0, 0);

    float width = 0;

    Color color = new Color(0, 0, 0, 1);

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rectLine(startPos, endPos, width);
    }

    public void setStartPos(Vector2 startPos) {
        this.startPos = startPos;
    }

    public void setStartPos(float x, float y) {
        setStartPos(new Vector2(x, y));
    }

    public void setEndPos(Vector2 endPos) {
        this.endPos = endPos;
    }

    public void setEndPos(float x, float y) {
        setEndPos(new Vector2(x, y));
    }


    public void reset() {
        startPos = new Vector2(0, 0);
        endPos = new Vector2(0, 0);
        width = 0;
        color = new Color(0, 0, 0, 1);
    }
}

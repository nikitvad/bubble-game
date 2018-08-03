package com.nikit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.Random;

public class EnemyFactory {
    float xPosRange;
    float yPosRange;

    float excludeX;
    float excludeXRange;

    float excludeY;
    float excludeYRange;

    Random random = new Random();

    float scale;
    TextureAtlas textureAtlas;
    PhysicsShapeCache physicsShapeCache;
    World world;

    ObjectListener objectListener;

    public EnemyFactory(TextureAtlas textureAtlas, PhysicsShapeCache physicsShapeCache, float scale, World world) {
        this.textureAtlas = textureAtlas;
        this.physicsShapeCache = physicsShapeCache;
        this.world = world;
        this.scale = scale;
    }

    public Enemy generateEnemy() {
        float x, y;

        while (true) {
            System.out.println("sdfsdgfsdfg");

            x = random.nextInt((int) xPosRange);
            y = random.nextInt((int) yPosRange);

            if (x > excludeX && x < excludeX + excludeXRange) {
                if (y > excludeY && x < excludeY + excludeYRange) {
                    continue;
                }
            }

            break;
        }
        if (world.isLocked()) {
            return null;
        }
        return new Enemy(textureAtlas, physicsShapeCache, world, scale, new Vector2(Math.abs(x), Math.abs(y)));
    }

    private Thread thread;

    boolean onPause = false;

    public void start(final long periodMilliSeconds, final ObjectListener objectListener) {
        this.objectListener = objectListener;

        onPause = false;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        Thread.sleep(periodMilliSeconds);
                        if (!onPause) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    final Enemy enemy = generateEnemy();
                                    if (enemy != null) {
                                        objectListener.onGenerated(enemy);
                                    }

                                }
                            });
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void pause() {
        onPause = true;
    }

    public void resume() {
        onPause = false;
    }

    public void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    interface ObjectListener {
        void onGenerated(Enemy enemy);
    }
}

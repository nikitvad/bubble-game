package com.nikit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.DestructionListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.nikit.game.Constants.*;


public class GameScreen implements Screen {
    BubbleGame game;

    OrthographicCamera camera;
    ExtendViewport viewport;

    PhysicsShapeCache physicsShapeCache;
    TextureAtlas textureAtlas;

    World world;

    Ball ball1;

    Line line;

    Set<Drawable> drawables = new HashSet<Drawable>();

    Set<Drawable> drawablesToCreate = new HashSet<Drawable>();

    Box2DDebugRenderer debugRenderer;




    public GameScreen(BubbleGame game) {
        this.game = game;

        Box2D.init();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_PORT_WIDT, VIEW_PORT_HEIGHT, camera);

        physicsShapeCache = new PhysicsShapeCache("physic.xml");
        textureAtlas = new TextureAtlas("texture.txt");
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(contactListener);

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawBodies(true);
        Gdx.input.setInputProcessor(inputProcessor);

        ball1 = new Ball(textureAtlas, physicsShapeCache, world, SPRITE_SCALE, new Vector2(10, 10));

        line = new Line();
        drawables.add(new Stick(textureAtlas, physicsShapeCache, world, SPRITE_SCALE, new Vector2(20, 40), 45));

    }

    private EnemyFactory createEnemyFactory() {
        EnemyFactory enemyFactory = new EnemyFactory(textureAtlas, physicsShapeCache, SPRITE_SCALE, world);
        enemyFactory.xPosRange = camera.viewportWidth - 10;
        enemyFactory.yPosRange = camera.viewportHeight - 10;

        return enemyFactory;
    }


    @Override
    public void show() {

    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(Drawable drawable: drawablesToCreate){
            drawable.create(world);
            drawables.add(drawable);
        }
        drawablesToCreate.clear();

        game.batch.begin();

        ball1.draw(game.batch);

        for (Drawable drawable : drawables) {
            drawable.draw(game.batch);
        }

        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        line.draw(shapeRenderer);
        shapeRenderer.end();

        stepWorld();

        for (Drawable drawable : bodiesToRemove) {
            for(Body b: drawable.bodies){
                world.destroyBody(b);
            }
            drawables.remove(drawable);
        }
        bodiesToRemove.clear();


    }

    EnemyFactory enemyFactory;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
        createEdges();
        shapeRenderer.setProjectionMatrix(camera.combined);


        if (enemyFactory != null) {
            enemyFactory.stop();
        }

        enemyFactory = createEnemyFactory();
        enemyFactory.start(1000, new EnemyFactory.ObjectListener() {
            @Override
            public void onGenerated(Enemy enemy) {
                drawables.add(enemy);
                if (drawables.size() > 10) {
                    enemyFactory.pause();
                }
            }
        });

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


    float accumulator = 0;
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void dispose() {
        physicsShapeCache.dispose();
    }

    Array<Body> edges = new Array<Body>();

    private void createEdges() {
        if (edges.size > 0) {
            for (Body item : edges) {
                world.destroyBody(item);
            }
            edges.clear();
        }

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape bottom = new PolygonShape();
        bottom.setAsBox(camera.viewportWidth, 1);

        PolygonShape left = new PolygonShape();
        left.setAsBox(1, camera.viewportHeight + 10);

        Vector2[] rightEdge = new Vector2[]{new Vector2(camera.viewportWidth, 0),
                new Vector2(camera.viewportWidth, camera.viewportHeight + 10),
                new Vector2(camera.viewportWidth - 1, camera.viewportHeight + 10),
                new Vector2(camera.viewportWidth - 1, 0)};
        PolygonShape right = new PolygonShape();
        right.set(rightEdge);


        Vector2[] topEdge = new Vector2[]{new Vector2(0, camera.viewportHeight),
                new Vector2(camera.viewportWidth, camera.viewportHeight),
                new Vector2(0, camera.viewportHeight - 1),
                new Vector2(camera.viewportWidth, camera.viewportHeight - 1)};
        PolygonShape top = new PolygonShape();
        top.set(topEdge);

        fixtureDef.shape = bottom;

        Body bottomB = world.createBody(bodyDef);
        bottomB.setUserData(Constants.OBJECT_TYPE_EDGE);
        bottomB.createFixture(fixtureDef);
        edges.add(bottomB);

        fixtureDef.shape = left;

        Body leftB = world.createBody(bodyDef);
        leftB.createFixture(fixtureDef);
        leftB.setUserData(Constants.OBJECT_TYPE_EDGE);
        edges.add(leftB);

        fixtureDef.shape = right;

        Body rightB = world.createBody(bodyDef);
        rightB.setUserData(Constants.OBJECT_TYPE_EDGE);
        rightB.createFixture(fixtureDef);
        edges.add(rightB);


        fixtureDef.shape = top;

        Body topB = world.createBody(bodyDef);
        topB.setUserData(Constants.OBJECT_TYPE_EDGE);
        topB.createFixture(fixtureDef);
        edges.add(topB);

        top.dispose();
        bottom.dispose();
        left.dispose();
        right.dispose();

    }

    ContactListener contactListener = new ContactListener() {
        @Override
        public void beginContact(Contact contact) {

        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {


        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();



            if (bodyA.getUserData().equals(Constants.OBJECT_TYPE_BALL) || bodyB.getUserData().equals(OBJECT_TYPE_BALL)) {
                if (bodyA.getUserData().equals(Constants.OBJECT_TYPE_STICK) || bodyB.getUserData().equals(OBJECT_TYPE_STICK)) {
                    processBallStickContact(bodyA, bodyB);
                }
            }



        }
    };

    private void processBallStickContact(Body a, Body b) {
        if (a.getUserData().equals(OBJECT_TYPE_STICK)) {
            Vector2 pos = a.getPosition();
            float angle = (float) Math.toDegrees(a.getAngle());

            addToRemove(findDrawableWithBody(a));

            BrokenStick brokenStick = new BrokenStick(textureAtlas, physicsShapeCache, world, pos, SPRITE_SCALE, angle);
            drawablesToCreate.add(brokenStick);
            Vector2 velocity = b.getLinearVelocity();
            velocity.x  = -velocity.x;
            velocity.y = -velocity.y;
            b.setLinearVelocity(velocity);
        } else {

            Vector2 pos = b.getPosition();
            float angle = (float) Math.toDegrees(b.getAngle());
            addToRemove(findDrawableWithBody(b));
            BrokenStick brokenStick = new BrokenStick(textureAtlas, physicsShapeCache, world, pos, SPRITE_SCALE, angle);
            drawablesToCreate.add(brokenStick);

            Vector2 velocity = a.getLinearVelocity();
            velocity.x  = -velocity.x;
            velocity.y = -velocity.y;
            a.setLinearVelocity(velocity);
        }
    }


    private Drawable findDrawableWithBody(Body body) {
        for (Drawable drawable : drawables) {
            for (Body b : drawable.bodies) {
                if (b.equals(body)) {
                    return drawable;
                }
            }
        }

        return null;
    }

    ArrayList<Drawable> bodiesToRemove = new ArrayList<Drawable>();

    private void addToRemove(Drawable drawable) {
        bodiesToRemove.add(drawable);
    }

    private InputProcessor inputProcessor = new InputProcessor() {

        int lineBuilderPointer = -1;

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));

            if (lineBuilderPointer < 0) {
                lineBuilderPointer = pointer;
                line.width = 1;
                line.setEndPos(touchPos.x, touchPos.y);
                line.setStartPos(touchPos.x, touchPos.y + 1);
            }


            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {

            if (pointer == lineBuilderPointer) {
                lineBuilderPointer = -1;

                Vector2 vector2 = new Vector2(line.endPos.x - line.startPos.x, line.endPos.y - line.startPos.y);
                ball1.bodies[0].applyForceToCenter(vector2.x * FORCE_SCALE, vector2.y * FORCE_SCALE, true);

                line.reset();
            }

            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {

            Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));
            if (pointer == lineBuilderPointer) {
                line.setEndPos(touchPos.x, touchPos.y);
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    };

    private void processBallEdgeContact(Body ball, Body edge, Vector2 contactNormal) {
        System.out.println(contactNormal);
//        Vector2 oldImpulse = ball.
//        ball.applyForceToCenter(new Vector2(1000 * contactNormal.x, 1000 * contactNormal.y), true);
    }


}

package com.nikit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import static com.nikit.game.Constants.*;


public class GameScreen implements Screen {
    BubbleGame game;

    OrthographicCamera camera;
    ExtendViewport viewport;

    PhysicsShapeCache physicsShapeCache;
    TextureAtlas textureAtlas;

    World world;

    Ball ball1;

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

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();


        ball1.draw(game.batch);


        game.batch.end();

        debugRenderer.render(world, camera.combined);
        stepWorld();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
        createEdges();

        ball1 = new Ball(textureAtlas, physicsShapeCache, world, SPRITE_SCALE, new Vector2(55, 10));
        ball1.body.applyLinearImpulse(new Vector2(1000, 1000), ball1.body.getWorldCenter(), true);
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

            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();


            String contactType = Utils.getContactType(bodyA, bodyB);
            if (contactType.equals(CONTACT_TYPE_EDGE_BALL)) {
                processBallEdgeContact(bodyB, bodyA, oldManifold.getLocalNormal());
            } else if (contactType.equals(CONTACT_TYPE_BALL_EDGE)) {
                processBallEdgeContact(bodyA, bodyB, oldManifold.getLocalNormal());
            }

//            Gdx.app.log("asdfsdf", oldManifold.getLocalNormal().toString());

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    };


    private void processBallEdgeContact(Body ball, Body edge, Vector2 contactNormal) {
        System.out.println(contactNormal);
        ball.applyLinearImpulse(new Vector2(2000 * contactNormal.x, 2000 * contactNormal.y), ball.getLocalCenter(), true);
    }


}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.SpatialTrack;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.Light;
import com.jme3.light.LightProbe;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
//import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.Terrain;
import com.jme3.animation.Animation;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import java.util.HashMap;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.*;
import com.jme3.util.SkyFactory;
import java.util.Random;

/**
 *
 * @author A S U S
 */
public class GameLogic extends AbstractAppState implements AnimEventListener, ActionListener {

    Node rootNode;
    Node localRootNode = new Node("terrain");
    AssetManager assetManager;
    InputManager inputManager;
    BulletAppState bulletAppState;
    Spatial player, obs2;
    CharacterControl playerControl;
    FlyByCamera flyByCamera;
    Camera camera;
    ChaseCamera chaseCam;
    RigidBodyControl obsCon;
    BitmapFont fnt;
    BitmapText text;
    boolean statusPause = false;
    AudioNode audioJump, nodeBGM, audioTabrak, audioRun;
    BitmapFont defaultFont;
    //textField
    BitmapText fpsScoreText, pressStart;

    final Vector3f playerWalkDirection = Vector3f.ZERO;
    boolean left = false, right = false, up = false, down = false, jump = false;
    boolean status = false;
    //animation
    AnimChannel animationChannel;
    AnimChannel shootingChannel;
    AnimControl animationControl;
    float airTime = 0;
    Scene sc;
    Audio audio;
    Obstacle obs;
    Obstacle obsY;
    int score = 0;
    Random rand;
    

    Vector3f temp;

    public GameLogic(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        flyByCamera = app.getFlyByCamera();
        camera = app.getCamera();
        fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(fnt);
        sc = new Scene(app);
        audio = new Audio(app);
        obs = new Obstacle(app);
        obsY = new Obstacle(app);
        
        rand = new Random();

    }

    //tanahVoid
    public void Tanah() {
        Spatial floor = localRootNode.getChild("Tanah");
        floor.rotate(0, 0, 0);

        bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));

    }

    //font 
    private void loadText(BitmapText txt, String text, BitmapFont font, float x, float y, float z) {
        txt.setSize(font.getCharSet().getRenderedSize());
        txt.setLocalTranslation(txt.getLineWidth() * x, txt.getLineHeight() * y, z);
        txt.setText(text);
        localRootNode.attachChild(txt);
    }

    public void obstacle() {
        obs2 = obs.getObstacle("Models/Fence 01/Fence 01.j3o");
        obs2.setLocalScale(5f, 0f, 5f);

        System.out.println(obs2.getLocalTranslation());
        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
        localRootNode.attachChild(obs2);

        float rad2 = boundingBox.getXExtent();
        float height2 = boundingBox.getYExtent();
        CapsuleCollisionShape playerShape2 = new CapsuleCollisionShape(rad2, height2);
        obsCon = new RigidBodyControl(playerShape2, 0f);

        obs2.addControl(obsCon);
        bulletAppState.getPhysicsSpace().add(obsCon);
        obsCon.setPhysicsLocation(new Vector3f(-100f, 0.99419403f, 0.4776468f));

    }
    
    public void obstacleY() {
        obs2 = obs.getObstacle("Models/Fence 01/Fence 01.j3o");
        obs2.setLocalScale(5f, 0f, 5f);

        System.out.println(obs2.getLocalTranslation());
        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
        localRootNode.attachChild(obs2);

        float rad2 = boundingBox.getXExtent();
        float height2 = boundingBox.getYExtent();
        CapsuleCollisionShape playerShape2 = new CapsuleCollisionShape(rad2, height2);
        obsCon = new RigidBodyControl(playerShape2, 0f);

        obs2.addControl(obsCon);
        bulletAppState.getPhysicsSpace().add(obsCon);
        obsCon.setPhysicsLocation(new Vector3f(-100f, 0.99419403f, 0.4776468f));

    }

    public void setPhysic() {
        //etupAnimationController();
        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(false);

    }

    public void setScene() {
        //Spatial scene = assetManager.loadModel("Scenes/TestScene.j3o");

        Spatial scene = sc.getModelScene();//Masukan sinbad ke scene
        localRootNode.attachChild(scene);//masangin scene ke local
        scene.getParent().rotate(0, -190, 0);

    }

    public void createPlayer() {
        player = localRootNode.getChild("Player");//Dapetin kelas anak "player" dari Sinbad

        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();

        float rad = boundingBox.getXExtent();
        float height = boundingBox.getYExtent();
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(rad, height);
        playerControl = new CharacterControl(playerShape, 1.0f);
        player.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        playerControl.setPhysicsLocation(new Vector3f(-40f, 0.99419403f, 0.4776468f));

        //Animation
        animationControl = player.getParent().getControl(AnimControl.class);
        System.out.println(animationControl == null ? "Kosong" : "isi");
        animationControl.addListener(this);
        animationChannel = animationControl.createChannel();

        //IO
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Go", new KeyTrigger(KeyInput.KEY_F9));

        inputManager.addMapping("Pausez", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");
        inputManager.addListener(actionListener, "Jump");
        inputManager.addListener(actionListener, "Pausez");
        inputManager.addListener(actionListener, "Go");

    }

    private void createSky() {
        localRootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));

    }

    public void initAudioJump() {

        audioJump = audio.initAudioJump();
        localRootNode.attachChild(audioJump);
    }

    public void initAudioTabrak() {
        /* Berbunyi jika tabrakan. */
        audioTabrak = audio.initAudioTabrak();
        localRootNode.attachChild(audioTabrak);
    }

    public void jumpSound() {
        nodeBGM = audio.jumpSound();
        localRootNode.attachChild(nodeBGM);
        nodeBGM.play();

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.jumpSound();

        this.setPhysic();
        stateManager.attach(bulletAppState);
        rootNode.attachChild(localRootNode);

        //scene
        this.setScene();
        //score
        defaultFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        pressStart = new BitmapText(defaultFont, false);
        fpsScoreText = new BitmapText(defaultFont, false);

        loadText(fpsScoreText, "Current Score: 0", defaultFont, 0, 2 + 5, 0);
        loadText(pressStart, "PRESS ENTER", defaultFont, 0, 5 + 5, 0);

        //Floor part
        this.Tanah();

        //audio
        //init audio jump
        initAudioJump();

        //init audio tabrak
        initAudioTabrak();
        //score
        text.setBox(new Rectangle(0, 0, 1360, 768));
        text.setSize(fnt.getPreferredSize() * 2f);
        text.setText("jojo");
        text.setLocalTranslation(0, 5, 0);
        localRootNode.attachChild(text);

        this.createSky();
//       
        //Player part
        this.createPlayer();

        flyByCamera.setEnabled(false);
        chaseCam = new ChaseCamera(camera, player, inputManager);

        //obs2 part
        this.obstacle();

    }

    protected final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) {
                animationChannel.setAnim("RunBase");

            }

            if (name.equals(
                    "Pause") && !keyPressed) {
                setEnabled(!isEnabled());
            } else if (name.equals("Up")) {
                up = true;

            } else if (name.equals(
                    "Down")) {
                up = false;
                down = keyPressed;
                up = false;

            } else if (name.equals(
                    "Left")) {
                up = false;

                left = keyPressed;

            } else if (name.equals("Right")) {
                up = false;
                right = keyPressed;

            } else if (name.equals(
                    "Jump")) {

                playerControl.jump();
                playerControl.setJumpSpeed(20);

                if (keyPressed) {
                    animationChannel.setAnim("JumpLoop");
                    audioJump.playInstance(); // membunyikan suara sekali
                } else {
                    animationChannel.setAnim("IdleBase");
                    animationChannel.setAnim("RunBase");

                }
            } else if (name.equals("Pausez")) { // go
                statusPause = true;
                playerControl.setPhysicsLocation(new Vector3f(-40f, 0.99419403f, 0.4776468f));
                obsCon.setPhysicsLocation(new Vector3f(-100f, 0.99419403f, 0.4776468f));
                score += 1;
                System.out.println(score + "ini score");

            } else if (name.equals("Go")) // pause kebalik nama
            {
                statusPause = false;

            }

            if (!keyPressed) {
                up = false;
            }

        }
    };

    @Override
    public void update(float tpf) {
        Vector3f camDir = camera.getDirection().clone();
        Vector3f camLeft = camera.getLeft().clone();
        camDir.y = 0;
        camLeft.y = 0;

        camDir.normalizeLocal();
        camLeft.normalizeLocal();

        playerWalkDirection.set(0, 0, 0);
        playerControl.setViewDirection(camLeft);

        if (left) {
            playerWalkDirection.add(camLeft);
            playerWalkDirection.set(0, 0, 5);

        }
        if (right) {
            playerWalkDirection.add(camLeft.negate());
            playerWalkDirection.set(0, 0, -5);
        }
        if (up) {
            playerWalkDirection.add(camDir);
            //playerWalkDirection.set(-5, 0, 0);

        }
        if (down) {
            playerWalkDirection.add(camDir.negate());
            playerWalkDirection.set(5, 0, 0);
        }

        if (player != null) {
            playerWalkDirection.multLocal(10f).multLocal(tpf);
            playerControl.setWalkDirection(playerWalkDirection);
        }

        if (statusPause == true) {
            nodeBGM.play();

            if (obsCon.getPhysicsLocation().x < -10) {

                obsCon.setPhysicsLocation(new Vector3f(obsCon.getPhysicsLocation().x + tpf * 25, 1, 0.4776468f));
                // System.out.println(obsCon.getPhysicsLocation().x + " ini obs");
                score += 1;
                System.out.println("ini score" + score);

            } else {

                obsCon.setPhysicsLocation(new Vector3f(-100, 0.99419403f, 0.4776468f));

            }

            if (playerControl.getPhysicsLocation().x > -39) {
                statusPause = false;
                animationChannel.setAnim("IdleBase");
                score = 0;
                System.out.println("reset" + score);

                audioTabrak.playInstance();
                nodeBGM.stop();

            }

        }
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

    }

}

//class Player extends Character {
//
//    public Player(SimpleApplication app) {
//        super(app);
//    }
//
//    public void createPlayer() {
//        player = localRootNode.getChild("Player");//Dapetin kelas anak "player" dari Sinbad
//        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
//
//        float rad = boundingBox.getXExtent();
//        float height = boundingBox.getYExtent();
//        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(rad, height);
//        playerControl = new CharacterControl(playerShape, 1.0f);
//        player.addControl(playerControl);
//        bulletAppState.getPhysicsSpace().add(playerControl);
//        playerControl.setPhysicsLocation(new Vector3f(-40f, 0.99419403f, 0.4776468f));
//
//        //Animation
//        animationControl = player.getParent().getControl(AnimControl.class);
//        System.out.println(animationControl == null ? "Kosong" : "isi");
//        animationControl.addListener(this);
//        animationChannel = animationControl.createChannel();
//
//        //IO
//        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
//        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
//        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
//        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
//        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
//        inputManager.addMapping("Go", new KeyTrigger(KeyInput.KEY_F9));
//
//        inputManager.addMapping("Pausez", new KeyTrigger(KeyInput.KEY_P));
//        inputManager.addListener(actionListener, "Pause");
//        inputManager.addListener(actionListener, "Up");
//        inputManager.addListener(actionListener, "Down");
//        inputManager.addListener(actionListener, "Left");
//        inputManager.addListener(actionListener, "Right");
//        inputManager.addListener(actionListener, "Jump");
//        inputManager.addListener(actionListener, "Pausez");
//        inputManager.addListener(actionListener, "Go");
//
//    }


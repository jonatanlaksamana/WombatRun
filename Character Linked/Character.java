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
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
//import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author A S U S
 */
public class Character extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("terrain");
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private BulletAppState bulletAppState;
    private Spatial player;
    private CharacterControl playerControl;
    private final FlyByCamera flyByCamera;
    private final Camera camera;
    private ChaseCamera chaseCam;

    private final Vector3f playerWalkDirection = Vector3f.ZERO;
     boolean left = false, right = false, up = false, down = false, jump = false;

    //animation
    AnimChannel animationChannel;
    AnimChannel shootingChannel;
    AnimControl animationControl;
    float airTime = 0;

    public Character(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        flyByCamera = app.getFlyByCamera();
        camera = app.getCamera();

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        //etupAnimationController();

        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);
        rootNode.attachChild(localRootNode);
        //Spatial scene = assetManager.loadModel("Scenes/TestScene.j3o");
        Spatial scene = assetManager.loadModel("Models/Sinbad/Sinbad.mesh.j3o");

        localRootNode.attachChild(scene);
        Spatial floor = localRootNode.getChild("floor");
        bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));

        player = localRootNode.getChild("player");

        BoundingBox boundingBox = (BoundingBox) player.getWorldBound();
        float rad = boundingBox.getXExtent();
        float height = boundingBox.getYExtent();
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(rad, height);

        playerControl = new CharacterControl(playerShape, 1.0f);
        player.addControl(playerControl);

        bulletAppState.getPhysicsSpace().add(playerControl);

        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");
        inputManager.addListener(actionListener, "Jump");

        flyByCamera.setEnabled(false);
        chaseCam = new ChaseCamera(camera, player, inputManager);

    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                setEnabled(!isEnabled());
            } else if (name.equals("Up")) {
                up=keyPressed;
               
            }
            else if (name.equals("Down")) {
                down=keyPressed;
               
            }
            else if (name.equals("Left")) {
                left=keyPressed;
               
            }
            else if (name.equals("Right")) {
                right=keyPressed;
               
            }
            else if (name.equals("Jump")) {
//                //playerControl.setJumpSpeed(35f);
//                //animationControl.createChannel();
//                animationChannel.setAnim("JumpStart");
//                //new AnimationEvent(player, "JumpStart");
                playerControl.jump();
                
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

        if (left) {
            playerWalkDirection.add(camLeft);
            playerWalkDirection.set(0,0,5);
            
        }
        if (right) {
            playerWalkDirection.add(camLeft.negate());
            playerWalkDirection.set(0,0,-5);
        }
        if (up) {
            playerWalkDirection.add(camDir);
            playerWalkDirection.set(-5,0,0);
        }
        if (down) {
            playerWalkDirection.add(camDir.negate());
            playerWalkDirection.set(5,0,0);
        }

        if (player != null) {
            playerWalkDirection.multLocal(10f).multLocal(tpf);
            playerControl.setWalkDirection(playerWalkDirection );
        }
//        if (jump == false) {
//            //if (!"stand".equals(animationChannel.getAnimationName())) {
//            animationChannel.setAnim("JumpStart", 1f);
//            animationChannel.setAnim("JumpEnd", 1f);
//            //}
//        }
    }
//
//    private void setupAnimationController() {
//        animationControl = rootNode.getControl(AnimControl.class);
//        animationControl.addListener((AnimEventListener) this);
//        animationChannel = animationControl.createChannel();
//
//    }

}

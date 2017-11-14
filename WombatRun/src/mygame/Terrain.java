/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author A S U S
 */
public class Terrain extends AbstractAppState {

    Spatial scene;
    BulletAppState bul;
    Node rootNode;
    Node localRootNode = new Node("terrain");
    AssetManager assetManager;
    Spatial terrain;

    public Terrain(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        //etupAnimationController();
        bul = new BulletAppState();
        bul.setDebugEnabled(false);
        stateManager.attach(bul);
        rootNode.attachChild(localRootNode);
        scene = assetManager.loadModel("Models/Sinbad/Sinbad.j3o");
        localRootNode.attachChild(scene);

//        //Collision
//        terrain = localRootNode.getChild("Tanah");//Dapetin kelas anak "player" dari Sinbad
//        BoundingBox boundingBox = (BoundingBox) terrain.getWorldBound();
//        float rad = boundingBox.getXExtent();
//        float height = boundingBox.getYExtent();
//        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(rad, height);
        //Floor part
        Spatial floor = localRootNode.getChild("Tanah");
        bul.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;

/**
 *
 * @author A S U S
 */
public class Main extends SimpleApplication{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(new Character(this));
    }
    
}

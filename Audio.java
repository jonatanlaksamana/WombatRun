/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

/**
 *
 * @author A S U S
 */
public class Audio extends  AbstractAppState{
    
    AudioNode audioJump , audioTabrak , nodeBGM;
    AssetManager assetManager;
    
    
    public Audio (SimpleApplication app)
    {
        assetManager = app.getAssetManager();
    }
       public AudioNode initAudioJump() {
        /* Suara lompatan dipicu oleh mouse yang diklik. */
        audioJump = new AudioNode(assetManager, "Sounds/Blast-SoundBible.com-2068539061.wav", AudioData.DataType.Buffer);
        audioJump.setPositional(false);
        audioJump.setLooping(false);
        audioJump.setVolume(2);
        return audioJump;
       
    }

    public AudioNode initAudioTabrak() {
        /* Berbunyi jika tabrakan. */
        audioTabrak = new AudioNode(assetManager, "Sounds/an_items_was_lost.wav", AudioData.DataType.Buffer);
        audioTabrak.setPositional(false);
        audioTabrak.setLooping(false);
        audioTabrak.setVolume(2);
        return audioTabrak;
       
    }

    public AudioNode jumpSound() {
        nodeBGM = new AudioNode(assetManager, "Sounds/bgm11.wav", AudioData.DataType.Buffer);
        nodeBGM.setPositional(false);
        nodeBGM.setLooping(true);
        nodeBGM.setVolume(3);
        return nodeBGM;
       
        

    }
 
    
}

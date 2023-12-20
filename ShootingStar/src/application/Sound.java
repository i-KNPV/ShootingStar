package application;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    
    public Sound() {
    	// Store the file URLS in an array for easy access
        soundURL[0] = getClass().getResource("/assets/sounds/hurt.wav");
        soundURL[1] = getClass().getResource("/assets/sounds/bg_music.wav");
        soundURL[2] = getClass().getResource("/assets/sounds/danger2.wav");
        soundURL[3] = getClass().getResource("/assets/sounds/death.wav");
        soundURL[4] = getClass().getResource("/assets/sounds/obtain_shimmer.wav");
        soundURL[5] = getClass().getResource("/assets/sounds/shimmer.wav");
        soundURL[6] = getClass().getResource("/assets/sounds/overture.wav");
        soundURL[7] = getClass().getResource("/assets/sounds/twinkle.wav");
        soundURL[8] = getClass().getResource("/assets/sounds/noise.wav");
        soundURL[9] = getClass().getResource("/assets/sounds/laser.wav");
        soundURL[10] = getClass().getResource("/assets/sounds/grab.wav");
        soundURL[11] = getClass().getResource("/assets/sounds/shield.wav");
        soundURL[12] = getClass().getResource("/assets/sounds/break.wav");
    }
    
    // Set the sound file for playback
    public void setFile(int i) {
        try {
        	// Get the audio stream from the specified URL
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            
            // Obtain a sound clip and open it
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    // Play the sound
    public void play() {
        clip.start();
    }
    
    // Stop the sound
    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }
    
    // Loop the sound forever
    public void loop(int i) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    // Change the volume of the sound
    public void setVolume(float volume) {
        if (clip == null) {
            return;
        }
        
        // Get the volume control of the clip
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        
        // Calculate the gain value based on the volume
        float gain = (range * volume) + gainControl.getMinimum(); // volume is a value between 0 and 1
        gainControl.setValue(gain); // Set the volume
    }

}

// REFERENCE: https://youtu.be/nUHh_J2Acy8?si=IWpi4-TtdJql9vch
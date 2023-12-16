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
        soundURL[0] = getClass().getResource("/assets/sounds/hurt.wav");
        soundURL[1] = getClass().getResource("/assets/sounds/bg_music.wav");
        soundURL[2] = getClass().getResource("/assets/sounds/danger1.wav");
        soundURL[3] = getClass().getResource("/assets/sounds/death.wav");
        soundURL[4] = getClass().getResource("/assets/sounds/obtain_shimmer.wav");
        soundURL[5] = getClass().getResource("/assets/sounds/shimmer.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Print the stack trace to understand what went wrong
        }
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public void loop(int i) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void setVolume(float volume) {
        if (clip == null) {
            return;
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum(); // volume is a value between 0 and 1
        gainControl.setValue(gain);
    }

}

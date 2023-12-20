package application;

public class Settings {
	private boolean musicMuted;
    private boolean sfxMuted;

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void setMusicMuted(boolean musicMuted) {
        this.musicMuted = musicMuted;
    }

    public boolean isSfxMuted() {
        return sfxMuted;
    }

    public void setSfxMuted(boolean sfxMuted) {
        this.sfxMuted = sfxMuted;
    }
}

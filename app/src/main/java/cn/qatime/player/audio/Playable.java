package cn.qatime.player.audio;

public interface Playable {
	long getDuration();
	String getPath();
	boolean isAudioEqual(Playable audio);
}
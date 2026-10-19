package org.example;

import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VLC {
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private boolean play = false;
    private String totalTimeText;
    private long totalTimeLong;
    private long skipTimeMs;
    private String videoPath;
    void changePause(){
        play = !play;
        mediaPlayer.controls().setPause(!play); // True пауза
    }
    VLC(ImageView imageView){
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));
        skipTimeMs = 5000;
    }

    void setVideo(String path){
        videoPath = path;
        totalTimeLong = 0; totalTimeText = "";

    }
    void play(){
        play = true;
        videoPath = "/home/tir/Рабочий стол/Tir/IMG_7317.MOV";
        mediaPlayer.media().play(videoPath, ":input-repeat=65535");
    }
    void peremotka(boolean forward){
        if(forward){
            mediaPlayer.controls().skipTime(skipTimeMs);
        }
        else mediaPlayer.controls().skipTime(-1 * skipTimeMs);
    }
    boolean isPlaying(){return play;}
    String getTimeText() {
        long time = mediaPlayer.media().info().duration() / 1000;
        if (time > 0) {
            if(time == totalTimeLong)return totalTimeText;
            totalTimeLong = time;
            totalTimeText = String.valueOf(time / 3600) + ":" + String.valueOf((time % 3600) / 60) + ":" + String.valueOf(time % 60);
            return totalTimeText;
        }
        return "0";
    }
    long getNowTime(){return mediaPlayer.status().time()/1000;}
}

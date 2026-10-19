package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        StackPane videoPane = new StackPane(imageView);
        imageView.fitWidthProperty().bind(videoPane.widthProperty());
        imageView.fitHeightProperty().bind(videoPane.heightProperty());

        VLC vlc = new VLC(imageView);


        VBox vBox = new VBox(videoPane);
        //VBox.setVgrow(videoPane, Priority.ALWAYS);
        videoPane.prefHeightProperty().bind(vBox.heightProperty().multiply(0.95));
        videoPane.prefWidthProperty().bind(vBox.widthProperty().multiply(1.00));
        videoPane.setMinSize(0, 0); //Чтобы масштабировались,а не обрезалась при уменьшении
        videoPane.setMaxHeight(Region.USE_PREF_SIZE);
        videoPane.setStyle("-fx-background-color: black;");

        Button button = new Button();
        button.setText("Play");
        button.setOnAction(event -> { vlc.changePause();});
        Text text = new Text("Здечь будет шкала, отвечаю");
        HBox pane = new HBox();
        pane.getChildren().add(button);
        pane.getChildren().add(text);
        pane.setStyle("-fx-background-color: darkslategray;");
        Text textTime = new Text("00/00");
        //textTime.setStyle("-fx-font-smoothing-type: gray;");
        textTime.setFill(Color.WHITE);
        pane.getChildren().add(textTime);


        vBox.getChildren().add(pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.setMaxHeight(Double.MAX_VALUE);


        Scene scene = new Scene(vBox, 800, 800);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case SPACE -> {
                    vlc.changePause();
                    event.consume();
                }
                case LEFT, KP_LEFT -> {
                    vlc.peremotka(false);
                    event.consume();
                }
                case RIGHT, KP_RIGHT -> {
                    vlc.peremotka(true);
                    event.consume();
                }
            }
        });
        videoPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                vlc.changePause();
            }
        });
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);
        AnimationTimer timer = new AnimationTimer() {
            private String lastTextTime = "";
            @Override
            public void handle(long now) {
                if (vlc.isPlaying()) {
                    long time = vlc.getNowTime();
                    String currentTime = String.valueOf(time / 3600) + ":" + String.valueOf((time % 3600)/60) + ":" + String.valueOf(time % 60);
                    String newTime = currentTime + "/" +  vlc.getTimeText();
                    if(!newTime.equals(lastTextTime)){
                        lastTextTime = newTime;
                        textTime.setText(lastTextTime);
                    }
                }
            }
        };
        timer.start();

        vlc.setVideo("/home/tir/Загрузки/pam.mp4");
        vlc.play();
        primaryStage.show();
    }


}
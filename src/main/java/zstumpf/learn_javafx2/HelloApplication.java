package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Camera;

import java.io.IOException;

public class HelloApplication extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        Sphere sphere = new Sphere(50);
        sphere.translateXProperty().set(WIDTH/2);
        sphere.translateYProperty().set(HEIGHT/2);

        Group group = new Group();
        group.getChildren().add(sphere);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setCamera(camera);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    camera.setTranslateZ(camera.getTranslateZ() + 50);
                    break;
                case S:
                    camera.setTranslateZ(camera.getTranslateZ() - 50);
                    break;
            }
        });

        stage.setTitle("JavaFX Stage");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
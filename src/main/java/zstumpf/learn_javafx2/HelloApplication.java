package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.Group;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Sphere sphere = new Sphere(50);
        Group group = new Group();
        group.getChildren().add(sphere);

        stage.setTitle("JavaFX Stage");
        stage.setScene(new Scene(group,1000,1000));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
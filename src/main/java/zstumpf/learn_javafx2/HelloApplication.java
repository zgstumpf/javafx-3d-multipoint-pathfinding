package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Camera;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Image skyBackground = new Image(getClass().getResourceAsStream("/images/sky.png"));
        scene.setFill(new ImagePattern(skyBackground));

        Box baseplate = new Box(2000, 10, 2000);
        baseplate.setTranslateY(200);
        root.getChildren().add(baseplate);

        Sphere sphere1 = new Sphere(50);
        root.getChildren().add(sphere1);

        Sphere sphere2 = new Sphere(50);
        sphere2.setTranslateX(400);
        root.getChildren().add(sphere2);

        Box obstacle = new Box(20, 300, 500);
        obstacle.setTranslateX(200);
        root.getChildren().add(obstacle);


        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(20_000);
        camera.setTranslateZ(-800);

        scene.setCamera(camera);

        Rotate cameraRotationX = new Rotate(0, camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(), Rotate.X_AXIS);
        Rotate cameraRotationY = new Rotate(0, camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(), Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotationX);
        camera.getTransforms().add(cameraRotationY);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    camera.setTranslateZ(camera.getTranslateZ() + 50);
                    break;
                case S:
                    camera.setTranslateZ(camera.getTranslateZ() - 50);
                    break;
                case A:
                    camera.setTranslateX(camera.getTranslateX() - 5);
                    break;
                case D:
                    camera.setTranslateX(camera.getTranslateX() + 5);
                    break;
                case UP:
                    cameraRotationX.setAngle(cameraRotationX.getAngle() + 0.5);
                    break;
                case DOWN:
                    cameraRotationX.setAngle(cameraRotationX.getAngle() - 0.5);
                    break;
                case RIGHT:
                    cameraRotationY.setAngle(cameraRotationY.getAngle() + 0.5);
                    break;
                case LEFT:
                    cameraRotationY.setAngle(cameraRotationY.getAngle() - 0.5);
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
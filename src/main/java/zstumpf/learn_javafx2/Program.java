package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Camera;
import javafx.scene.transform.Rotate;

import java.io.IOException;


public class Program extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 600;

    private void moveCamera(Camera camera, double x, double y, double z) {
        // Get rotation transformations from camera
        Rotate rotateX = null;
        Rotate rotateY = null;
        for (Transform transform : camera.getTransforms()) {
            if (transform instanceof Rotate) {
                Rotate rotate = (Rotate) transform;
                if (rotate.getAxis() == Rotate.X_AXIS) {
                    rotateX = rotate;
                } else if (rotate.getAxis() == Rotate.Y_AXIS) {
                    rotateY = rotate;
                }
            }
        }

        // Compute the rotation angles in radians
        double cosY = Math.cos(Math.toRadians(rotateY.getAngle()));
        double sinY = Math.sin(Math.toRadians(rotateY.getAngle()));

        double sinX = Math.sin(Math.toRadians(rotateX.getAngle()));

        // Adjust forward/backward movement based on camera orientation
        double forwardX = z * sinY;
        double forwardZ = z * cosY;

        // Adjust up/down movement based on the X-axis rotation (pitch)
        double forwardY = z * sinX;

        // Adjust strafing (left/right) movement based on Y-axis rotation (yaw)
        double strafeX = x * cosY;
        double strafeZ = x * -sinY;

        // Apply the translation to the camera
        camera.setTranslateX(camera.getTranslateX() + strafeX + forwardX);
        camera.setTranslateY(camera.getTranslateY() + y - forwardY); // Adjust Y movement
        camera.setTranslateZ(camera.getTranslateZ() + strafeZ + forwardZ);
    }

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

        Target target = new Target();
        target.setTranslateX(400);
        root.getChildren().add(target);

        Obstacle wall = new Obstacle(20, 300, 750);
        wall.setTranslateX(200);
        root.getChildren().add(wall);


        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(20_000);
        camera.setTranslateZ(-800);
        scene.setCamera(camera);

        Rotate cameraRotationX = new Rotate(0, Rotate.X_AXIS);
        Rotate cameraRotationY = new Rotate(0, Rotate.Y_AXIS);

        camera.getTransforms().addAll(cameraRotationX, cameraRotationY);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    moveCamera(camera, 0, 0, 50);
                    break;
                case S:
                    moveCamera(camera, 0, 0, -50);
                    break;
                case A:
                    moveCamera(camera, -50, 0, 0);
                    break;
                case D:
                    moveCamera(camera, 50, 0, 0);
                    break;
                case UP:
                    cameraRotationX.setAngle(cameraRotationX.getAngle() + 5);
                    break;
                case DOWN:
                    cameraRotationX.setAngle(cameraRotationX.getAngle() - 5);
                    break;
                case RIGHT:
                    cameraRotationY.setAngle(cameraRotationY.getAngle() + 5);
                    break;
                case LEFT:
                    cameraRotationY.setAngle(cameraRotationY.getAngle() - 5);
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
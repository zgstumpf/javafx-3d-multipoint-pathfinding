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
import javafx.geometry.Point3D;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 600;

    /**
     * Determines if the 3D point is inside the 3D box.
     * This is used to detect points that are impossible to travel to because
     * they are inside the box object.
     * @param box Box instance
     * @param point Point3D instance
     * @return true if point is inside box; false otherwise
     */
    public boolean contains(Box box, Point3D point) {

        // Basic box geometry data
        double width = box.getWidth();
        double height = box.getHeight();
        double depth = box.getDepth();
        Point3D boxCenter = new Point3D(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());

        // Advanced box geometry data
        double rightSideOfBox = boxCenter.getX() + (width / 2);
        double leftSideOfBox = boxCenter.getX() - (width / 2);
        // In JavaFX, Y values get more positive as you go down the Y axis.
        double bottomOfBox = boxCenter.getY() + (height / 2);
        double topOfBox = boxCenter.getY() - (height / 2);
        double nearSideOfBox = boxCenter.getZ() - (depth / 2);
        double farSideOfBox = boxCenter.getZ() + (depth / 2);

        // Determine if point coordinates are within all sides of Box
        return (point.getX() >= leftSideOfBox &&
                point.getX() <= rightSideOfBox &&
                point.getY() <= bottomOfBox &&
                point.getY() >= topOfBox &&
                point.getZ() >= nearSideOfBox &&
                point.getZ() <= farSideOfBox);

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
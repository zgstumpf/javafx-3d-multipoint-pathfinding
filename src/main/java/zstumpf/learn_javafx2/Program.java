package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;

import java.io.IOException;


public class Program extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 600;

    private static final double SHIFT_MULTIPLIER = 30.0;
    private static final double ROTATION_SPEED = 0.1;
    private final Group root = new Group();
    private final ZokkCamera camera = new ZokkCamera();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double dx;
    private double dy;

    /**
     * add created {@link Camera} object to scene
     */
    private void buildCamera() {
        root.getChildren().add(camera.getXForm());
        //cameraXform.setPivot(100,100,100);
    }

    /**
     * Create mouse handlers
     * @param scene
     */
    private void handleMouse(Scene scene){

        scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
//            PickResult pick = event.getPickResult();
//            // picking up obstacle and changed draw mode to opposite
//            if(pick != null) {
//                Node pickedNode = pick.getIntersectedNode();
//                if(pickedNode instanceof MeshView){
//                    MeshView pickedMeshView = (MeshView) pickedNode;
//                    if(pickedMeshView.getDrawMode() == DrawMode.FILL)
//                        pickedMeshView.setDrawMode(DrawMode.LINE);
//                    else if(pickedMeshView.getDrawMode() == DrawMode.LINE)
//                        pickedMeshView.setDrawMode(DrawMode.FILL);
//                }
            //}
        });

        scene.setOnMouseDragged(event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            dx = mousePosX - mouseOldX;
            dy = mousePosY - mouseOldY;

            camera.rotateX(dy * ROTATION_SPEED);
            camera.rotateY(dx * ROTATION_SPEED);
            ;
            // }
        });

    }

    /**
     * Create keyboard handlers
     * @param scene
     */
    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if(!event.isShiftDown()) {
                switch (event.getCode()) {
                    case W:
                        camera.moveForward();
                        break;
                    case S:
                        camera.moveBackward();
                        break;
                    case A:
                        camera.moveLeft();
                        break;
                    case D:
                        camera.moveRight();
                        break;
                    case SPACE:
                        camera.moveUp();
                        break;
                    case CONTROL:
                        camera.moveDown();
                        break;
                    case ESCAPE:
                        System.exit(0);
                        break;
                }
            }
            else if(event.isShiftDown()){

                if(event.isShiftDown() && event.getCode() == KeyCode.W){
                    camera.moveForward(SHIFT_MULTIPLIER);
                }
                else if(event.isShiftDown() && event.getCode() == KeyCode.S){
                    camera.moveBackward(SHIFT_MULTIPLIER);
                }
                else if(event.isShiftDown() && event.getCode() == KeyCode.A){
                    camera.moveLeft(SHIFT_MULTIPLIER);
                }
                else if(event.isShiftDown() && event.getCode() == KeyCode.D){
                    camera.moveRight(SHIFT_MULTIPLIER);
                }
                else if(event.isShiftDown() && event.getCode() == KeyCode.SPACE){
                    camera.moveUp(SHIFT_MULTIPLIER);
                }
                else if(event.isShiftDown() && event.getCode() == KeyCode.CONTROL){
                    camera.moveDown(20);
                }
            }
        });
    }

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

        double cosY = Math.cos(Math.toRadians(rotateX.getAngle()));
        double sinY = Math.sin(Math.toRadians(rotateY.getAngle()));
        double cosX = Math.cos(Math.toRadians(rotateX.getAngle()));
        double sinX = Math.sin(Math.toRadians(rotateX.getAngle()));

        // Forward/backward movement based on camera orientation (Y-axis rotation)
        double forwardX = z * sinY;
        double forwardZ = z * cosY;

        // Left/right strafing based on camera orientation (Y-axis rotation)
        double strafeX = x * cosY;
        double strafeZ = x * -sinY;

        // Adjust up/down movement based on X-axis rotation (pitch)
        double moveY = y - z * sinX;

        // Apply the translation to the camera
        camera.setTranslateX(camera.getTranslateX() + strafeX + forwardX);
        camera.setTranslateY(camera.getTranslateY() + moveY);
        camera.setTranslateZ(camera.getTranslateZ() + strafeZ + forwardZ);
    }


    private void normalizeRotation(Rotate rotation) {
        double angle = rotation.getAngle() % 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }
        rotation.setAngle(angle);
    }

    @Override
    public void start(Stage stage) throws IOException {
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


//        Camera camera = new PerspectiveCamera(true);
//        camera.setFarClip(20_000);
//        camera.setTranslateZ(-800);
//        scene.setCamera(camera);
//
//        Rotate cameraRotationX = new Rotate(0, Rotate.X_AXIS);
//        Rotate cameraRotationY = new Rotate(0, Rotate.Y_AXIS);
//
//        camera.getTransforms().addAll(cameraRotationX, cameraRotationY);
//
//        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
//            switch (keyEvent.getCode()) {
//                case W:
//                    // glitched
//                    moveCamera(camera, 0, 0, 50);
//                    break;
//                case S:
//                    // glitched
//                    moveCamera(camera, 0, 0, -50);
//                    break;
//                case A:
//                    // glitched
//                    moveCamera(camera, -50, 0, 0);
//                    break;
//                case D:
//                    // glitched
//                    moveCamera(camera, 50, 0, 0);
//                    break;
//                case UP:
//                    // glitched
//                    cameraRotationX.setAngle(cameraRotationX.getAngle() + 5);
//                    normalizeRotation(cameraRotationX);
//                    break;
//                case DOWN:
//                    // glitched
//                    cameraRotationX.setAngle(cameraRotationX.getAngle() - 5);
//                    normalizeRotation(cameraRotationX);
//                    break;
//                case RIGHT:
//                    cameraRotationY.setAngle(cameraRotationY.getAngle() + 5);
//                    normalizeRotation(cameraRotationY);
//                    break;
//                case LEFT:
//                    cameraRotationY.setAngle(cameraRotationY.getAngle() - 5);
//                    normalizeRotation(cameraRotationY);
//                    break;
//            }
//        });


        buildCamera();
        handleKeyboard(scene);
        handleMouse(scene);

        scene.setCamera(camera.getCamera());


        stage.setTitle("JavaFX Stage");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
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

    private static final double SHIFT_MULTIPLIER = 60.0;
    private static final double ROTATION_SPEED = 0.1;
    private final Group root = new Group();
    private final Camera camera = new Camera();

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
    }

    /**
     * Create mouse handlers
     * @param scene
     */
    private void handleMouse(Scene scene){

        scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
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
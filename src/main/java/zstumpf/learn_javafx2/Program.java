package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.util.List;

public class Program extends Application {
    private final static double WIDTH = 1000;
    private final static double HEIGHT = 600;

    private static final double SHIFT_MULTIPLIER = 60.0;
    private static final double ROTATION_SPEED = 0.1;
    private final Group root3d = new Group();
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
        root3d.getChildren().add(camera.getXForm());
    }

    /**
     * Create mouse handlers
     * @param scene
     */
    private void handleMouse(SubScene scene){

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
    private void handleKeyboard(SubScene scene) {
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
        // Move this stuff later
        Obstacle baseplate = new Obstacle(0,100,0,2000, 1, 2000);
        root3d.getChildren().add(baseplate);

        Target target0 = new Target(1, 0, 0, 0);
        root3d.getChildren().add(target0);

//        Target target1 = new Target(2, 400, 0, 0);
//        root3d.getChildren().add(target1);

        Obstacle wall = new Obstacle(200, 0, 0, 20, 300, 750);
        root3d.getChildren().add(wall);
        // .......



        // Lines are indented to represent hierarchy in the scene graph.
        SplitPane splitPane = new SplitPane();

            // For expected functionality, SplitPane requires that SubScene is inside a layout node, such as Pane.
            StackPane mapPane = new StackPane();
            mapPane.setMinWidth(0);

                Label mapTitle = new Label("Map");
                StackPane.setAlignment(mapTitle, Pos.TOP_LEFT);

                // Values of width and height don't matter because these properties will be binded to map3D's container.
                SubScene map3D = new SubScene(root3d, 0, 0, true, SceneAntialiasing.BALANCED);

                map3D.heightProperty().bind(mapPane.heightProperty()); // make SubScene fill available height
                map3D.widthProperty().bind(mapPane.widthProperty()); // make SubScene fill available width

                // Giving a SubScene a fill enables mouse events to register when clicking/dragging the "sky"
                map3D.setFill(Color.TRANSPARENT);

                handleKeyboard(map3D);
                handleMouse(map3D);
                buildCamera();
                map3D.setCamera(camera.getCamera()); // perspective camera

                // Send inputs to map3D on start and when map3D is clicked.
                map3D.requestFocus();
                map3D.setOnMouseClicked(event -> map3D.requestFocus());

                // This pane displays 2d labels for targets.
                // It will be placed on top of the 3d SubScene.
                Pane targetLabels = new Pane();
                targetLabels.setMouseTransparent(true); // Mouse clicks "go through" labels pane to 3D subscene
            mapPane.getChildren().addAll(map3D, targetLabels, mapTitle);

            VBox distancesPane = new VBox();
                Label distancesTitle = new Label("Distances");

                CheckBox showTargetIDsCheckBox = new CheckBox("Show target IDs");
                showTargetIDsCheckBox.setSelected(true);
                showTargetIDsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    for (Target target : Target.allTargets) {
                        target.renderId(newValue, map3D, camera, targetLabels);
                    }
                });

                GridPane distancesTable = new GridPane();
                distancesTable.setGridLinesVisible(true);
                for (int i = 0; i < Target.allTargets.size(); i++) {
                    Target target = Target.allTargets.get(i);

                    // Even though Labels contain the same information, they must be separate nodes
                    // to be added to two different spots in the GridPane.
                    Label targetLabelTopRow = new Label(String.valueOf(target.id));
                    Label targetLabelLeftCol = new Label(String.valueOf(target.id));

                    // 10px padding on all sides
                    targetLabelTopRow.setPadding(new Insets(10));
                    targetLabelLeftCol.setPadding(new Insets(10));

                    // Top row. i+1 leaves first cell in top left corner blank
                    distancesTable.add(targetLabelTopRow, i+1, 0);

                    // Leftmost column
                    distancesTable.add(targetLabelLeftCol, 0, i+1);
                }

            distancesPane.getChildren().addAll(distancesTitle, showTargetIDsCheckBox, distancesTable);

        splitPane.getItems().addAll(mapPane, distancesPane);

        Scene scene = new Scene(splitPane, WIDTH, HEIGHT, true);





//        Pathfinder.runAStar(Target.allTargets);
//
//        Pathfinder.printAStarSolutionMatrix();
//
//        Pathfinder.renderPath(Pathfinder.aStarSolutionMatrix[1][0].shortestPath, root);

        // Final setup instructions
        stage.setTitle("3D Multipoint Pathfinder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
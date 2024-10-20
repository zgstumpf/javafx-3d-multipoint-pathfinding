package zstumpf.learn_javafx2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {
    private final static double SCENE_WIDTH = 1000;
    private final static double SCENE_HEIGHT = 600;

    // Camera for 3D SubScene
    private final FirstPersonCamera camera = new FirstPersonCamera();

    @Override
    public void start(Stage stage) throws IOException {
        Group root3d = new Group();
        // Move this stuff later
        Obstacle baseplate = new Obstacle(0,100,0,2000, 1, 2000);
        root3d.getChildren().add(baseplate);

        Target target0 = new Target("1", 0, 0, 0);
        root3d.getChildren().add(target0);

//        Target target1 = new Target("2", 400, 0, 0);
//        root3d.getChildren().add(target1);

        Obstacle wall = new Obstacle(200, 0, 0, 20, 300, 750);
        root3d.getChildren().add(wall);
        // .......



        // Lines are indented to represent hierarchy in the scene graph.
        SplitPane splitPane = new SplitPane();

            // For expected functionality, SplitPane requires that SubScene is inside a layout node, such as Pane.
            StackPane mapPane = new StackPane();
            mapPane.setMinWidth(0); // Fix bug where mapPane will expand but not shrink.

                Label mapTitle = new Label("Map");
                StackPane.setAlignment(mapTitle, Pos.TOP_LEFT);

                // Values of width and height don't matter because these properties will be binded to map3D's container.
                SubScene map3D = new SubScene(root3d, 0, 0, true, SceneAntialiasing.BALANCED);

                map3D.heightProperty().bind(mapPane.heightProperty()); // make SubScene fill available height
                map3D.widthProperty().bind(mapPane.widthProperty()); // make SubScene fill available width

                // Giving a SubScene a fill enables mouse events to register when clicking/dragging the "sky"
                map3D.setFill(Color.TRANSPARENT);

                camera.handleKeyboardInputs(map3D);
                camera.handleMouseInputs(map3D);
                map3D.setCamera(camera.getCamera()); // perspective camera

                // Send inputs to map3D on start and when map3D is clicked.
                map3D.requestFocus();
                map3D.setOnMouseClicked(event -> map3D.requestFocus());

                // This pane displays 2d labels for targets.
                // It will be placed on top of the 3d SubScene.
                Pane targetLabels = new Pane();
                targetLabels.setMouseTransparent(true); // Mouse clicks "go through" labels pane to 3D subscene

                Button homeButton = new Button("Home");
                StackPane.setAlignment(homeButton, Pos.BOTTOM_RIGHT);
            mapPane.getChildren().addAll(map3D, targetLabels, mapTitle, homeButton);

            VBox distancesPane = new VBox();
                Label distancesTitle = new Label("Distances");

                CheckBox targetNamesCheckBox = new CheckBox("Show target names");
                targetNamesCheckBox.setSelected(true); // Default state is checked

                // User can click checkbox to toggle rendered labels
                targetNamesCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Target.renderLabelAll(newValue, map3D, targetLabels);
                });

                CheckBox showDiagnosticsCheckBox = new CheckBox("Show diagnostics");

                CheckBox pathfindingInActionCheckBox = new CheckBox("See pathfinding in action");

                GridPane distancesTable = new GridPane();
                distancesTable.setGridLinesVisible(true);
                for (int i = 0; i < Target.getAll().size(); i++) {
                    Target target = Target.getAll().get(i);

                    // Even though Labels contain the same information, they must be separate nodes
                    // to be added to two different spots in the GridPane.
                    Label targetLabelTopRow = new Label(target.getName());
                    Label targetLabelLeftCol = new Label(target.getName());

                    // 10px padding on all sides
                    targetLabelTopRow.setPadding(new Insets(10));
                    targetLabelLeftCol.setPadding(new Insets(10));

                    // Top row. i+1 leaves first cell in top left corner blank
                    distancesTable.add(targetLabelTopRow, i+1, 0);

                    // Leftmost column
                    distancesTable.add(targetLabelLeftCol, 0, i+1);
                }

                Button calculateAllButton = new Button("Calculate all");

            distancesPane.getChildren().addAll(distancesTitle, targetNamesCheckBox, showDiagnosticsCheckBox, pathfindingInActionCheckBox, calculateAllButton, distancesTable);

        splitPane.getItems().addAll(mapPane, distancesPane);
        Scene scene = new Scene(splitPane, SCENE_WIDTH, SCENE_HEIGHT, true);




//        Pathfinder.runAStar(Target.allTargets);
//
//        Pathfinder.printAStarSolutionMatrix();
//
//        Pathfinder.renderPath(Pathfinder.aStarSolutionMatrix[1][0].shortestPath, root);

        // Final setup instructions
        stage.setTitle("3D Multipoint Pathfinder");
        stage.setScene(scene);

        // Since default "show labels" checkbox state is checked, when application starts, render all labels.
        // stage.setOnShown renders labels after all node positioning is computed, which ensures labels
        // are in the right locations.
        stage.setOnShown(event -> {
            Target.renderLabelAll(true, map3D, targetLabels);
        });

        // If the user moves the camera, targets may appear to move from their original screen positions,
        // which may cause their labels to no longer be in the right spots.
        // NOTE: addEventFilter prevents screen from freezing due to blocking map3D's event listeners for camera
        // movements. The behavior is complicated, read about it here, if you want: https://docs.oracle.com/javafx/2/events/processing.htm
        // Re-render the labels if the camera is rotated (by dragging).
        map3D.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (targetNamesCheckBox.selectedProperty().get()) {
                Target.renderLabelAll(true, map3D, targetLabels);
            }
        });
        // Re-render the labels if the camera is moved (WASD or Space/Control)
        map3D.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (targetNamesCheckBox.selectedProperty().get()) {
                switch (event.getCode()) {
                    // Fall-through if any cases are matched.
                    case W:
                    case A:
                    case S:
                    case D:
                    case SPACE:
                    case CONTROL:
                        Target.renderLabelAll(true, map3D, targetLabels);
                }
            }
        });

        // Re-render labels if camera is moved by pressing Home button.
        homeButton.setOnMouseClicked((event) -> {
            camera.resetCamera();
            if (targetNamesCheckBox.selectedProperty().get()) {
                Target.renderLabelAll(true, map3D, targetLabels);
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
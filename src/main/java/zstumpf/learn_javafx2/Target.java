package zstumpf.learn_javafx2;

import javafx.geometry.Point2D;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * A Target is a point or node that the pathfinder must visit at least once. It is visually represented by a small sphere.
 */
public class Target extends Sphere {
    public static List<Target> allTargets = new ArrayList<>();

    // The id, or identifier, is used to tell Targets apart.
    public final int id;

    // The Point3D associated with the Target
    public Point3D point3D;

    public Target(int id, double translateX, double translateY, double translateZ){
        this.id = id;

        // Position
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
        this.setTranslateZ(translateZ);

        // Other Properties
        this.setRadius(10);
        // opacity not working...

        // Color
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.CORNFLOWERBLUE);
        this.setMaterial(material);

        // Set Point3D in center of Target
        this.point3D = new Point3D(this.getTranslateX(), this.getTranslateY(), this.getTranslateZ());

        // Keep track of this target
        allTargets.add(this);
    }

    /**
     * Show the target's id on the map.
     * @param render
     * @param map3D
     */
    public void renderId(boolean render, SubScene map3D, Camera camera, Pane labelPane) {
        Point3D targetPosition3d = this.point3D;

        XForm cameraXForm = camera.getXForm();

        // Stores translation and rotation data of the camera
        Affine cameraTransform = new Affine(); // cameraTransform represents where the target appears to be, I think...

        // As camera moves one direction, the object appears to move in the opposite direction.
        // Here, apply where the object appears to be.
        cameraTransform.appendTranslation(-1 * cameraXForm.t.getX(), -1 * cameraXForm.t.getY(), -1 * cameraXForm.t.getZ());

        // Get 3D point of camera's current position
        Point3D cameraCurrentPos3d = new Point3D(cameraXForm.t.getX(), cameraXForm.t.getY(), cameraXForm.t.getZ());

        cameraTransform.appendRotation(cameraXForm.rx.getAngle(), cameraCurrentPos3d, Rotate.X_AXIS);
        cameraTransform.appendRotation(cameraXForm.ry.getAngle(), cameraCurrentPos3d, Rotate.Y_AXIS);
        cameraTransform.appendRotation(cameraXForm.rz.getAngle(), cameraCurrentPos3d, Rotate.Z_AXIS);

        // Applies the same translation and rotation state of the camera to the target's 3D position
        Point3D transformedPosition = cameraTransform.transform(targetPosition3d);
        System.out.println("transformedPosition: " + transformedPosition);

        // ChatGPT says z scaling is crucial for projecting 3d coordinates to 2d. ... I think this is just glitchy.
        double scaleFactor = 2.0 / (transformedPosition.getZ() + 1); // Add 1 to avoid division by zero
        double x2D = transformedPosition.getX() * scaleFactor + (map3D.getWidth() / 2);
        double y2D = -1 * transformedPosition.getY() * scaleFactor + (map3D.getHeight() / 2); // Invert Y for screen coordinates
        System.out.println("Place label at x " + x2D + " and y " + y2D);

        if (render) {
            Label targetLabel = new Label(" " + this.id);
            targetLabel.setLayoutX(x2D);
            targetLabel.setLayoutY(y2D);
            labelPane.getChildren().add(targetLabel);
        }
    }

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", point3D=" + point3D.toString() +
                '}';
    }
}

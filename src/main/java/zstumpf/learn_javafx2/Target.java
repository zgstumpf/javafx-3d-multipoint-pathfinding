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
import javafx.scene.transform.Translate;

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
        for (Target target : Target.allTargets) {
            Point3D coords3D = target.localToScene(Point3D.ZERO, true);
            System.out.println("3d target Coordinates: " + coords3D.toString());

            //Clipping Logic
            //if coordinates are outside of the scene it could
            //stretch the screen so don't transform them
            double x = coords3D.getX();
            double y = coords3D.getY();
            //is it left of the view?
            if(x < 0) {
                x = 0;
            }

            Label label = new Label(" " + this.id);

            //is it right of the view?
            if((x+label.getWidth()+5) > map3D.getWidth()) {
                x = map3D.getWidth() - (label.getWidth()+5);
            }
            //is it above the view?
            if(y < 0) {
                y = 0;
            }
            //is it below the view
            if((y+label.getHeight()) > map3D.getHeight())
                y = map3D.getHeight() - (label.getHeight()+5);
            //@DEBUG SMP  useful debugging print
            //System.out.println("clipping Coordinates: " + x + ", " + y);
            //update the local transform of the label.
            label.getTransforms().setAll(new Translate(x, y));
            labelPane.getChildren().add(label);
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

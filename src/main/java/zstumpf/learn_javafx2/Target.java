package zstumpf.learn_javafx2;

import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import javafx.geometry.Point3D;

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

    public static void renderAllIds(boolean render) {
        for (Target target : Target.allTargets) {
            target.renderId(render);
        }
    }

    public void renderId(boolean render) {

    }

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", point3D=" + point3D.toString() +
                '}';
    }
}

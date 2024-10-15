package zstumpf.learn_javafx2;

import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;

/**
 * A Target is a point or node that the pathfinder must visit at least once. It is visually represented by a small sphere.
 */
public class Target extends Sphere {
    public Target(double translateX, double translateY, double translateZ){
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
    }
}

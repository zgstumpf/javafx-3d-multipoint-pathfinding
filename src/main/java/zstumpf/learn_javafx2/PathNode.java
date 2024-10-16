package zstumpf.learn_javafx2;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.List;

public class PathNode extends Sphere {
    public PathNode(double x, double y, double z) {

        // Position
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setTranslateZ(z);

        // Other Properties
        this.setRadius(5);
        // opacity not working...

        // Color
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.YELLOW);
        this.setMaterial(material);
    }

}

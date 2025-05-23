package zstumpf.learn_javafx2;

import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import javafx.geometry.Point3D;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

/**
 * A Target is a point or node that the pathfinder must visit at least once. It is visually represented by a small sphere.
 */
public class Target extends Sphere {
    // Internal List to keep track of all Target instances that exist on the map.
    private static List<Target> all = new ArrayList<>();

    // The name is used to tell Targets apart.
    private final String name;

    // The target's label that appears on the map.
    private Label label;

    // The Point3D associated with the Target
    public Point3D point3D;

    /**
     *
     * @param name Name used to identify this target. Should be unique for all Target instances.
     * @param translateX JavaFX 3D X-coordinate for where to place this target.
     * @param translateY JavaFX 3D Y-coordinate for where to place this target.
     * @param translateZ JavaFX 3D Z-coordinate for where to place this target.
     */
    public Target(String name, double translateX, double translateY, double translateZ){
        this.name = name;
        this.label = new Label(this.name);

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
        all.add(this);
    }

    /**
     *
     * @return a List of all Target instances that exist on the map.
     */
    public static List<Target> getAll() {
        return Target.all;
    }

    /**
     * @return name of this target
     */
    public String getName() {
        return this.name;
    }

    /**
     * Controls visibility of the target's label on the map.
     * @param render true to make label visible; false to make label invisible.
     * @param map3D The SubScene containing the target.
     * @param labelPane The Pane that has labels as children. This Pane must overlap the SubScene, so
     *                  any labels on the Pane will also appear to be on the SubScene.
     */
    public void renderLabel(boolean render, SubScene map3D, Pane labelPane) {
        // Some of the code in this method is modified code from the following source:
        // https://github.com/FXyz/FXyz/blob/master/FXyz-Samples/src/main/java/org/fxyz3d/samples/utilities/FloatingLabels.java
        //
        // Here is that source's license.
        /**
         * FloatingLabels.java
         *
         * Copyright (c) 2013-2017, F(X)yz
         * All rights reserved.
         *
         * Redistribution and use in source and binary forms, with or without
         * modification, are permitted provided that the following conditions are met:
         *     * Redistributions of source code must retain the above copyright
         * notice, this list of conditions and the following disclaimer.
         *     * Redistributions in binary form must reproduce the above copyright
         * notice, this list of conditions and the following disclaimer in the
         * documentation and/or other materials provided with the distribution.
         *     * Neither the name of F(X)yz, any associated website, nor the
         * names of its contributors may be used to endorse or promote products
         * derived from this software without specific prior written permission.
         *
         * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
         * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
         * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
         * DISCLAIMED. IN NO EVENT SHALL F(X)yz BE LIABLE FOR ANY
         * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
         * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
         * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
         * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
         * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
         * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
         */

        if (!render) {
            // Hide labels
            this.label.setVisible(false);
            // We are done, end method.
            return;
        }

        Point3D coords3D = this.localToScene(Point3D.ZERO, true);

        //Clipping Logic
        //if coordinates are outside of the scene it could
        //stretch the screen so don't transform them
        double x = coords3D.getX();
        double y = coords3D.getY();
        //is it left of the view?
        if(x < 0) {
            x = 0;
        }

        //is it right of the view?
        if(x + this.label.getWidth() + 5 > map3D.getWidth()) {
            x = map3D.getWidth() - (this.label.getWidth() + 5);
        }
        //is it above the view?
        if(y < 0) {
            y = 0;
        }
        //is it below the view
        if(y + this.label.getHeight() > map3D.getHeight()){
            y = map3D.getHeight() - (this.label.getHeight() + 5);
        }
        //@DEBUG SMP  useful debugging print
        //System.out.println("clipping Coordinates: " + x + ", " + y);
        //update the local transform of the label.
        this.label.getTransforms().setAll(new Translate(x, y));

        // Make label visible in case it was made invisible when all labels were un-rendered.
        this.label.setVisible(true);

        // Add labels to scene if they don't exist. If they exist, change their position.
        boolean labelExists = false;
        for (Node node : labelPane.getChildren()) {
            // Not all nodes in labelPane are guaranteed to be Labels.
            if (node instanceof Label) {
                Label currentLabel = (Label) node;

                // This node is a Label, but is it this target's Label?
                if (currentLabel == this.label) {

                    // Label has already been added to scene. Position has already been changed,
                    // so there's nothing left to do.
                    labelExists = true;
                    break;
                }
            }
        }
        if (!labelExists) {
            // Label has not been added to scene yet, so add it.
            labelPane.getChildren().add(this.label);
        }
    }

    /**
     * Calls {@link #renderLabel(boolean, SubScene, Pane) renderLabel} for all targets on the map.
     */
    public static void renderLabelAll(boolean render, SubScene map3D, Pane labelPane) {
        for (Target target : Target.all) {
            target.renderLabel(render, map3D, labelPane);
        }
    }

    @Override
    public String toString() {
        return "Target{" +
                "name=" + name +
                ", point3D=" + point3D.toString() +
                '}';
    }
}

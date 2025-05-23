// Some of this code is modified code from the following source:
// https://github.com/vvoZokk/c-space-processing/:
//
// The license for that source is below.
/*
MIT License

Copyright (c) 2018 BMSTU

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
* */


package zstumpf.learn_javafx2;

import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;

/**
 * Class for 3D space camera presenting.
 * Presents PerspectiveCamera node in a scene with defined
 * far clip, near clip and field of view.
 * W,S,A,D, Ctrl, Space used for movement on a scene.
 * Mouse dragging (while pressed) used for rotation on a scene.
 * SHIFT modifier can be used to increase step modifier
 *
 * @author      Vladislav Khakin, Zach Stumpf
 * @see XForm
 * @see PerspectiveCamera
 */
public class FirstPersonCamera {
    private XForm xForm;
    private PerspectiveCamera camera;
    private boolean invertRotations = false;

    private final double FIELD_OF_VIEW  = 40.0;
    private final double CAMERA_NEAR_CLIP = 0.1;
    private final double CAMERA_FAR_CLIP = 10000.0;
    private final double CAMERA_MOVEMENT_UNIT = 25;
    private final double Z_POSITION = -1000;

    // Movement multiplier applied when user holds down Shift
    private static final double SHIFT_MULTIPLIER = 60.0;

    // These fields store data on mouse dragging for camera rotation
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double dx;
    private double dy;
    private static final double ROTATION_SPEED = 0.1;


    // return 1 if invertRotations else -1
    private int getRotationDirection(){
        return invertRotations ? 1: -1;
    }

    private void moveUnitForward(){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() + zCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTy(xForm.t.getY() - yCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitBackward(){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() - zCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTy(xForm.t.getY() + yCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitLeft(){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() + zCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitRight(){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() - zCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitUp(){
        xForm.setTy(xForm.t.getY() - CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitDown(){
        xForm.setTy(xForm.t.getY() + CAMERA_MOVEMENT_UNIT);
    }


    /**
     * Default constructor.
     * Set field of view, near and far clips, create
     * scene node and link Perspective camera with it
     * Use default rotation (not inverse)
     */
    public FirstPersonCamera(){
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(Z_POSITION);
        camera.setFieldOfView(FIELD_OF_VIEW);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        xForm = new XForm();
        xForm.getChildren().add(camera);
        invertRotations = false;
    }

    /**
     * Generate camera by default constructor and set inversed rotation
     * @see #FirstPersonCamera()
     * @param invertRotations if rotation should be applied with inversion
     */
    public FirstPersonCamera(boolean invertRotations){
        super();
        this.invertRotations = invertRotations;
    }

    /**
     * Return scene node of camera
     * @return XFrom
     */
    public XForm getXForm(){
        return xForm;
    }

    /**
     * Return PerspectiveCamera object in created camera
     * @return Camera base class of PerspectiveCamera
     */
    public javafx.scene.Camera getCamera(){
        return camera;
    }

    /**
     * Move camera forward in it's rotated direction. Straight where it "look's"
     * @param value - how far to move
     */
    public void moveForward(double value){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * value);
        xForm.setTz(xForm.t.getZ() + zCoord * value);
        xForm.setTy(xForm.t.getY() - yCoord * value);
    }

    /**
     * Move camera forward in it's rotated direction. Straight where it "look's"
     * Use default movement unit as step size
     */
    public void moveForward(){
        moveUnitForward();
    }

    /**
     * Move camera backward in it's rotated direction.
     * @param value - how far to move
     */
    public void moveBackward(double value){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * value);
        xForm.setTz(xForm.t.getZ() - zCoord * value);
        xForm.setTy(xForm.t.getY() + yCoord * value);
    }

    /**
     * Move camera backward in it's rotated direction.
     * Use default movement unit as step size
     */
    public void moveBackward(){
        moveUnitBackward();
    }

    /**
     * Move camera left in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveLeft(double value){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * value);
        xForm.setTz(xForm.t.getZ() + zCoord * value);
    }

    /**
     * Move camera left in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveLeft(){
        moveUnitLeft();
    }

    /**
     * Move camera right in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveRight(double value){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * value);
        xForm.setTz(xForm.t.getZ() - zCoord * value);
    }

    /**
     * Move camera right in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveRight(){
        moveUnitRight();
    }

    /**
     * Move camera up in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveUp(double value){
        xForm.setTy(xForm.t.getY() - value);
    }

    /**
     * Move camera up in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveUp(){
        moveUnitUp();
    }

    /**
     * Move camera down in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveDown(double value){
        xForm.setTy(xForm.t.getY() + value);
    }

    /**
     * Move camera down in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveDown(){
        moveUnitDown();
    }



    /**
     * Rotate camera around x axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateX(double angle){
        xForm.setRotateX(xForm.rx.getAngle() + getRotationDirection() * angle);

    }

    /**
     * Rotate camera around y axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateY(double angle){
        xForm.setRotateY(xForm.ry.getAngle() - getRotationDirection() * angle);
    }

    /**
     * Rotate camera around z axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateZ(double angle){
        xForm.setRotateZ(xForm.rz.getAngle() + getRotationDirection() * angle);

    }

    /**
     * Resets the camera's position and rotation to how it is when the application starts.
     */
    public void resetCamera() {
        xForm.reset();
    }

    /**
     * Makes the subScene's camera move when there are keyboard inputs, such as WASD, Space (Up), Control (Down),
     * and Escape (Exit application).
     * @param subScene SubScene containing 3D elements and camera
     */
    // NOTE: You could make this work if you wanted to use argument of type Scene.
    public void handleKeyboardInputs(SubScene subScene) {
        subScene.setOnKeyPressed(event -> {
            if(!event.isShiftDown()) {
                // Normal movements
                switch (event.getCode()) {
                    case W:
                        this.moveForward();
                        break;
                    case S:
                        this.moveBackward();
                        break;
                    case A:
                        this.moveLeft();
                        break;
                    case D:
                        this.moveRight();
                        break;
                    case SPACE:
                        this.moveUp();
                        break;
                    case CONTROL:
                        this.moveDown();
                        break;
                    case ESCAPE:
                        System.exit(0);
                        break;
                }
            }
            else {
                // Shift is held down - accelerate movements
                switch (event.getCode()) {
                    case W:
                        this.moveForward(SHIFT_MULTIPLIER);
                        break;
                    case S:
                        this.moveBackward(SHIFT_MULTIPLIER);
                        break;
                    case A:
                        this.moveLeft(SHIFT_MULTIPLIER);
                        break;
                    case D:
                        this.moveRight(SHIFT_MULTIPLIER);
                        break;
                    case SPACE:
                        this.moveUp(SHIFT_MULTIPLIER);
                        break;
                    case CONTROL:
                        this.moveDown(20);
                        break;
                }
            }
        });
    }


    /**
     * Makes the subScene's camera rotate when the camera is dragged.
     * @param subScene SubScene containing 3D elements and camera
     */
    // NOTE: You could make this work if you wanted to use argument of type Scene.
    public void handleMouseInputs(SubScene subScene){
        subScene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
        });

        subScene.setOnMouseDragged(event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            dx = mousePosX - mouseOldX;
            dy = mousePosY - mouseOldY;

            this.rotateX(dy * ROTATION_SPEED);
            this.rotateY(dx * ROTATION_SPEED);
        });

    }

}
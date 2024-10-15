package zstumpf.learn_javafx2;

import javafx.geometry.Point3D;
import javafx.scene.shape.Box;

import java.util.List;
import java.util.ArrayList;

/**
 * An obstacle is a 3D shape that cannot be pathfinded through. Obstacles are rectangular prisms (JavaFX Box).
 */
public class Obstacle extends Box {
    // Keeps track of all Obstacle instances that exist in the Scene since
    // the A* search algorithm needs to be aware of all obstacles.
    public static List<Obstacle> allObstacles = new ArrayList<>();

    public Obstacle(double translateX, double translateY, double translateZ, double width, double height, double depth) {
        this.setWidth(width);
        this.setHeight(height);
        this.setDepth(depth);
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
        this.setTranslateZ(translateZ);

        // Keep track of this instance
        allObstacles.add(this);
    }

    /**
     * Determines if the 3D point is inside the 3D obstacle.
     * This is used to detect points that are impossible to travel to because
     * they are inside the obstacle object.
     * @param point Point3D instance
     * @return true if point is inside obstacle; false otherwise
     */
    public boolean contains(Point3D point) {

        // Basic obstacle geometry data
        double width = this.getWidth();
        double height = this.getHeight();
        double depth = this.getDepth();
        Point3D obstacleCenter = new Point3D(this.getTranslateX(), this.getTranslateY(), this.getTranslateZ());

        // Advanced obstacle geometry data
        double rightSideOfObstacle = obstacleCenter.getX() + (width / 2);
        double leftSideOfObstacle = obstacleCenter.getX() - (width / 2);
        // In JavaFX, Y values get more positive as you go down the Y axis.
        double bottomOfObstacle = obstacleCenter.getY() + (height / 2);
        double topOfObstacle = obstacleCenter.getY() - (height / 2);
        double nearSideOfObstacle = obstacleCenter.getZ() - (depth / 2);
        double farSideOfObstacle = obstacleCenter.getZ() + (depth / 2);

        // Determine if point coordinates are within all sides of Obstacle
        return (point.getX() >= leftSideOfObstacle &&
                point.getX() <= rightSideOfObstacle &&
                point.getY() <= bottomOfObstacle &&
                point.getY() >= topOfObstacle &&
                point.getZ() >= nearSideOfObstacle &&
                point.getZ() <= farSideOfObstacle);

    }
}

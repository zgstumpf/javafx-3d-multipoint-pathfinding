package zstumpf.learn_javafx2;

import javafx.geometry.Point3D;
import javafx.scene.Group;

import java.util.List;

public class Pathfinder {
    public static AStarSearch.AStarSolution[][] aStarSolutionMatrix = new AStarSearch.AStarSolution[Target.getAll().size()][Target.getAll().size()];

    /**
     * Use A* algorithm to compute the shortest path for each Target to every other Target.
     */
    public static void runAStar(List<Target> targets) {
        int N = targets.size();

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Only compute for i != j and store in both (i, j) and (j, i)
                AStarSearch.AStarSolution aStarSolution = AStarSearch.getShortestPath(targets.get(i), targets.get(j));
                aStarSolutionMatrix[i][j] = aStarSolution;
                aStarSolutionMatrix[j][i] = aStarSolution;  // Symmetry: copy to the other side
            }
        }

        for (int i = 0; i < N; i++) {
            // setting this to null may be a bad idea - in AStarSearch.getShortestPath, if a path is impossible,
            // it returns a solution with an empty path list, not null.
            aStarSolutionMatrix[i][i] = null;  // Distance to self is zero
        }
    }
    // Clean this garbage up
    public static void printAStarSolutionMatrix() {
        for (int i = 0; i < aStarSolutionMatrix.length; i++) {
            for (int j = 0; j < aStarSolutionMatrix[i].length; j++) {
                System.out.print((aStarSolutionMatrix[i][j] != null ? aStarSolutionMatrix[i][j].totalDistance : "null") + " ");
            }
            System.out.println();
        }
    }

    public static void renderPath(List<Point3D> path, Group group) {
        for (Point3D point : path) {
            PathNode pathNode = new PathNode(point.getX(), point.getY(), point.getZ());
            group.getChildren().add(pathNode);
        }
    }


}

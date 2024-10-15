package zstumpf.learn_javafx2;

import javafx.geometry.Point3D;

import java.util.*;

/**
 * Handles operations for the A* search algorithm.
 */
public class AStarSearch {
    // Number of JavaFX 3D units to move when searching for neighbors.
    // A lower value means a more precise path, but calculations will be slower.
    private static final double NEIGHBOR_DISTANCE = 50;

    /**
     * A node in the A* search algorithm. Nodes are associated with points, but nodes store
     * additional data that will be used during the A* search.
     */
    private static class Node {
        Point3D point;
        Node parent;

        double g; // the distance of the path from the start node (Target t1) to this node
        double h; // the estimated distance from this node to the goal end node (Target t2)

        public Node(Point3D point, Node parent, double g, double h) {
            this.point = point;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }

        /**
         * Total distance function
         * @return estimated total distance from the start node to the end node, if this node is used in the path.
         */
        public double f() {
            return this.g + this.h;
        }
    }

    /**
     * Performs the A* search algorithm in 3D space between start and goal while avoiding obstacles.
     * @param start First Target instance. Where path starts.
     * @param goal Second Target instance. Where the path is aiming to end.
     * @return List of Point3D instances forming the shortest path. If List is empty, no path was found.
     */
    public static List<Point3D> getShortestPath(Target start, Target goal) {
        Set<Point3D> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::f));
        openSet.add(new Node(start.point3D, null, 0, getDistance(start.point3D, goal.point3D)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // current node has found the goal
            if (current.point.getX() == goal.point3D.getX() && current.point.getY() == goal.point3D.getY() && current.point.getZ() == goal.point3D.getZ()) {
                return reconstructPath(current);
            }

            closedSet.add(current.point);

            // Explore neighboring points in 3 dimensions
            for (Point3D neighbor : getNeighbors(current.point)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                boolean blockedByObstacle = false;
                for (Obstacle obstacle : Obstacle.allObstacles) {
                    if (obstacle.contains(neighbor)) {
                        blockedByObstacle = true;
                        break;
                    }
                }

                if (blockedByObstacle) {
                    // Since this neighboring point is blocked, add it to the closed set
                    // so it won't be visited again.
                    closedSet.add(neighbor);
                    continue; // keep searching through other neighbors
                }

                // Found a viable neighboring node
                Node neighborNode = new Node(neighbor, current, current.g + NEIGHBOR_DISTANCE, getDistance(neighbor, goal.point3D));
                openSet.add(neighborNode);
            }
        }
        // No path found
        return new ArrayList<Point3D>();
    }


    /**
     * Given a point, returns the 6 neighboring points in 3D space (right, left, top, bottom, near, far)
     * A* will navigate to each neighbor as it searches for the shortest path.
     * @param point Point3D point to find neighbors of
     * @return list of 6 neighboring points
     */
    private static List<Point3D> getNeighbors(Point3D point) {
        // can I use regular Array here?
        List<Point3D> neighbors = new ArrayList<>();

        // neighboring point on the right side
        neighbors.add(new Point3D(point.getX() + NEIGHBOR_DISTANCE, point.getY(), point.getZ()));

        // neighboring point on the left side
        neighbors.add(new Point3D(point.getX() - NEIGHBOR_DISTANCE, point.getY(), point.getZ()));

        // neighboring point on the top side
        neighbors.add(new Point3D(point.getX(), point.getY() - NEIGHBOR_DISTANCE, point.getZ()));

        // neighboring point on the bottom side
        neighbors.add(new Point3D(point.getX(), point.getY() + NEIGHBOR_DISTANCE, point.getZ()));

        // neighboring point on the near side
        neighbors.add(new Point3D(point.getX(), point.getY(), point.getZ() - NEIGHBOR_DISTANCE));

        // neighboring point on the far side
        neighbors.add(new Point3D(point.getX(), point.getY(), point.getZ() - NEIGHBOR_DISTANCE));

        return neighbors;
    }


    /**
     * Calculates distance in 3 dimensions between points p1 and p2, ignoring obstacles.
     * @param p1 First point
     * @param p2 Second point
     * @return double
     */
    private static double getDistance(Point3D p1, Point3D p2) {
        return Math.sqrt( Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2) );
    }


    /**
     * Returns list of points that represent path between current Node and the starting target.
     * @param current
     * @return
     */
    private static List<Point3D> reconstructPath(Node current) {
        List<Point3D> path = new ArrayList<>();
        while (current != null) {
            path.add(current.point);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}

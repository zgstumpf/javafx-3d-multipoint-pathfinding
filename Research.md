# 3D Pathfinding Research

Goal: Given a set of points in 3D space, including a start point and end point, draw a line starting at start point that visits all points, ending at end point, with the minimum distance travelled.

1. Use A* algorithm to compute the shortest path for each waypoint to every other waypoint. This will give a distance matrix
2. Solve Travelling Salesman Problem (TSP) on the resulting distance matrix to find the optimal order of visiting the waypoints. There are external TSP solver libraries.
3. Reconstruct the full path by combining the A* paths between consecutive waypoints in the TSP solution.

Research:
For TSP, a greedy approach must be used (time O(n^2)). An optimal algorithm will be way too slow. [Source](https://www.w3schools.com/dsa/dsa_ref_traveling_salesman.php)

## A*

Remember A* must be performed between each point and every other point, for all points.

Requires:
* x, y, z Start coordinates
* z, y, z End Coordinates
* List of Obstacles, where each obstacle consists of a bounding box represented by:
  * start x, y, z
  * end x, y, z

This definition of obstacles works on rectangular prisms / Boxes
May need a more sophisticated algorithm to take any javafx shape and determine if a point is inside it.
But should probably just get it to work with Boxes for now.
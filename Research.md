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

Since resulting shortest paths of targets likely won't change often, you can cache the results, and recalculate if targets are modified.
Use 2nd part of pathfinding algorithm on cached results. When a target is modified, only update A star solutions where that target is a start or end target.

Will this A* solution work if (target x, y, z % NEIGHBOR_DISTANCE != 0) ?
Will A* work if obstacle is too skinny, meaning nodes will skip over it?

3D designer for docs: https://figuro.io/Designer

- Look into NEIGHBOR_DISTANCE - Even a value of 25 causes Java Heap to run out of space, with just 2 targets.

2nd Phase Thoughts
- Find variant of TPS with start point, end point, and multiple waypoints that can be visited in any order
  - This may be way too slow, so use a greedy algorithm.
  - Maybe from any node, visit closest node in 3d space, ignoring obstacles.
    - Problem occurs if map is like:
      - o  o  o    o
      - ------------
      - o o o o
    - Since algorithm may want to hop over the wall over and over, instead of doing 2 passes.
    - So, maybe go to closest node that isnt blocked by obstacle - will need to modify a star solution to also store if path is blocked by obstacle
    - If you run out of nodes that arent blocked, next step is
        1. Method 1: Use A star distance table. From that point, go to next needed point with shortest distance in table. (Greedy)
    - Greedy to solve whole problem: From starting point, visit node that has shortest distance in A star table. Repeat process until all needed nodes are visited. 
    - Permutation way to solve whole problem: For small number of targets, try every permutation, pick path with shortest distance.
    - Should use permutation for problems with few targets, and if many targets, switch to different algorithm.
On program start, it should render obstacles and targets

2 Tabs
First tab is Map : shows 3d map
Second tab is Distances
- Shows table of shortest distance between every target. Column/Row labels are Target IDs. Target IDs should also be toggleable to be visible in the map.
- Has Button "Calculate All", that does A* between all nodes and fills in table as results are calculated.
- Or, user can click empty cell to make toolbar appear. One option is "Calculate this path", which finds the path between the two nodes shared by that cell.
- There should be two checkboxes. One is "Show diagnostics", other is "See pathfinding in action". "Show diagnostics" should send data about what is happening to the screen, such as what targets are being pathfinded, how many/what calculations have been performed, # nodes in open/closed set, how much RAM stack/heap is used, # objects initialized, time spent running. "See pathfinding in action" should render PathNodes as A* searches in 6 directions. Closed set pathnodes should not be rendered (for now), pathnodes should be a diff color, when the A* path is find, make other nodes disappear and color path nodes the normal yellow. If "see pathfindin in action is checked", there should be a few second pause once the A* path is found before moving on to next targets. If user only did 1 cell, path should stay.
- If you click on a cell, that path between two nodes should be rendered on the map.
- Multiple cells can be clicked, and the path will stay highlighted. Clicking a clicked cell unrenders path.
- Bonus: Button to render all paths, between all nodes. Maybe each path should have a different color.
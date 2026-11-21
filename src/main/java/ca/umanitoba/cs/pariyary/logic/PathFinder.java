package ca.umanitoba.cs.pariyary.logic;
import ca.umanitoba.cs.pariyary.model.Map;
import ca.umanitoba.cs.pariyary.model.Coordinates;
import java.util.*;
import com.google.common.base.Preconditions;

public class PathFinder {

    private final Map world;

    public PathFinder(Map world) {
        Preconditions.checkNotNull(world, "world cannot be null");
        this.world = world;
        checkPathFinder();
    }

    public List<Coordinates> findPath(Coordinates start,
                                      Coordinates end,
                                      Collection<Coordinates> coords) {

        Preconditions.checkNotNull(start, "start cannot be null");
        Preconditions.checkNotNull(end, "end cannot be null");
        Preconditions.checkNotNull(coords, "coords cannot be null");
        Preconditions.checkState(world.coordWithinMap(start), "start must be within map");
        Preconditions.checkState(world.coordWithinMap(end), "end must be within map");

        checkPathFinder();

        List<Coordinates> path = new ArrayList<>();
        Set<Coordinates> coordsNoDup = new HashSet<>(coords);

        boolean found = false;
        PathNode endNode = null;

        if (coordsNoDup.contains(start) && coordsNoDup.contains(end)) {

            Stack<PathNode> stack = new LinkedListStack<>();
            Set<Coordinates> visited = new HashSet<>();

            stack.push(new PathNode(start, null));

            while (!stack.isEmpty() && !found) {

                PathNode currNode = stack.pop();
                Coordinates curr = currNode.getCoords();

                if (!visited.contains(curr)) {

                    visited.add(curr);

                    if (curr.equals(end)) {
                        found = true;
                        endNode = currNode;
                    } else {

                        List<Coordinates> neighbours = getNeighbours(curr);

                        for (Coordinates neighbour : neighbours) {
                            if (!visited.contains(neighbour)
                                    && coordsNoDup.contains(neighbour)) {

                                stack.push(new PathNode(neighbour, currNode));
                            }
                        }
                    }
                }
            }
        }

        if (found) {
            path = createPath(endNode);
        }

        return path;
    }

    private List<Coordinates> createPath(PathNode endNode) {

        LinkedList<Coordinates> path = new LinkedList<>();
        PathNode current = endNode;

        while (current != null) {
            path.addFirst(current.getCoords());
            current = current.getPrev();
        }

        return path;
    }

    private static class PathNode {

        private final Coordinates coords;
        private final PathNode prev;

        public PathNode(Coordinates coord, PathNode prev) {
            this.coords = coord;
            this.prev = prev;
        }

        public Coordinates getCoords() {
            return coords;
        }

        public PathNode getPrev() {
            return prev;
        }
    }

    private List<Coordinates> getNeighbours(Coordinates coordinate) {

        List<Coordinates> neighbours = new ArrayList<>();

        Coordinates up = new Coordinates(coordinate.getX(), coordinate.getY() - 1);
        Coordinates down = new Coordinates(coordinate.getX(), coordinate.getY() + 1);
        Coordinates left = new Coordinates(coordinate.getX() - 1, coordinate.getY());
        Coordinates right = new Coordinates(coordinate.getX() + 1, coordinate.getY());

        if (world.coordWithinMap(up)) {
            neighbours.add(up);
        }

        if (world.coordWithinMap(down)) {
            neighbours.add(down);
        }

        if (world.coordWithinMap(left)) {
            neighbours.add(left);
        }

        if (world.coordWithinMap(right)) {
            neighbours.add(right);
        }

        return neighbours;
    }

    private void checkPathFinder() {
        Preconditions.checkNotNull(world, "world can't be null");
    }
}
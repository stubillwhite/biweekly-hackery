package snakegame.internal.pathfinding;

import com.google.common.collect.Queues;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pathfinder<T> {

    private final PriorityQueue<CostedNode<T>> closedList = Queues.newPriorityQueue();
    private final PriorityQueue<CostedNode<T>> openList = Queues.newPriorityQueue();

    private final CostFunction<T> heuristicFunction;
    private final CostFunction<T> moveFunction;

    public interface CostFunction<T> {
        double calculateCost(T start, T end);
    }

    public interface NeighbourFunction<T> {
        Set<T> neighboursOf(T node);
    }

    private static class CostedNode<T> implements Comparable<CostedNode<T>> {
        public final T node;
        public final double heuristicCost;
        public final double moveCost;
        public final CostedNode<T> parent;

        public CostedNode(T node,
                          double heuristicCost,
                          double moveCost,
                          CostedNode<T> parent) {
            this.node = node;
            this.heuristicCost = heuristicCost;
            this.moveCost = moveCost;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return node.toString();
        }

        @Override
        public int compareTo(CostedNode other) {
            return Double.compare(this.heuristicCost + this.moveCost, other.heuristicCost + other.moveCost);
        }
    }

    public Pathfinder(CostFunction<T> heuristicFunction,
                      CostFunction<T> moveFunction) {
        this.heuristicFunction = heuristicFunction;
        this.moveFunction = moveFunction;
    }

    public List<T> findPath(T start,
                            T end,
                            NeighbourFunction<T> neighbourFunction) {
        openList.clear();
        closedList.clear();

        openList.add(costNode(start, end, null));

        while (!openList.isEmpty()) {
            final CostedNode<T> n = openList.peek();
            if (n.node.equals(end)) {
                return constructPath(n);
            }

            neighbourFunction.neighboursOf(n.node).forEach(neighbour -> {
                final CostedNode<T> costedNeighbour = costNode(neighbour, end, n);

                if (!openList.contains(costedNeighbour) && !closedList.contains(costedNeighbour)) {
                    // A node that hasn't been considered before
                    openList.add(costedNeighbour);
                } else {
                    // A node that has been considered before, but we're finding a different route
                    if (closedList.contains(costedNeighbour)) {
                        closedList.remove(costedNeighbour);
                        openList.add(costedNeighbour);
                    }
                }

                openList.remove(n);
                closedList.add(n);
            });
        }

        return null;
    }

    private List<T> constructPath(CostedNode<T> finalNode) {
        final List<T> path = Stream.iterate(finalNode, x -> x.parent)
                .takeWhile(x -> x != null)
                .map(x -> x.node)
                .collect(Collectors.toList());

        Collections.reverse(path);

        return path;
    }

    private CostedNode<T> costNode(T node,
                                   T end,
                                   CostedNode<T> parent) {
        return new CostedNode<T>(
                node,
                heuristicFunction.calculateCost(node, end),
                parent == null ? 0.0 : moveFunction.calculateCost(parent.node, node),
                parent);
    }
}

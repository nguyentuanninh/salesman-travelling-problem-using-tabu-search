import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<TSP.Point> cities = new ArrayList<>();
        cities.add(new TSP.Point(1, 1));
        cities.add(new TSP.Point(2, 3));
        cities.add(new TSP.Point(5, 2));
        cities.add(new TSP.Point(6, 8));
        cities.add(new TSP.Point(7, 2));

        TSP tsp = new TSP(cities);
        List<Integer> shortestPath = tsp.findShortestPath(1000, 50);
        System.out.println("Shortest path: " + shortestPath);
    }
}

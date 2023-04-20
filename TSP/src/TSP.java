import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSP {
    private int numCities;
    private List<Point> cities;
    private int[][] distanceMatrix;
    private Random random;

    public TSP(List<Point> cities) {
        this.numCities = cities.size();
        this.cities = cities;
        this.random = new Random();

        createDistanceMatrix();
    }

    // Tạo ma trận khoảng cách giữa các thành phố
    private void createDistanceMatrix() {
        distanceMatrix = new int[numCities][numCities];

        for(int i = 0; i < numCities; i++) {
            for(int j = 0; j < numCities; j++) {
                if(i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    int distance = cities.get(i).distanceTo(cities.get(j));
                    distanceMatrix[i][j] = distance;
                    distanceMatrix[j][i] = distance;
                }
            }
        }
    }

    // Tìm đường đi ngắn nhất sử dụng Tabu Search
    public List<Integer> findShortestPath(int iterations, int tabuTenure) {
        int[] bestSolution = generateRandomSolution();
        int bestCost = calculateCost(bestSolution);

        int[] currentSolution = Arrays.copyOf(bestSolution, numCities);
        int currentCost = bestCost;

        int[] tabuList = new int[numCities];
        Arrays.fill(tabuList, -tabuTenure);

        for(int i = 0; i < iterations; i++) {
            int[] nextSolution = getNextNeighbor(currentSolution, tabuList);
            int nextCost = calculateCost(nextSolution);

            // Nếu không tìm được neighbor tốt hơn
            if(nextCost >= currentCost) {
                updateTabuList(tabuList);
            } else {
                currentSolution = nextSolution;
                currentCost = nextCost;

                if(currentCost < bestCost) {
                    bestSolution = Arrays.copyOf(currentSolution, numCities);
                    bestCost = currentCost;
                }
            }
        }

        List<Integer> bestPath = new ArrayList<>();
        for(int city : bestSolution) {
            bestPath.add(city);
        }

        return bestPath;
    }

    // Sinh một giải pháp ngẫu nhiên
    private int[] generateRandomSolution() {
        List<Integer> citiesList = new ArrayList<>();
        for(int i = 0; i < numCities; i++) {
            citiesList.add(i);
        }

        Collections.shuffle(citiesList, random);

        int[] solution = new int[numCities];
        for(int i = 0; i < numCities; i++) {
            solution[i] = citiesList.get(i);
        }

        return solution;
    }

    // Tính chi phí của một giải pháp
    private int calculateCost(int[] solution) {
        int cost = 0;
        for(int i = 0; i < numCities - 1; i++) {
            cost += distanceMatrix[solution[i]][solution[i + 1]];
        }

        cost += distanceMatrix[solution[numCities - 1]][solution[0]];

        return cost;
    }

    // Tìm neighbor tốt nhất
    private int[] getNextNeighbor(int[] solution, int[] tabuList) {
        int[] bestNeighbor = Arrays.copyOf(solution, numCities);
        int bestCost = Integer.MAX_VALUE;

        for(int i = 0; i < numCities - 1; i++) {
            for(int j = i + 1; j < numCities; j++) {
                if(tabuList[solution[i]] < 0 && tabuList[solution[j]] < 0) {
                    int[] neighbor = Arrays.copyOf(solution, numCities);
                    swapCities(neighbor, i, j);

                    int cost = calculateCost(neighbor);
                    if(cost < bestCost) {
                        bestNeighbor = neighbor;
                        bestCost = cost;
                    }
                }
            }
        }

        return bestNeighbor;
    }

    // Hoán đổi vị trí của hai thành phố trong giải pháp
    private void swapCities(int[] solution, int i, int j) {
        int temp = solution[i];
        solution[i] =solution[j];
        solution[j] = temp;
    }

    // Cập nhật tabu list
    private void updateTabuList(int[] tabuList) {
        for(int i = 0; i < numCities; i++) {
            tabuList[i]--;
        }
    }

    // Điểm biểu diễn một thành phố trên bản đồ
    public static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int distanceTo(Point other) {
            int dx = other.x - x;
            int dy = other.y - y;

            return (int)Math.sqrt(dx * dx + dy * dy);
        }
    }
}
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static int totalPathCount;
    private static Map<String, Integer> smallCavesVisited;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("src/fullInput.txt");
        Scanner inScan = new Scanner(inputFile);
        List<String[]> lines = new ArrayList<>();
        Map<String, List<String>> pathMap = new HashMap<>();
        smallCavesVisited = new HashMap<>();

        while (inScan.hasNextLine()) {
            lines.add(inScan.nextLine().split("-"));
        }

        lines.forEach(line -> {
            List<String> tempList;
            if (pathMap.get(line[0]) != null) {
                tempList = pathMap.get(line[0]);
                tempList.add(line[1]);
                pathMap.put(line[0], tempList);
            }
            else {
                tempList = new ArrayList<>();
                tempList.add(line[1]);
                pathMap.put(line[0],tempList);
            }
            List<String> tempList2;
            if (pathMap.get(line[1]) != null) {
                tempList2 = pathMap.get(line[1]);
                tempList2.add(line[0]);
                pathMap.put(line[1], tempList2);
            }
            else {
                if ((line[0].compareTo("start") != 0) && (line[1].compareTo("end") != 0)) {
                    tempList2 = new ArrayList<>();
                    tempList2.add(line[0]);
                    pathMap.put(line[1], tempList2);
                }
            }
        });

        Stack<String> currentPath = new Stack<>();
        findNextPath("start", currentPath, pathMap, 1);
        System.out.println("Total Path Counts (Part 1): " + totalPathCount);

        currentPath = new Stack<>();
        smallCavesVisited = new HashMap<>();
        totalPathCount = 0;
        findNextPath("start", currentPath, pathMap, 2);
        System.out.println("Total Path Counts (Part 2): " + totalPathCount);
    }

    private static void findNextPath(String currentLocation, Stack<String> currentPath, Map<String,
            List<String>> pathMap, int allowedVisits) {
        currentPath.push(currentLocation);

        if (currentLocation.compareTo("end") == 0) {
            //currentPath.forEach(path -> {System.out.print(path +",");});
            //System.out.println();
            totalPathCount += 1;
        }
        else {
            if (isSmallCave(currentLocation)) {
                if (smallCavesVisited.containsKey(currentLocation)) { //already in visited hashmap
                    int count = smallCavesVisited.get(currentLocation);
                    smallCavesVisited.put(currentLocation, count + 1);
                }
                else {
                    smallCavesVisited.put(currentLocation, 1);
                }
            }

            for (String nextPath : pathMap.get(currentLocation)) {
                if (isValidPath(nextPath, currentPath, allowedVisits))
                {
                    findNextPath(nextPath, currentPath, pathMap, allowedVisits);
                    String lastPath = currentPath.pop();
                    if (smallCavesVisited.containsKey(lastPath)) {
                        int oldValue = smallCavesVisited.get(lastPath);
                        smallCavesVisited.replace(lastPath, oldValue - 1);
                    }
                }
            }
        }
    }

    private static boolean isValidPath(String nextPath, List<String> currentPath, int allowedVisits) {
        //TODO: One small cave can be visited twice, others once
        if (isSmallCave(nextPath) && currentPath.contains(nextPath)) {
            if ((nextPath.compareTo("start") == 0) || (nextPath.compareTo("end") == 0)) {
                return false;
            }
            if (smallCavesVisited.containsValue(allowedVisits)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSmallCave(String path) {
        return (Character.isLowerCase(path.charAt(0)));
    }

    private static int countVisits(List<String> currentPath, String nextPath) {
        int count = 0;
        for (String path : currentPath) {
            if (path.compareTo(nextPath) == 0) {
                count++;
            }
        }
        return count;
    }
}






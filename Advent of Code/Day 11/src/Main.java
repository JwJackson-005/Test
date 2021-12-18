import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static int flashCount;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("src/testInput.txt");
        Scanner inScan = new Scanner(inputFile);
        List<String> lines = new ArrayList<>();
        flashCount  = 0;

        while(inScan.hasNextLine()) {
            lines.add(inScan.nextLine());
        }

        int[][] grid = new int[lines.size() + 2][lines.get(0).length() + 2];
        int[][] grid2 = new int[lines.size() + 2][lines.get(0).length() + 2];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = -99;
                grid2[i][j] = -99;
            }
        }

        for (int i = 1; i < lines.size() + 1; i++) {
            for (int j = 1; j < lines.get(0).length() + 1; j++) {
                grid[i][j] = Character.getNumericValue(lines.get(i-1).charAt(j-1));
                grid2[i][j] = Character.getNumericValue(lines.get(i-1).charAt(j-1));
            }
        }

        part1(grid, 100);
        part2(grid2);
    }

    private static void part2(int[][] grid) {
        int stepCount = 1;
        while (!takeStep(grid)) {
            //System.out.println("Step " + stepCount + " complete.");
            stepCount++;
        }

        System.out.println("All flashes synchronized at step: " + stepCount);
    }

    private static void part1(int[][] grid, int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            takeStep(grid);
        }

        System.out.println("Total Flashes in Part 1: " + flashCount);
    }

    private static boolean takeStep(int[][] grid) {
        for (int i = 1; i < grid.length-1; i++) {
            for (int j = 1; j < grid[i].length-1; j++) {
                if(grid[i][j] >= 0) {
                    grid[i][j] += 1;
                }
                if (grid[i][j] == 10) {
                    flash(grid, i, j);
                }
            }
        }
        return clearFlashed(grid);
    }

    private static boolean clearFlashed(int[][] grid) {
        int flashesCleared = 0;

        for (int i = 1; i < grid.length - 1; i++) {
            for (int j = 1; j < grid[i].length - 1; j++) {
                if (grid[i][j] > 9) {
                    grid[i][j] = 0;
                    flashesCleared++;
                }
            }
        }

        //System.out.println("Flashes: " + flashesCleared);

        return (flashesCleared == ((grid.length - 2) * (grid[0].length - 2)));
    }

    private static void displayGrid(int[][] grid) {
        System.out.println("====================");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n===================");
    }

    private static void flash(int[][] grid, int i, int j) {
        int[] xVector = {-1,0,1};
        int[] yVector = {-1,0,1};
        flashCount++;
        //System.out.println("Flashing at [" + i +"]["+j+"] Value = " + grid[i][j]);

        for (int x : xVector) {
            for (int y : yVector) {
                if (grid[i + x][j + y] >= 0) {
                    grid[i + x][j + y] += 1;
                }
                if (grid[i + x][j + y] == 10) {
                    flash(grid, i+x, j+y);
                    //displayGrid(grid);
                }
            }
        }
    }
}

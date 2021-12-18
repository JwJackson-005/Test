import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class TransPaper {
    private char[][] grid;
    List<String[]> instructions;
    List<int[]> coordinates;
    int dotCount;
    int rows;
    int cols;

    private TransPaper() {}
    public TransPaper(String inputFile) {
        this.instructions = new ArrayList<>();
        this.coordinates = new ArrayList<>();
        getInput(inputFile);
        buildGrid();
    }

    private void buildGrid() {
        int max_x = 0;
        int max_y = 0;

        for (int[] coordinate : coordinates) {
            if (coordinate[0] > max_x) {
                max_x = coordinate[0];
            }
            if (coordinate[1] > max_y) {
                max_y = coordinate[1];
            }
        }

        cols = max_x + 1;
        rows = max_y + 1;


        grid = new char[rows][cols];
        dotCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '.';
            }
        }

        populateGrid();
    }

    private void populateGrid() {
        for (int[] coordinate : coordinates) {
            grid[coordinate[1]][coordinate[0]] = '#';
        }
    }

    private void displayGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    private void getInput(String inputFile) {
        try {
            Stream<String> lines = Files.lines(Path.of(inputFile));
            lines
                    .filter(line -> !line.startsWith("fold"))
                    .filter(line -> !(line.compareTo("") == 0))
                    .map(line -> line.split(","))
                    .forEach(x -> {
                        int[] temp = Arrays.stream(x).mapToInt(Integer::parseInt).toArray();
                        coordinates.add(temp);
                    });

            lines = Files.lines(Path.of(inputFile));
            lines
                    .filter(line -> line.startsWith("fold"))
                    .forEach(line -> {
                        String[] newLine = line.replace("fold along ", "").split("=");
                        instructions.add(newLine);
                    });

        } catch (IOException ioe) {
            System.out.println ("Invalid filename given");
        }
    }

    private void fold(String direction, int index) {
        dotCount = 0;
        char[][] newGrid;

        if (direction.compareTo("y") == 0) { //folding along x axis
            for (int i = 1; index + i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[index-i][j] != '#' && grid[index+i][j] == '#') {
                        grid[index-i][j] = '#';
                    }
                }
            }
            rows = index;
        }
        else { // folding along y axis
            for (int i = 0; i < rows; i++) {
                for (int j = 1; index + j < cols; j++) {
                    if (grid[i][index-j] != '#' && grid[i][index+j] == '#') {
                        grid[i][index-j] = '#';
                    }
                }
            }
            cols = index;
        }
    }

    public int getDotCount() {return this.dotCount;}

    public void part1() {
        fold(instructions.get(0)[0], Integer.parseInt(instructions.get(0)[1]));
        System.out.println("Part 1: " + dotCount);
    }



    public void part2() {
        for (String[] instruction : instructions) {
            fold (instruction[0], Integer.parseInt(instruction[1]));
        }
        displayGrid();
    }

}

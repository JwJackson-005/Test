package Days.Day_14;

import Util.DayUtil;

import java.util.HashMap;
import java.util.Map;

public class Polymer {
    private String template;
    private Map<String, String> pairMap;
    private Map<String, String> fastPairMap;

    public Polymer(String fileName) {
        String[] input = DayUtil.splitString(DayUtil.readFileToString(fileName), "\r\n\r\n");
        this.template = input[0];
        String[] pairs = DayUtil.splitString(input[1]);
        pairMap = new HashMap<>();
        fastPairMap = new HashMap<>();

        for (int i = 0; i < pairs.length; i++) {
            String[] temp = pairs[i].split(" -> ");
            pairMap.put(temp[0], temp[1].strip());
        }
    }

    public void buildPolymer() {
        StringBuilder newPolymer = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            newPolymer.append(template.charAt(i));
            if (i+1 < template.length()) {
                StringBuilder temp = new StringBuilder();
                temp.append(template.charAt(i));
                temp.append(template.charAt(i+1));
                newPolymer.append(pairMap.get(temp.toString()));
            }
        }
        template =  newPolymer.toString();
    }

    public void takeSteps(int steps) {
        for (int i = 0; i < steps; i++) {
            buildPolymer();
        }
    }

    public void partOne() {
        Map<Character, Long> charCounts = new HashMap<>();
        takeSteps(10);
        for (int i = 0; i < template.length(); i++) {
            char letter = template.charAt(i);
            charCounts.putIfAbsent(letter, (long) 0);
            charCounts.compute(letter, (key, val) -> val + 1);
        }
        long max = charCounts.values().stream().mapToLong(x -> x).max().getAsLong();
        long min = charCounts.values().stream().mapToLong(x -> x).min().getAsLong();

        System.out.println("Part 1 Answer: " + (max-min));
    }

    public void partTwo() {
        Map<Character, Long> charCounts = new HashMap<>();
        takeSteps(40);
        for (int i = 0; i < template.length(); i++) {
            char letter = template.charAt(i);
            charCounts.putIfAbsent(letter, (long) 0);
            charCounts.compute(letter, (key, val) -> val + 1);
        }
        long max = charCounts.values().stream().mapToLong(x -> x).max().getAsLong();
        long min = charCounts.values().stream().mapToLong(x -> x).min().getAsLong();

        System.out.println("Part 1 Answer: " + (max-min));
    }

}

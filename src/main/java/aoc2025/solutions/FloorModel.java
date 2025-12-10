package aoc2025.solutions;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FloorModel {
    private int width;
    private int length;
    ArrayList<ArrayList<int[]>> floor;

    public FloorModel(int width, int length) {
        this.width = width;
        this.length = length;
        floor = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<int[]> rowSections = new ArrayList<>();
            rowSections.add(new int[]{0, width});
            floor.add(rowSections);
        }
    }

    public void addTile(int x, int y) {
        ArrayList<int[]> secs = floor.get(y);
        int cum = 0;
        for (int i = 0; i < secs.size(); i++) {
            int[] sec = secs.get(i);
            int type = sec[0];
            int len = sec[1];
            if (cum <= x && x < cum + len) {
                if (type == 0) { // empty
                    int before = x - cum;
                    int after = len - before - 1;
                    secs.set(i, new int[]{0, before});
                    secs.add(i + 1, new int[]{1, 1});
                    if (after > 0) secs.add(i + 2, new int[]{0, after});
                }
                break;
            }
            cum += len;
        }
        
        // cleanup 0 lengths
        for (int j = 0; j < secs.size(); ) {
            if (secs.get(j)[1] == 0) {
                secs.remove(j);
            } else {
                j++;
            }
        }
        // merge adjacent same type
        for (int j = 0; j < secs.size() - 1; ) {
            int[] s1 = secs.get(j);
            int[] s2 = secs.get(j + 1);
            if (s1[0] == s2[0]) {
                s1[1] += s2[1];
                secs.remove(j + 1);
            } else {
                j++;
            }
        }
    }

    public boolean isTile(int x, int y) {
        ArrayList<int[]> secs = floor.get(y);
        int cum = 0;
        for (int[] sec : secs) {
            int len = sec[1];
            if (cum <= x && x < cum + len) {
                return sec[0] == 1;
            }
            cum += len;
        }
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public void printFloor() {
        for (int y = 0; y < length; y++) {
            ArrayList<int[]> secs = floor.get(y);
            for (int[] sec : secs) {
                int type = sec[0];
                int len = sec[1];
                char ch = type == 0 ? '.' : '#';
                for (int k = 0; k < len; k++) {
                    System.out.print(ch);
                }
            }
            System.out.print("    RLE: " + secs.stream().map(arr -> arr[0] + "," + arr[1]).collect(Collectors.joining(" ")));
            System.out.println();
        }
    }
    
}

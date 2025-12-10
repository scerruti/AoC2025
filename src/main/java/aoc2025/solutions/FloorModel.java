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
        ArrayList<int[]> lineSections = floor.get(y);
        int position = 0;
        for (int i = 0; i < lineSections.size(); i++) {
            int[] section = lineSections.get(i);
            int type = section[0];
            int len = section[1];
            if (position <= x && x < position + len) {
                if (type == 0) { // empty
                    int before = x - position;
                    int after = len - before - 1;
                    lineSections.set(i, new int[]{0, before});
                    lineSections.add(i + 1, new int[]{1, 1});
                    if (after > 0) lineSections.add(i + 2, new int[]{0, after});
                }
                break;
            }
            position += len;
        }
        
        // cleanup 0 lengths
        for (int j = 0; j < lineSections.size(); ) {
            if (lineSections.get(j)[1] == 0) {
                lineSections.remove(j);
            } else {
                j++;
            }
        }
        // merge adjacent same type
        for (int j = 0; j < lineSections.size() - 1; ) {
            int[] s1 = lineSections.get(j);
            int[] s2 = lineSections.get(j + 1);
            if (s1[0] == s2[0]) {
                s1[1] += s2[1];
                lineSections.remove(j + 1);
            } else {
                j++;
            }
        }
    }

    public boolean isTile(int x, int y) {
        ArrayList<int[]> lineSections = floor.get(y);
        int position = 0;
        for (int[] section : lineSections) {
            int len = section[1];
            if (position <= x && x < position + len) {
                return section[0] == 1;
            }
            position += len;
        }
        return false;
    }

    public int getFilledLengthInRange(int y, int startX, int endX) {
        ArrayList<int[]> lineSections = floor.get(y);
        int filled = 0;
        int position = 0;
        for (int[] section : lineSections) {
            int type = section[0];
            int len = section[1];
            int secStart = position;
            int secEnd = position + len - 1;
            if (secEnd < startX) {
                position += len;
                continue;
            }
            if (secStart > endX) break;
            int overlapStart = Math.max(secStart, startX);
            int overlapEnd = Math.min(secEnd, endX);
            if (overlapStart <= overlapEnd && type == 1) {
                filled += overlapEnd - overlapStart + 1;
            }
            position += len;
        }
        return filled;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public void printFloor() {
        for (int y = 0; y < length; y++) {
            ArrayList<int[]> lineSections = floor.get(y);
            for (int[] section : lineSections) {
                int type = section[0];
                int len = section[1];
                char ch = type == 0 ? '.' : '#';
                for (int k = 0; k < len; k++) {
                    System.out.print(ch);
                }
            }
            System.out.print("    RLE: " + lineSections.stream().map(arr -> arr[0] + "," + arr[1]).collect(Collectors.joining(" ")));
            System.out.println();
        }
    }

    public void fillInterior() {
        for (int y = 0; y < length; y++) {
            ArrayList<int[]> lineSections = floor.get(y);
            ArrayList<int[]> newSections = new ArrayList<>();
            boolean inside = false;
            for (int[] section : lineSections) {
                int type = section[0];
                int len = section[1];
                if (type == 1) {
                    inside = !inside;
                }
                newSections.add(new int[]{inside ? 1 : type, len});
            }
            floor.set(y, newSections);
            // merge adjacent same
            for (int j = 0; j < newSections.size() - 1; ) {
                int[] s1 = newSections.get(j);
                int[] s2 = newSections.get(j + 1);
                if (s1[0] == s2[0]) {
                    s1[1] += s2[1];
                    newSections.remove(j + 1);
                } else {
                    j++;
                }
            }
        }
    }
    
}

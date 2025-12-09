package aoc2025.solutions;

import java.util.ArrayList;


public class Day09 extends Day {
    public Day09() { super(9); }

    @Override
    public String part1(ArrayList<String> input) {
        ArrayList<Point> redTiles = new ArrayList<>();
        for (String line : input) {
            redTiles.add(new Point(line));
        }

        long maxArea = 0;
        for (int i = 0; i < redTiles.size() - 1; i++) {
            for (int j = i+1; j < redTiles.size(); j++) {
                long area = redTiles.get(i).areaWith(redTiles.get(j));
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return String.valueOf(maxArea);
    }

    @Override
    public String part2(ArrayList<String> input) {
        ArrayList<Point> redTiles = new ArrayList<>();
        int minX = Integer.MAX_VALUE;
        int maxX = 0;
        int minY = Integer.MAX_VALUE;
        int maxY = 0;
        for (String line : input) {
            Point point = new Point(line);
            redTiles.add(point);
            if (point.x > maxX) maxX = point.x;
            if (point.x < minX) minX = point.x;
            if (point.y > maxY) maxY = point.y;
            if (point.y < minY) minY = point.y;
        }

        // Build border map (only segment borders, not redTiles)
        java.util.Map<Integer, java.util.List<Integer>> borderMap = new java.util.HashMap<>();
        Point prev = redTiles.get(redTiles.size()-1);
        for (Point point : redTiles) {
            if (point.x == prev.x) {
                int start = point.y;
                int end = prev.y;
                int step = (start < end) ? 1 : -1;
                for (int r = start; r != end + step; r += step) {
                    borderMap.computeIfAbsent(r, k -> new java.util.ArrayList<>()).add(point.x);
                }
            } else {
                int start = point.x;
                int end = prev.x;
                int step = (start < end) ? 1 : -1;
                for (int c = start; c != end + step; c += step) {
                    borderMap.computeIfAbsent(point.y, k -> new java.util.ArrayList<>()).add(c);
                }
            }
            prev = point;
        }

        // Build filled intervals using simulated scanline fill
        java.util.Map<Integer, java.util.List<int[]>> filledIntervals = new java.util.HashMap<>();
        int gridWidth = maxX + 2;
        for (int y = 0; y < maxY + 2; y++) {
            java.util.List<Integer> borders = borderMap.get(y);
            if (borders == null) borders = new java.util.ArrayList<>();
            // Add redTiles as borders for this row
            for (Point p : redTiles) {
                if (p.y == y) {
                    borders.add(p.x);
                }
            }
            java.util.Collections.sort(borders);
            // Remove duplicates
            java.util.List<Integer> uniqueBorders = new java.util.ArrayList<>();
            for (int x : borders) {
                if (uniqueBorders.isEmpty() || uniqueBorders.get(uniqueBorders.size()-1) != x) {
                    uniqueBorders.add(x);
                }
            }
            borders = uniqueBorders;

            java.util.List<int[]> intervals = new java.util.ArrayList<>();
            // Add all borders as filled
            for (int x : borders) {
                intervals.add(new int[]{x, x});
            }
            // Simulate scanline fill for interior (using only segment borders for toggle)
            java.util.List<Integer> segmentBorders = borderMap.get(y);
            if (segmentBorders == null) segmentBorders = new java.util.ArrayList<>();
            java.util.Collections.sort(segmentBorders);
            java.util.List<Integer> uniqueSegmentBorders = new java.util.ArrayList<>();
            for (int x : segmentBorders) {
                if (uniqueSegmentBorders.isEmpty() || uniqueSegmentBorders.get(uniqueSegmentBorders.size()-1) != x) {
                    uniqueSegmentBorders.add(x);
                }
            }
            segmentBorders = uniqueSegmentBorders;
            boolean[] isSegmentBorder = new boolean[gridWidth];
            for (int x : segmentBorders) {
                if (x >= 0 && x < gridWidth) isSegmentBorder[x] = true;
            }
            boolean inside = false;
            int start = -1;
            for (int x = 0; x <= maxX; x++) {
                if (x > 0 && !isSegmentBorder[x-1] && isSegmentBorder[x]) {
                    inside = !inside;
                }
                if (inside && !isSegmentBorder[x]) {
                    if (start == -1) start = x;
                } else {
                    if (start != -1) {
                        intervals.add(new int[]{start, x-1});
                        start = -1;
                    }
                }
            }
            if (start != -1) intervals.add(new int[]{start, maxX});
            // Merge intervals
            intervals.sort(java.util.Comparator.comparingInt(a -> a[0]));
            java.util.List<int[]> merged = new java.util.ArrayList<>();
            for (int[] interval : intervals) {
                if (merged.isEmpty() || merged.get(merged.size()-1)[1] + 1 < interval[0]) {
                    merged.add(interval);
                } else {
                    merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], interval[1]);
                }
            }
            filledIntervals.put(y, merged);
        }

        // Print filled grid for visualization (comment out for large datasets)
        // printFilledGrid(filledIntervals, 0, gridWidth-1, 0, maxY+1, redTiles);

        long maxArea = 0;
        for (int i = 0; i < redTiles.size() - 1; i++) {
            for (int j = i+1; j < redTiles.size(); j++) {
                long area = redTiles.get(i).filledAreaWith(redTiles.get(j), filledIntervals);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return String.valueOf(maxArea);
    }

    private static void printFilledGrid(java.util.Map<Integer, java.util.List<int[]>> filledIntervals, int minX, int maxX, int minY, int maxY, java.util.List<Point> redTiles) {
        System.out.println("Tiles after filling:");
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                java.util.List<int[]> intervals = filledIntervals.get(y);
                boolean filled = false;
                if (intervals != null) {
                    for (int[] interval : intervals) {
                        if (interval[0] <= x && x <= interval[1]) {
                            filled = true;
                            break;
                        }
                    }
                }
                System.out.print(filled ? "1" : "0");
            }
            System.out.println();
        }
    }

    public class Point {
        int x;
        int y;

        public Point(String line) {
            this(line.split(","));
        }

        public Point(String[] coordinates) {
            this(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public long areaWith(Point p) {
            long width = (long) (Math.abs(x - p.x) + 1);
            long length = (long) (Math.abs(y - p.y) + 1);
            return width * length;
        }

        public long filledAreaWith(Point p, java.util.Map<Integer, java.util.List<int[]>> filledIntervals) {
            long width = (long) (Math.abs(x - p.x) + 1);
            long length = (long) (Math.abs(y - p.y) + 1);

            int startY = Math.min(y, p.y);
            int endY = Math.max(y, p.y);
            int startX = Math.min(x, p.x);
            int endX = Math.max(x, p.x);

            for (int r = startY; r <= endY; r++) {
                java.util.List<int[]> intervals = filledIntervals.get(r);
                if (intervals == null) return 0;
                boolean covered = false;
                for (int[] interval : intervals) {
                    if (interval[0] <= startX && endX <= interval[1]) {
                        covered = true;
                        break;
                    }
                }
                if (!covered) return 0;
            }

            return width * length;
        }
    }
}

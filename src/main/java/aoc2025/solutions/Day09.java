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

        // System.out.println("Number of red tiles: " + redTiles.size());

        // Adjust points to 0-based coordinates
        // for (Point point : redTiles) {
        //     point.x -= minX;
        //     point.y -= minY;
        // }

        /* RLE Version is more memory friendly */
        // Connect the red tiles with green tiles
        // int[][] floor = new int[maxX+2][maxY+2];
        // Point prev = redTiles.get(redTiles.size()-1);
        // for (Point point : redTiles) {
        //     floor[point.x][point.y] = 1;
        //     if (point.x == prev.x) {
        //         int start = point.y;
        //         int end = prev.y;
        //         int step = (start < end) ? 1 : -1;
        //         for (int r = start; r != end + step; r += step) {
        //             floor[point.x][r] = 1;
        //         }
        //     } else {
        //         int start = point.x;
        //         int end = prev.x;
        //         int step = (start < end) ? 1 : -1;
        //         for (int c = start; c != end + step; c += step) {
        //             floor[c][point.y] = 1;
        //         }
        //     }
        //     prev = point;
        // }
        // printFloor(floor);

        // Fill interior points using even-odd rule (horizontal scanline, toggle only on 0->1 transitions)
        // for (int y = 0; y < maxY + 1; y++) {
        //     boolean inside = false;
        //     for (int x = 0; x < maxX + 1; x++) {
        //         if (x > 0 && floor[x-1][y] == 0 && floor[x][y] == 1) {
        //             inside = !inside;
        //         }
        //         if (inside && floor[x][y] == 0) {
        //             floor[x][y] = 1;
        //         }
        //     }
        // }
        // System.out.println("\n\n");
        // printFloor(floor);

        /* RLE Version is more memory friendly */
        FloorModel floor = new FloorModel(maxX + 1, maxY + 1);

        // Draw green tiles between red tiles
        Point prev = redTiles.get(redTiles.size()-1);
        for (Point point : redTiles) {
            // System.out.println("Adding tile at " + point.x + "," + point.y);
            floor.addTile(point.x, point.y);
            if (point.x == prev.x) {
                int start = point.y;
                int end = prev.y;
                if (start < end) {
                    for (int r = start + 1; r < end; r++) {
                        floor.addTile(point.x, r);
                    }
                } else {
                    for (int r = start - 1; r > end; r--) {
                        floor.addTile(point.x, r);
                    }
                }
            } else {
                int start = point.x;
                int end = prev.x;
                if (start < end) {
                    for (int c = start + 1; c < end; c++) {
                        floor.addTile(c, point.y);
                    }
                } else {
                    for (int c = start - 1; c > end; c--) {
                        floor.addTile(c, point.y);
                    }
                }
            }
            // floor.printFloor();
            prev = point;
        }

        System.out.println("Built model");
        // floor.printFloor();



        long maxArea = 0;
        for (int i = 0; i < redTiles.size() - 1; i++) {
            for (int j = i+1; j < redTiles.size(); j++) {
                long area = redTiles.get(i).filledAreaWith(redTiles.get(j), floor);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return String.valueOf(maxArea);
    }

    // private static void printFloor(int[][] floor) {
    //     for (int r = 0; r < floor[0].length; r++) {
    //         for (int c = 0; c < floor.length; c++) {
    //             System.out.print(floor[c][r]);
    //         }
    //         System.out.println();
    //     }
    // }

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

        public long filledAreaWith(Point p, int[][] floor) {
            long width = (long) (Math.abs(x - p.x) + 1);
            long length = (long) (Math.abs(y - p.y) + 1);

            int startY = y;
            int endY = p.y;
            int stepY = (startY < endY) ? 1 : -1;
            for (int r = startY; r != endY + stepY; r += stepY) {
                int startX = x;
                int endX = p.x;
                int stepX = (startX < endX) ? 1 : -1;
                for (int c = startX; c != endX + stepX; c += stepX) {
                    if (floor[c][r] == 0) return 0;
                }
            }

            return width * length;
        }

        public long filledAreaWith(Point p, FloorModel floor) {
            long width = (long) (Math.abs(x - p.x) + 1);
            long length = (long) (Math.abs(y - p.y) + 1);

            int startY = y;
            int endY = p.y;
            int stepY = (startY < endY) ? 1 : -1;
            for (int r = startY; r != endY + stepY; r += stepY) {
                int startX = x;
                int endX = p.x;
                int stepX = (startX < endX) ? 1 : -1;
                for (int c = startX; c != endX + stepX; c += stepX) {
                    if (!floor.isTile(c, r)) {
                        // Optimized ray casting using RLE: count filled runs starting after c
                        int crossings = 0;
                        ArrayList<int[]> secs = floor.floor.get(r);
                        int cumPos = 0;
                        for (int[] sec : secs) {
                            int type = sec[0];
                            int len = sec[1];
                            if (type == 1 && cumPos > c) {
                                crossings++;
                            }
                            cumPos += len;
                        }
                        if (crossings % 2 == 0) return 0;
                    }
                }
            }

            return width * length;
        }
    }
}

package aoc2025.solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Set;


public class Day08 extends Day {
    private TreeMap<Long, ArrayList<ArrayList<JunctionBox>>> distances;
    private ArrayList<HashSet<JunctionBox>> circuits;

    public Day08() { super(8); }

    @Override
    public String part1(ArrayList<String> input) {
        // Use 10 connections for sample input, 1000 for real input
        int connectionLimit = (input.size() <= 20) ? 10 : 1000;
        String result = bruteForcePart1(input, connectionLimit);
        return result;
    }

    private String bruteForcePart1(ArrayList<String> input, int connectionLimit) {
        ArrayList<JunctionBox> boxes = new ArrayList<>();
        distances = new TreeMap<>();

        for (String line : input) {
            boxes.add(new JunctionBox(line));
        }

        for (int i = 0; i < boxes.size() - 1; i++) {
            JunctionBox box = boxes.get(i);
            for (int j = i + 1; j < boxes.size(); j++) {
                JunctionBox otherBox = boxes.get(j);

                long sqDistance = box.getSquaredDistance(otherBox);
                ArrayList<JunctionBox> pair = new ArrayList<>();
                pair.add(box);
                pair.add(otherBox);

                if (!distances.containsKey(sqDistance)) {
                    ArrayList<ArrayList<JunctionBox>> record = new ArrayList<>();
                    record.add(pair);
                    distances.put(sqDistance, record);
                } else {
                    distances.get(sqDistance).add(pair);
                }
            }
        }

        Set<Long> keys = distances.keySet();
        circuits = new ArrayList<>();
        int connectionCount = 0;
        boolean connectionsMade = false;
        HashSet<Long> usedKeys = new HashSet<>();

        for (Long distance : keys) {
            ArrayList<ArrayList<JunctionBox>> pairsList = distances.get(distance);
            System.out.println("Distance: " + distance + ", pairs: " + pairsList.size());
            usedKeys.add(distance);
            for (ArrayList<JunctionBox> pairs : pairsList) {
                ArrayList<HashSet<JunctionBox>> matchingCircuits = new ArrayList<>();
                for (HashSet<JunctionBox> circuit : circuits) {
                    if (circuit.contains(pairs.get(0)) || circuit.contains(pairs.get(1))) {
                        matchingCircuits.add(circuit);
                    }
                }
                switch (matchingCircuits.size()) {
                    case 0:
                        System.out.println("New circuit");
                        HashSet<JunctionBox> newCircuit = new HashSet<>(pairs);
                        circuits.add(newCircuit);
                        break;
                    case 1:
                        System.out.println("Extend circuit");
                        matchingCircuits.get(0).addAll(pairs);
                        break;
                    case 2:
                        System.out.println("Merge circuits");
                        matchingCircuits.get(0).addAll(pairs);
                        matchingCircuits.get(0).addAll(matchingCircuits.get(1));
                        circuits.remove(matchingCircuits.get(1));
                        break;
                    default:
                        System.out.println("What?");
                        break;
                }

                connectionCount += 1;
                if (connectionCount >= connectionLimit) {
                    connectionsMade = true;
                    break;
                }
            }
            if (connectionsMade) break;
        }
    //    for (Double usedKey :usedKeys) {
    //     distances.remove(usedKey);
    //    }

        ArrayList<Integer> circuitSize = new ArrayList<>();
        for (HashSet<JunctionBox> circuit : circuits) {
            circuitSize.add(circuit.size());
        }

        java.util.Collections.sort(circuitSize, java.util.Collections.reverseOrder());

        long product = 1;
        for (int i = 0; i < 3; i++) {
            product *= circuitSize.get(i);
        }

        return String.valueOf(product);
    }

    @Override
    public String part2(ArrayList<String> input) {
        String result = bruteForcePart2(input);
        return result;
    }

    private String bruteForcePart2(ArrayList<String> input) {
        ArrayList<JunctionBox> boxes = new ArrayList<>();
        for (String line : input) {
            boxes.add(new JunctionBox(line));
        }
        Set<Long> keys = distances.keySet();
        long product = 0;
        boolean found = false;

        outer:
        for (Long distance : keys) {
            for (ArrayList<JunctionBox> pairs : distances.get(distance)) {
                System.out.println("Testing: " + pairs.get(0) + "-" + pairs.get(1));
                ArrayList<HashSet<JunctionBox>> matchingCircuits = new ArrayList<>();
                for (HashSet<JunctionBox> circuit : circuits) {
                    if (circuit.contains(pairs.get(0)) || circuit.contains(pairs.get(1))) {
                        matchingCircuits.add(circuit);
                    }
                }
                switch (matchingCircuits.size()) {
                    case 0:
                        System.out.println("New circuit");
                        HashSet<JunctionBox> newCircuit = new HashSet<>(pairs);
                        circuits.add(newCircuit);
                        break;
                    case 1:
                        System.out.println("Extend circuit");
                        matchingCircuits.get(0).addAll(pairs);
                        break;
                    case 2:
                        System.out.println("Merge circuits");
                        matchingCircuits.get(0).addAll(pairs);
                        matchingCircuits.get(0).addAll(matchingCircuits.get(1));
                        circuits.remove(matchingCircuits.get(1));
                        break;
                    default:
                        System.out.println("What?");
                        break;
                }
                System.out.println("Circuit Count" + circuits.size());
                if (circuits.size() == 1 && circuits.get(0).size() == boxes.size() && !found) {
                    product = pairs.get(0).x * pairs.get(1).x;
                    System.out.println("Final merge: " + pairs.get(0) + " " + pairs.get(1));
                    found = true;
                    break outer;
                }
            }
        }
        return String.valueOf(product);
    }

    public class JunctionBox {
                public long getSquaredDistance(JunctionBox otherBox) {
                    return (long)(x - otherBox.x) * (x - otherBox.x)
                         + (long)(y - otherBox.y) * (y - otherBox.y)
                         + (long)(z - otherBox.z) * (z - otherBox.z);
                }
        int x;
        int y;
        int z;

        public JunctionBox(String coordinates) {
            this(coordinates.split(","));
        }

        public JunctionBox(String[] coordinates) {
            this(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));
        }

        public JunctionBox(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getDistance(JunctionBox otherBox) {
            return Math.sqrt(
                Math.pow(x - otherBox.x, 2) +
                Math.pow(y - otherBox.y, 2) +
                Math.pow(z - otherBox.z, 2)
            );
        }

        @Override
        public String toString() {
            return "JunctionBox [x=" + x + ", y=" + y + ", z=" + z + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            JunctionBox other = (JunctionBox) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }

        private Day08 getEnclosingInstance() {
            return Day08.this;
        }

    }
}

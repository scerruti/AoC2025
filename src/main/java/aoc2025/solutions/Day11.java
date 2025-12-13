package aoc2025.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


public class Day11 extends Day {
    private TreeMap<String, Device> connectionMap = new TreeMap<>();
    private Device start;
    private Device exit;
    
    public Day11() { super(11); }

    @Override
    public String part1(ArrayList<String> input) {
        String startName = "you";
        String exitName = "out";
        for (String line: input) {
            Device.buildConnectionFromString(line, connectionMap, this, startName, exitName);
        }

        // printMap();

        // Count paths from you to out
        int paths = routesTo(start, exit);

        return String.valueOf(paths);
    }

    private int routesTo(Device from, Device to) {
        return routesTo(from, to, new Device[0]);
    }

    private int routesTo(Device from, Device to, Device[] exclusions) {
        return routesTo(from, to, exclusions, new ArrayList<>());
    }

    private int routesTo(Device from, Device to, Device[] exclusions, ArrayList<Device> path) {
        ArrayList<Device> newPath = new ArrayList<>(path);
        newPath.add(from);

        if (from == to) return 1;

        int paths = 0;
        for (Device child : from.getConnections()) {
            if (path.contains(child)) continue;
            boolean excluded = false;
            for (Device ex : exclusions) {
                if (child != to && child == ex ) {
                    excluded = true;
                    break;
                }
            }
            if (!excluded) paths += routesTo(child, to, exclusions, path);
        }
        return paths;
    }

    @Override
    public String part2(ArrayList<String> input) {
        // I think we can use the existing map
        start = connectionMap.get("svr");
        String[] requiredNodes = {"dac", "fft"};
        Device[] requiredDevices = new Device[requiredNodes.length];
        for (int i = 0; i < requiredNodes.length; i++) {
            requiredDevices[i] = connectionMap.get(requiredNodes[i]);
            if (requiredDevices[i] == null) throw new RuntimeException("Required node not found.");
        }

        int[] pathPartCount = new int[requiredNodes.length * 2 + (requiredNodes.length * requiredNodes.length)];

        for (int i = 0; i < requiredDevices.length; i++) {
            pathPartCount[i] = routesTo(start, requiredDevices[i], requiredDevices);
            System.out.printf("%d:\tThere are %d paths from %s to %s%n", i, pathPartCount[i], start, requiredDevices[i]);

            for (int j = 0; j < requiredDevices.length; j++) {
                if (i == j) 
                    pathPartCount[(i + 1) * requiredDevices.length + j] = 1;
                else
                    pathPartCount[(i + 1) * requiredDevices.length + j] = routesTo(requiredDevices[i], requiredDevices[j], requiredDevices);
                System.out.printf("%d:\tThere are %d paths from %s to %s%n", 
                    (i + 1) * requiredDevices.length + j, 
                    pathPartCount[(i + 1) * requiredDevices.length + j], 
                    requiredDevices[i], requiredDevices[j]);
            }
            pathPartCount[requiredDevices.length + requiredDevices.length * requiredDevices.length + i] = routesTo(requiredDevices[i], exit, requiredDevices);
            System.out.printf("%d:\tThere are %d paths from %s to %s%n", requiredDevices.length + requiredDevices.length * requiredDevices.length + i, pathPartCount[requiredDevices.length + requiredDevices.length * requiredDevices.length + i], requiredDevices[i], exit);
       }

        int paths = 0;
        required:
        for (int i = 0; i < requiredDevices.length; i++) {
            int pathCount = pathPartCount[i];  // Start to i
            for (int j = 0; j < requiredDevices.length; j++) {
                if (i == j) continue;
                pathCount *= pathPartCount[requiredDevices.length + requiredDevices.length * requiredDevices.length + j]; // j to end
                if (pathCount == 0) break required;

                // All combinations of requiredDevice indexes starting at i and ending at j
                // Trival case: 1 route
                pathCount *= pathPartCount[(i + 1) * requiredDevices.length + j];
                
                paths += pathCount;
                pathCount = pathPartCount[i];  // Start to i
            }
            
        }

        return String.valueOf(paths);
    }

    private void printMap() {
        for (Device device : connectionMap.values()) {
            System.out.printf("%s: ", device.getName());
            for (Device child : device.getConnections()) {
                System.out.printf("%s ", child.getName());
            }
            System.out.println();
        }
    }

    public void setStart(Device device) {
        this.start = device;
    }

    public void setExit(Device device) {
        this.exit = device;
    }

    private static class Device {
        private String name;
        private ArrayList<Device> connections = new ArrayList<>();

        public Device(String name) {
            this.name = name;
            
        }
        
        public static void buildConnectionFromString(String input, TreeMap<String, Device> connectionMap, Day11 day11, String startName, String exitName) {
            Device device;
            String name = input.substring(0, input.indexOf(":"));
            if (connectionMap.containsKey(name)) {
                device = connectionMap.get(name);
            } else {
                device = new Device(name);
                connectionMap.put(name, device);
            }
            if (startName.equals(name)) day11.setStart(device);


            String[] connections = input.substring(input.indexOf(":")+2).split(" ");
            for (String connection : connections) {
                if (connectionMap.containsKey(connection)) {
                    device.addConnection(connectionMap.get(connection));
                } else {
                    Device child = new Device(connection);
                    device.addConnection(child);
                    connectionMap.put(connection, child);
                    if (exitName.equals(connection)) day11.setExit(child);
                }

            }
        }

        public void addConnection(Device device) {
            this.connections.add(device);
        }

        public String getName() {
            return name;
        }

        public ArrayList<Device> getConnections() {
            return connections;
        }

        @Override
        public String toString() {
            return "Device [name=" + name + "]";
        }

        
    }
}

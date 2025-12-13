# Day 11: Device Path Counting

## Problem Description (Part 1)
Advent of Code Day 11 involves navigating a network of devices connected in a directed graph. Each device has outputs that connect to other devices, forming a complex network. The goal of Part 1 is to count the number of paths from a starting device ("you") to an ending device ("out") without visiting any device more than once.

## Solution Approach (Part 1)
To solve Part 1, we model the device connections as a directed graph where each device is a node and the connections are edges. We use a recursive depth-first search (DFS) to explore all possible paths from start to end, ensuring no cycles by tracking visited devices in the current path.

Key challenges include handling the graph's structure and avoiding infinite loops in cycles. We implement cycle detection by checking if a device is already in the current path before recursing.

## Code Implementation (Part 1)
```java
private long routesTo(Device from, Device to, Device[] exclusions, ArrayList<Device> path) {
    String exclusionsStr = Arrays.stream(exclusions).map(Device::getName).sorted().collect(Collectors.joining(","));
    String key = from.getName() + "|" + to.getName() + "|" + exclusionsStr;
    if (memo.containsKey(key)) return memo.get(key);
    
    ArrayList<Device> newPath = new ArrayList<>(path);
    newPath.add(from);

    if (from == to) return 1L;

    long paths = 0;
    for (Device child : from.getConnections()) {
        if (path.contains(child)) continue;
        boolean excluded = false;
        for (Device ex : exclusions) {
            if (child != to && child == ex) {
                excluded = true;
                break;
            }
        }
        if (!excluded) paths += routesTo(child, to, exclusions, newPath);
    }
    memo.put(key, paths);
    return paths;
}
```

## Learning Objectives
- **AP CSA Alignment**: This problem reinforces recursion and graph traversal, key topics in AP Computer Science A. Students practice implementing DFS for path finding and managing state with exclusion lists.
- **CSTA Standards for Teachers**: Aligns with CSTA 3A-AP-17 (decomposing problems into smaller, solvable units) and 3A-AP-18 (creating algorithms for analysis). Teachers can use this to teach algorithmic efficiency and debugging recursive functions.

## Problem Description (Part 2)
Part 2 extends the challenge by requiring paths that visit specific required devices in any order before reaching the exit. The start point also changes, and exclusions prevent revisiting certain devices during segments of the path.

## Solution Approach (Part 2)
For Part 2, we break the problem into segments: paths from start to each required device (avoiding others), between required devices, and from required devices to exit. Since order matters, we generate all permutations of the required devices and sum the path counts for each valid sequence.

Memoization is crucial here to cache path counts for subproblems, avoiding recomputation. We use a permutation generator to handle the ordering.

## Code Implementation (Part 2)
```java
// Generate permutations of indices excluding i and j
private List<int[]> generatePermutationsExcluding(int exclude1, int exclude2, int total) {
    List<Integer> indices = new ArrayList<>();
    for (int k = 0; k < total; k++) {
        if (k != exclude1 && k != exclude2) {
            indices.add(k);
        }
    }
    List<int[]> permutations = new ArrayList<>();
    permute(indices, 0, permutations);
    return permutations;
}

// Recursive permutation helper
private void permute(List<Integer> list, int start, List<int[]> result) {
    if (start == list.size()) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        result.add(arr);
        return;
    }
    for (int i = start; i < list.size(); i++) {
        swap(list, start, i);
        permute(list, start + 1, result);
        swap(list, start, i);
    }
}
```

## Additional Learning Objectives
- **AP CSA Alignment**: Introduces combinatorial algorithms and memoization for optimization, building on recursion. Students learn to handle permutations and large numbers with `long`.
- **CSTA Standards for Teachers**: Supports 3A-AP-16 (evaluating algorithms for efficiency) and 3A-AP-22 (using abstractions to manage complexity). Teachers can discuss trade-offs between time and space in memoization.
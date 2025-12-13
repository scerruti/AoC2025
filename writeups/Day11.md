# Day 11: Device Path Counting

## Problem Description (Part 1)
Advent of Code Day 11 involves navigating a network of devices connected in a directed graph. Each device has outputs that connect to other devices, forming a complex network. The goal of Part 1 is to count the number of paths from a starting device ("you") to an ending device ("out") without visiting any device more than once.

## Solution Approach (Part 1)
To solve Part 1, we model the device connections as a directed graph where each device is a node and the connections are edges. We use a recursive depth-first search (DFS) to explore all possible paths from start to end. Although we initially thought cycle detection might be needed, Part 1's graph is acyclic, so it works without it— a pleasant surprise during testing.

Key challenges include handling the graph's structure and ensuring efficient path counting.

## Code Implementation (Part 1)
```java
private int routesTo(Device from, Device to) {
    if (from == to) return 1;

    int paths = 0;
    for (Device child : from.getConnections()) {
        paths += routesTo(child, to); 
    }
    return paths;
}
```

## Learning Objectives
- **AP CSA Alignment**: This problem reinforces recursion and graph traversal, key topics in AP Computer Science A. Students practice implementing DFS for path finding and managing state with exclusion lists.
- **CSTA Standards for Teachers**: Aligns with CSTA 3A-AP-17 (decomposing problems into smaller, solvable units) and 3A-AP-18 (creating algorithms for analysis). Teachers can use this to teach algorithmic efficiency and debugging recursive functions.

## Problem Description (Part 2)
Part 2 extends the challenge by requiring paths that visit specific required devices in any order before reaching the exit. The start point also changes, and exclusions prevent revisiting certain devices during segments of the path.

## Solution Approach (Part 2)
For Part 2, we precompute path counts between all relevant pairs of devices (start to required, required to required, required to exit) while avoiding the other required devices as exclusions. Then, for each pair of required devices (i, j), we calculate the total paths as (start to i) * (i to j) * (j to exit), summing over all such pairs. This segmented approach ensures we visit both required devices without revisiting others.

Initially, we attempted a general solution for n required nodes, but simplified to match the puzzle's two-node requirement. We wished we had used a 2D array for path counts instead of a 1D array with complex indexing. Another potential approach was to enumerate full paths and check for required node visits, but the segmented method was more efficient.

The key breakthrough was adding memoization to cache path counts, dramatically improving performance—cycle detection may have helped but memoization was crucial. Exclusions prevent invalid paths by blocking access to other required devices during segments.

## Code Implementation (Part 2)
The memoized recursive method for counting paths with exclusions and cycle detection:

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

The segmented path counting logic that precomputes and combines path segments:

```java
long[] pathPartCount = new long[requiredNodes.length * 2 + (requiredNodes.length * requiredNodes.length)];

// Precompute paths with exclusions
for (int i = 0; i < requiredDevices.length; i++) {
    pathPartCount[i] = routesTo(start, requiredDevices[i], requiredDevices);  // Start to i, avoiding others
    // ... similar for i to j and j to exit
}

// Sum paths for each pair (i, j)
long paths = 0;
for (int i = 0; i < requiredDevices.length; i++) {
    long pathCount = pathPartCount[i];  // Start to i
    for (int j = 0; j < requiredDevices.length; j++) {
        if (i == j) continue;
        pathCount *= pathPartCount[/* j to exit */];
        pathCount *= pathPartCount[/* i to j */];
        paths += pathCount;
        pathCount = pathPartCount[i];  // Reset
    }
}
```
**Memoization Guidelines**: Use memoization in recursive functions with overlapping subproblems (e.g., repeated calls to the same subcomputations) to avoid exponential time. It's ideal for problems like path counting or dynamic programming, but skip it for simple, non-overlapping recursion to avoid unnecessary overhead.

## Additional Learning Objectives
- **AP CSA Alignment**: Introduces combinatorial algorithms and memoization for optimization, building on recursion. Students learn to handle permutations and large numbers with `long`.
- **CSTA Standards for Teachers**: Supports 3A-AP-16 (evaluating algorithms for efficiency) and 3A-AP-22 (using abstractions to manage complexity). Teachers can discuss trade-offs between time and space in memoization.

## AI Assistance
This writeup was assisted by GitHub Copilot.
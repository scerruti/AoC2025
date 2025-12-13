# RLE FloorModel Optimizations for AoC Day 9

This document outlines 3 simple optimizations for the RLE-based `FloorModel` in `Day09.java`. These focus on improving performance in `filledAreaWith` (rectangle checking), the main bottleneck. Optimizations are ordered by scale of improvement (highest to lowest).

## Current Performance Baseline (Filled Intervals Version)
- Day 09 - Part 1: 4744899849 (8 ms)
- Day 09 - Part 2: 1540192500 (31753 ms)

Note: Part 2 is slow due to O(n²) rect checks on large grids. These optimizations target the RLE version for comparison and potential speedup.

## 1. Optimize Rectangle Checking: Check Range Coverage per Row
**Why?** The current `filledAreaWith` loops over every x in the rectangle (O(rect width)) and performs ray casting for empty cells. Instead, traverse RLE runs per row to sum filled lengths in [minX, maxX] and check if it covers the full width. This is O(runs per row), much faster for wide rects.

**Impact:** High – Eliminates per-cell ray casting.

**Implementation:**
- Add `getFilledLengthInRange` method to `FloorModel.java`.
- Update `filledAreaWith` in `Day09.java` to use it.

```java
// In FloorModel.java
public int getFilledLengthInRange(int y, int startX, int endX) {
    ArrayList<int[]> secs = floor.get(y);
    int filled = 0;
    int cum = 0;
    for (int[] sec : secs) {
        int type = sec[0];
        int len = sec[1];
        int secStart = cum;
        int secEnd = cum + len - 1;
        if (secEnd < startX) {
            cum += len;
            continue;
        }
        if (secStart > endX) break;
        int overlapStart = Math.max(secStart, startX);
        int overlapEnd = Math.min(secEnd, endX);
        if (overlapStart <= overlapEnd && type == 1) {
            filled += overlapEnd - overlapStart + 1;
        }
        cum += len;
    }
    return filled;
}

// In Day09.java, replace filledAreaWith
public long filledAreaWith(Point p, FloorModel floor) {
    long width = (long) (Math.abs(x - p.x) + 1);
    long length = (long) (Math.abs(y - p.y) + 1);
    int minX = Math.min(x, p.x);
    int maxX = Math.max(x, p.x);
    int minY = Math.min(y, p.y);
    int maxY = Math.max(y, p.y);
    for (int r = minY; r <= maxY; r++) {
        int filledInRow = floor.getFilledLengthInRange(r, minX, maxX);
        if (filledInRow < width) return 0;
    }
    return width * length;
}
```

## 2. Cache Row Cumulative Positions for Faster isTile
**Why?** `isTile` traverses runs each time (O(runs)). Cache cumulative start positions per row for O(log runs) binary search.

**Impact:** Medium – Speeds up individual `isTile` calls.

**Implementation:**
- Add `cumStarts` field to `FloorModel.java`.
- Update after merging, and modify `isTile`.

```java
// In FloorModel.java
private ArrayList<int[]> cumStarts;

public FloorModel(int width, int length) {
    // ... existing
    cumStarts = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
        cumStarts.add(new int[]{0});
    }
}

private void updateCumStarts(int y) {
    ArrayList<int[]> secs = floor.get(y);
    int[] starts = new int[secs.size()];
    int cum = 0;
    for (int i = 0; i < secs.size(); i++) {
        starts[i] = cum;
        cum += secs.get(i)[1];
    }
    cumStarts.set(y, starts);
}

// Call updateCumStarts(y) after merging in addTile

public boolean isTile(int x, int y) {
    int[] starts = cumStarts.get(y);
    ArrayList<int[]> secs = floor.get(y);
    int left = 0, right = starts.length - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (starts[mid] <= x && x < starts[mid] + secs.get(mid)[1]) {
            return secs.get(mid)[0] == 1;
        } else if (x < starts[mid]) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return false;
}
```

## 3. Avoid Unnecessary Merging (Deferred Merging)
**Why?** Merging after every `addTile` is O(runs), but if building is quick, defer until needed.

**Impact:** Low – Reduces redundant work during bulk additions.

**Implementation:**
- Add `dirty` flag per row.
- Merge only when accessing.

```java
// In FloorModel.java
private ArrayList<Boolean> dirty;

public FloorModel(int width, int length) {
    // ... existing
    dirty = new ArrayList<>(length);
    for (int i = 0; i < length; i++) dirty.add(false);
}

// In addTile, after splitting: dirty.set(y, true); // Remove merge code

private void mergeRow(int y) {
    // Move merge logic here
    ArrayList<int[]> secs = floor.get(y);
    // ... merge code
    updateCumStarts(y);
}

// In isTile/getFilledLengthInRange: if (dirty.get(y)) { mergeRow(y); dirty.set(y, false); }
```

Test after each optimization. Start with #1 for the biggest gain.
package aoc2025.solutions;

/**
 * Represents an inclusive range of ingredient IDs from start to end.
 * Used to determine if ingredient IDs are "fresh" in the cafeteria inventory system.
 */
public class Range {
    private long start;
    private long end;

    /**
     * Constructs a Range from a string in format "start-end".
     * @param rangeString String containing two numbers separated by a dash (e.g., "3-5")
     * @throws IllegalArgumentException if rangeString doesn't contain exactly one dash
     */
    public Range(String rangeString) throws IllegalArgumentException {
        String[] ends = rangeString.split("-");
        if (ends.length != 2) throw new IllegalArgumentException();
        this.start = Long.parseLong(ends[0]);
        this.end = Long.parseLong(ends[1]);
    }

    /**
     * Checks if a given ingredient ID falls within this range (inclusive).
     * @param ingredientString The ingredient ID as a string
     * @return true if the ingredient is within [start, end], false otherwise
     */
    public boolean contains(String ingredientString) {
        Long ingredient = Long.parseLong(ingredientString);
        return (start <= ingredient) && (ingredient <= end);
    }

    /**
     * Gets the start of this range.
     * @return the starting ingredient ID
     */
    public long getStart() {
        return start;
    }

    /**
     * Gets the end of this range.
     * @return the ending ingredient ID
     */
    public long getEnd() {
        return end;
    }

    /**
     * Calculates the number of ingredient IDs in this range (inclusive).
     * @return the count of IDs from start to end (end - start + 1)
     */
    public long getSize() {
        return end - start + 1;
    }

    /**
     * Checks if this range comes entirely before another range (no overlap or adjacency).
     * @param range The range to compare against (null-safe)
     * @return true if this range ends before the other range starts
     */
    public boolean before(Range range) {
        return range == null || range.start > this.end;
    }
    
    /**
     * Checks if this range comes entirely after another range (no overlap or adjacency).
     * @param range The range to compare against (null-safe)
     * @return true if this range starts after the other range ends
     */
    public boolean after(Range range) {
        return range == null || range.end < this.start;
    }
    
    /**
     * Checks if this range intersects or is adjacent to another range.
     * @param range The range to check for intersection (null-safe)
     * @return true if the ranges overlap or touch
     */
    public boolean intersects(Range range) {
        return range != null && this.end >= range.start && this.start <= range.end;
    }

    /**
     * Extends this range to include another intersecting range.
     * Modifies this range's start and end to encompass both ranges.
     * @param range The range to merge into this range
     * @return the number of new ingredient IDs added (difference in size)
     */
    public long extend(Range range) {
        // System.out.println("Extend " + this + " by " + range);
        long size = getSize();
        // Expand to include the other range
        start = Math.min(start, range.start);
        end = Math.max(end, range.end);
        // System.out.println("Extended " + this);
        return getSize() - size; 
    }

    /**
     * Joins three ranges (this range bridges upperRange and lowerRange).
     * Modifies lowerRange to encompass all three ranges.
     * @param upperRange The range above this one
     * @param lowerRange The range below this one (will be modified)
     * @return the number of new ingredient IDs added (accounting for gap)
     */
    public long join(Range upperRange, Range lowerRange) {
        // Calculate the gap between lower and upper ranges
        long gap = upperRange.start - lowerRange.end - 1;
        long size = upperRange.end - lowerRange.start + 1;
        // Merge all three ranges into lowerRange
        lowerRange.start = Math.min(lowerRange.start, start);
        lowerRange.end = Math.max(upperRange.end, end);
        return lowerRange.getSize() - size + gap;
    }

    @Override
    public String toString() {
        return "Range [start=" + start + ", end=" + end + " size: " + getSize() +  "]";
    }

    
}

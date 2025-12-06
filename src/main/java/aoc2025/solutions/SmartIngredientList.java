package aoc2025.solutions;

import java.util.ArrayList;

/**
 * Maintains a sorted list of non-overlapping ranges.
 * Automatically merges new ranges with existing ones to maintain the invariant
 * that no two ranges in the list overlap.
 */
public class SmartIngredientList {
    private ArrayList<Range> ranges = new ArrayList<>();

    /**
     * Adds a new range to the list, merging with existing ranges if necessary.
     * Maintains the invariant that ranges in the list never overlap.
     * 
     * There are three possible cases when adding a range:
     * 1. No intersection: Range fits between two existing ranges
     * 2. Intersects both: Bridges and merges three ranges into one
     * 3. Intersects one: Extends a single existing range (lower or upper)
     * 
     * @param range The new range to add
     * @return the number of new unique ingredient IDs added
     */
    public long add(Range range) {
        Range lowerRange = null;
        Range upperRange = null;

        for (int i = 0; i < ranges.size(); i++) {
            lowerRange = upperRange;
            upperRange = ranges.get(i);

            // Case 1: Range fits between lower and upper (no intersection)
            if (range.before(upperRange) && range.after(lowerRange)) {
                ranges.add(i, range);
                return range.getSize();
            } 
            // Case 2: Range intersects both lower and upper (bridge)
            else if (range.intersects(upperRange) && range.intersects(lowerRange)) {
                ranges.remove(upperRange);  // Remove upper, will merge into lower
                return range.join(upperRange, lowerRange);
            } 
            // Case 3a: Range intersects only lower
            else if (range.before(upperRange) && range.intersects(lowerRange)) {
                return lowerRange.extend(range);
            }  
        }

        // Case 3b: Range intersects the last range (upperRange)
        if (range.intersects(upperRange)) {
            return upperRange.extend(range);
        } else {
            // Range comes after all existing ranges
            ranges.add(range);
        }
        return range.getSize();
    }
}

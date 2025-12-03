package aoc2025.solutions;

public class Bank {
    public static int maxJoltage(String batteries) {
        int tens = Integer.parseInt(batteries.substring(0,1));
        int ones = Integer.parseInt(batteries.substring(1,2));
        int second = ones;

        for (int i = 2; i < batteries.length(); i++) {
            int first = second;
            second = Integer.parseInt(batteries.substring(i, i+1));
            if (first > tens) {
                tens = first;
                ones = second;
            } else {
                if (second > ones) {
                    ones = second;
                }
            }
        }

        return tens*10 + ones;
    }

    public static long maxJoltage(String batteries, int numBatteries) {
        int[] selectedBatteries = new int[numBatteries];
        int selectedCount = 0;
        int toSkip = batteries.length() - numBatteries;
        
        for (int i = 0; i < batteries.length(); i++) {
            int currentDigit = Integer.parseInt(batteries.substring(i, i + 1));
            
            // Remove previously selected digits if current is better and we can still skip
            while (selectedCount > 0 && 
                   toSkip > 0 && 
                   selectedBatteries[selectedCount - 1] < currentDigit) {
                selectedCount--;
                toSkip--;
            }
            
            // Add current digit if we haven't selected enough yet
            if (selectedCount < numBatteries) {
                selectedBatteries[selectedCount] = currentDigit;
                selectedCount++;
            } else {
                // We have enough digits, so we're skipping this one
                toSkip--;
            }
        }
        
        long joltage = 0;
        for (int i = 0; i < numBatteries; i++) {
            joltage = 10 * joltage + selectedBatteries[i];
        }
        return joltage;
    }
}

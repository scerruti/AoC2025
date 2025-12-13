package aoc2025.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;


public class Day10 extends Day {
    private Map<String, Integer> memo = new HashMap<>();

    public Day10() { super(10); }

    @Override
    public String part1(ArrayList<String> input) {
        int result = 0;

        line: 
        for (String line: input) {
            String lights = line.substring(1, line.indexOf("]"));
            boolean[] targetState = new boolean[lights.length()];
            for (int i = 0; i < lights.length(); i++) {
                targetState[i] = lights.substring(i, i+1).equals("#");
            }


            String[] buttonStrings = line.substring(
                line.indexOf("]")+1, 
                line.indexOf("{")).trim().split(" ");
            Button[] buttons = new Button[buttonStrings.length];
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(buttonStrings[i]);
            }

            // System.out.println(Arrays.toString(targetState));
            // System.out.println(Arrays.toString(buttons));

            // We need to build a tree of button presses so that we can find the earliest set
            // of button presses that works.
            ArrayList<ArrayList<ButtonPress>> buttonPresses = new ArrayList<>();
            HashSet<ButtonPress> previousPresses = new HashSet<>();

            for (Button button : buttons) {
                ButtonPress press = new ButtonPress(button, new boolean[targetState.length]);
                if (press.isConfigured(targetState)) {
                    result += 1;
                    continue line;
                }
                ArrayList<ButtonPress> firstPress = new ArrayList<>();
                firstPress.add(press);
                buttonPresses.add(firstPress);
                previousPresses.add(press);
            }

            while (buttonPresses.size() > 0) {
                ArrayList<ButtonPress> presses = buttonPresses.remove(0);
                boolean[] states = presses.get(presses.size() - 1).getResultState(); 
                // for (ButtonPress p : presses) System.out.print(p + " ");
                // System.out.println();
                for (Button button : buttons) {
                    ButtonPress press = new ButtonPress(button, states);
                    // System.out.println("\t" + press + " " + presses.size());
                    // Avoid loops
                    if (previousPresses.contains(press)) continue;
                    if (press.isConfigured(targetState)) {
                        // System.out.println("Found!");
                        result += presses.size()+1;
                        continue line;
                    }
                    ArrayList<ButtonPress> newPress = new ArrayList<>(presses);
                    newPress.add(press);
                    buttonPresses.add(newPress);
                    previousPresses.add(press);
                }                
            }
        }
        
        return String.valueOf(result);
    }

    @Override
    public String part2(ArrayList<String> input) {
        int result = 0;

        for (String line: input) {
            // Create the linear solver for this line
            ExpressionsBasedModel model = new ExpressionsBasedModel();

            String joltage = line.substring(line.indexOf("{")+1,line.length()-1);
            int[] targetJoltage = new int[joltage.length()];
            String[] joltages = joltage.split(",");
            for (int i = 0; i < joltages.length; i++) {
                targetJoltage[i] = Integer.parseInt(joltages[i]);
            }

            String[] buttonStrings = line.substring(
                line.indexOf("]")+1, 
                line.indexOf("{")).trim().split(" ");
            Button[] buttons = new Button[buttonStrings.length];
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(buttonStrings[i]);
            }

            System.out.println(Arrays.toString(targetJoltage));
            System.out.println(Arrays.toString(buttons));

            // Build the button-joltage matrix
            boolean[][] matrix = new boolean[buttons.length][targetJoltage.length];
            for (int i = 0; i < buttons.length; i++) {
                for (int light : buttons[i].lightNumbers) {
                    if (light < targetJoltage.length) {
                        matrix[i][light] = true;
                    }
                }
            }

            Variable[] vars = new Variable[buttons.length];
                for (int i = 0; i < buttons.length; i++) {
                vars[i] = model.addVariable("x" + i).lower(0).integer(true);  // Non-negative integer
            }

            for (int j = 0; j < targetJoltage.length; j++) {
                Expression expr = model.addExpression("constraint" + j).level(targetJoltage[j]);  // Right-hand side = target
                for (int i = 0; i < buttons.length; i++) {
                    if (matrix[i][j]) {  // If button i affects index j
                        expr.set(vars[i], 1);  // Coefficient 1 for x[i] in this constraint
                    }
                }
            }
            Expression objective = model.addExpression("objective").weight(1);  // Minimize
            for (Variable v : vars) {
                objective.set(v, 1);  // Coefficient 1 for each x[i]
            }            

            Optimisation.Result solveResult = model.minimise();
            if (solveResult.getState().isSuccess()) {
                int totalPresses = 0;
                for (int i = 0; i < vars.length; i++) {
                    int presses = (int) Math.round(solveResult.get(i).doubleValue());
                    totalPresses += presses;
                    // Optional: System.out.println("Button " + vars[i].getName() + ": " + presses);
                }
                result += totalPresses;  // Add to overall result
            } else {
                // Infeasible (unlikely for valid AoC inputs)
                System.out.println("No solution found");
            }
        }

        return String.valueOf(result);

    }    
        
    public String part2Attempt2(ArrayList<String> input) {
        int result = 0;

        for (String line: input) {
            String joltage = line.substring(line.indexOf("{")+1,line.length()-1);
            int[] targetJoltage = new int[joltage.length()];
            String[] joltages = joltage.split(",");
            for (int i = 0; i < joltages.length; i++) {
                targetJoltage[i] = Integer.parseInt(joltages[i]);
            }

            String[] buttonStrings = line.substring(
                line.indexOf("]")+1, 
                line.indexOf("{")).trim().split(" ");
            Button[] buttons = new Button[buttonStrings.length];
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(buttonStrings[i]);
            }

            // System.out.println(Arrays.toString(targetJoltage));
            // System.out.println(Arrays.toString(buttons));

            memo.clear();
            result += process(targetJoltage, buttons, 0, Integer.MAX_VALUE);
        }
        
        return String.valueOf(result);
    }

    public int process(int[] joltage, Button[] buttons, int depth, int globalMin) {
        String key = Arrays.toString(joltage) + "|" + Arrays.toString(buttons);
        if (memo.containsKey(key)) return memo.get(key);

        // System.out.printf("%s %s %d\n", Arrays.toString(joltage), Arrays.toString(buttons), depth);
        if (depth >= globalMin) return Integer.MAX_VALUE;
        
        // Find the joltage with the fewest clicks remaining
        int index = 0;
        int minClicks = Integer.MAX_VALUE;
        for (int i = 0; i < joltage.length; i++) {
            if (joltage[i] > 0 && joltage[i] < minClicks) {
                minClicks = joltage[i];
                index = i;
            }
        }

        // Find all combination of button clicks that result in that many clicks for that light
        ArrayList<Integer> relevantButtons = new ArrayList<>();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(index)) {
                relevantButtons.add(i);
            }
        }

        List<int[]> validButtonCombinations = findCombinations(relevantButtons, minClicks, minClicks);
        int newButtonCount = buttons.length - relevantButtons.size(); 
        Button[] newButtons = new Button[newButtonCount];
        int j = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (relevantButtons.contains(i)) continue;
            newButtons[j] = buttons[i];
            j += 1;
        }


        for (int[] presses : validButtonCombinations) {
            int clicks = 0;
            for (int i : presses) clicks += i;
            if (depth + clicks >= globalMin) continue;

            int[] resultJoltage = computeJoltage(joltage, buttons, relevantButtons, presses);
            if (endJoltage(resultJoltage)) return depth + clicks;
            if (!validJoltage(resultJoltage) || newButtonCount == 0) continue;

            int resultDepth = process(resultJoltage, newButtons, depth + clicks, globalMin); 
            if (resultDepth < globalMin) globalMin = resultDepth;
        }

        memo.put(key, globalMin);
        return globalMin;
    }

    private boolean validJoltage(int[] joltage) {
        for (int i = 0; i <joltage.length; i++)  {
            if (joltage[i] < 0 ) return false;
        }
        return true;
    }

    private boolean endJoltage(int[] joltage) {
        for (int i = 0; i <joltage.length; i++)  {
            if (joltage[i] != 0 ) return false;
        }
        return true;
  
    }

    private int[] computeJoltage(int[] joltage, Button[] buttons, ArrayList<Integer> relevantButtons, int[] presses) {
        int[] currentJoltage = Arrays.copyOf(joltage, joltage.length);
        for (int i = 0; i < presses.length; i++) {
            currentJoltage = buttons[relevantButtons.get(i)].press(currentJoltage, presses[i]);
        }
        return currentJoltage;
    }

    // Helper method to find all combinations of button presses that sum to target for a given set of buttons
    private List<int[]> findCombinations(ArrayList<Integer> affectingButtons, int target, int maxClicks) {
        List<int[]> result = new ArrayList<>();
        int[] currentCounts = new int[affectingButtons.size()];  // Tracks press counts for each button
        generateCombinations(affectingButtons, target, 0, currentCounts, result, maxClicks);
        return result;
    }

    // Recursive helper to build combinations
    private void generateCombinations(ArrayList<Integer> buttons, int remaining, int index, int[] currentCounts, List<int[]> result, int maxClicks) {
        if (remaining < 0) return;  // Invalid, overshot
        if (index == buttons.size()) {
            if (remaining == 0) {
                result.add(currentCounts.clone());  // Found a valid combination
            }
            return;
        }
        // Try pressing this button 0 or more times (up to remaining, to prune)
        for (int presses = 0; presses <= Math.min(remaining, maxClicks); presses++) {
            currentCounts[index] = presses;
            generateCombinations(buttons, remaining - presses, index + 1, currentCounts, result, maxClicks);
        }
    }

    public String bfs2(ArrayList<String> input) {
        int result = 0;

        line: 
        for (String line: input) {
            String joltage = line.substring(line.indexOf("{")+1,line.length()-1);
            int[] targetJoltage = new int[joltage.length()];
            String[] joltages = joltage.split(",");
            for (int i = 0; i < joltages.length; i++) {
                targetJoltage[i] = Integer.parseInt(joltages[i]);
            }

            String[] buttonStrings = line.substring(
                line.indexOf("]")+1, 
                line.indexOf("{")).trim().split(" ");
            Button[] buttons = new Button[buttonStrings.length];
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(buttonStrings[i]);
            }

            // System.out.println(Arrays.toString(targetJoltage));
            // System.out.println(Arrays.toString(buttons));

            // We need to build a tree of button presses so that we can find the earliest set
            // of button presses that works.
            ArrayList<ArrayList<ButtonPress>> buttonPresses = new ArrayList<>();
            HashSet<ButtonPress> previousPresses = new HashSet<>();

            for (Button button : buttons) {
                ButtonPress press = new ButtonPress(button, new int[targetJoltage.length]);
                if (press.isOverJoltage(targetJoltage)) continue;
                if (press.isMatchedJoltage(targetJoltage)) {
                    result += 1;
                    continue line;
                }
                ArrayList<ButtonPress> firstPress = new ArrayList<>();
                firstPress.add(press);
                buttonPresses.add(firstPress);
                previousPresses.add(press);
            }

            while (buttonPresses.size() > 0) {
                ArrayList<ButtonPress> presses = buttonPresses.remove(0);
                int[] jlts = presses.get(presses.size() - 1).getResultJoltage(); 
                // for (ButtonPress p : presses) System.out.print(p + " ");
                // System.out.println();
                for (Button button : buttons) {
                    ButtonPress press = new ButtonPress(button, jlts);
                    // System.out.println("\t" + press + " " + presses.size());
                    // Avoid loops
                    if (previousPresses.contains(press)) continue;
                    if (press.isOverJoltage(targetJoltage)) continue;
                    if (press.isMatchedJoltage(targetJoltage)) {
                        // System.out.println("Found!");
                        result += presses.size()+1;
                        continue line;
                    }
                    ArrayList<ButtonPress> newPress = new ArrayList<>(presses);
                    newPress.add(press);
                    buttonPresses.add(newPress);
                    previousPresses.add(press);
                }                
            }
        }
        
        return String.valueOf(result);
    }

    public class Node {
        int[] buttons;

        public Node(String buttonList) {

        }
    }

    private class Button {
        int[] lightNumbers;
        public Button(String buttonString) {
            String lightList = buttonString.substring(1, buttonString.length() - 1);
            String[] lightStrings = lightList.split(",");
            lightNumbers = new int[lightStrings.length];
            for (int i = 0; i < lightStrings.length; i++) {
                lightNumbers[i] = Integer.parseInt(lightStrings[i]);
            }
        }

        public boolean[] press(boolean[] lightStates) {
            boolean[] newStates = Arrays.copyOf(lightStates, lightStates.length);
            
            // System.out.println(Arrays.toString(lightStates) + " " + Arrays.toString(lightNumbers));
            for (int light : lightNumbers) {
                newStates[light] = !newStates[light];
            }

            return newStates;
        }

        public int[] press(int[] currentJoltage) {
            int[] newStates = Arrays.copyOf(currentJoltage, currentJoltage.length);
            
            // System.out.println(Arrays.toString(lightStates) + " " + Arrays.toString(lightNumbers));
            for (int light : lightNumbers) {
                newStates[light] += 1;
            }

            return newStates;
        }

        public int[] press(int[] currentJoltage, int i) {
            int[] newStates = Arrays.copyOf(currentJoltage, currentJoltage.length);
            
            // System.out.println(Arrays.toString(lightStates) + " " + Arrays.toString(lightNumbers));
            for (int light : lightNumbers) {
                newStates[light] -= i;
            }

            return newStates;
        }

        public boolean contains(int i) {
            for (int light : lightNumbers) {
                if (i == light) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Button " + Arrays.toString(lightNumbers);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + Arrays.hashCode(lightNumbers);
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
            Button other = (Button) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (!Arrays.equals(lightNumbers, other.lightNumbers))
                return false;
            return true;
        }

        private Day10 getEnclosingInstance() {
            return Day10.this;
        }

    }

    private class ButtonPress {
        private Button button;
        private boolean isJoltage;
        private boolean[] initialState;
        private boolean[] resultState;
        private int[] initialJoltage;
        private int[] resultJoltage;

        public ButtonPress(Button button, boolean[] initialState) {
            this.button = button;
            this.initialState = initialState;
            this.resultState = button.press(initialState);
            this.isJoltage = false;
        }

        public ButtonPress(Button button, int[] initialJoltage) {
            this.button = button;
            this.initialJoltage = initialJoltage;
            this.resultJoltage = button.press(initialJoltage);
            this.isJoltage = true;
        }

        public boolean isConfigured(boolean[] targetState) {
            for (int i = 0; i < resultState.length; i++) {
                if (resultState[i] != targetState[i]) return false;
            }
            return true;
        }

        public boolean isMatchedJoltage(int[] targetJoltage) {
            for (int i = 0; i < resultJoltage.length; i++) {
                if (resultJoltage[i] != targetJoltage[i]) return false;
            }
            return true;
        }

        public boolean isOverJoltage(int[] targetJoltage) {
            for (int i = 0; i < resultJoltage.length; i++) {
                if (resultJoltage[i] > targetJoltage[i]) return true;
            }
            return false;
        }

        public boolean[] getResultState() {
            return resultState;
        }

        public int[] getResultJoltage() {
            return resultJoltage;
        }

        @Override
        public String toString() {
            return "ButtonPress [button=" + button + 
            (isJoltage ? ", initialStates=" + Arrays.toString(initialJoltage) + "]" : 
                        ", initialJoltage=" + Arrays.toString(initialState) + "]");
        }
 
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((button == null) ? 0 : button.hashCode());
            result = prime * result + (isJoltage ? 0 : 1);
            result = prime * result + (isJoltage?Arrays.hashCode(initialJoltage):Arrays.hashCode(initialState));
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
            ButtonPress other = (ButtonPress) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (button == null) {
                if (other.button != null)
                    return false;
            } else if (!button.equals(other.button))
                return false;
            if (isJoltage != other.isJoltage) return false;
            if (isJoltage) {
                if (!Arrays.equals(initialJoltage, other.initialJoltage))
                    return false;
            } else {
                if (!Arrays.equals(initialState, other.initialState))
                    return false;
            }
            return true;
        }

        private Day10 getEnclosingInstance() {
            return Day10.this;
        }

        
    }
}

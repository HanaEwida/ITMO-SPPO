import java.util.*;

public class SearchOperation {
    private final List<Shorty> cell;
    private final Random random;

    public SearchOperation(List<Shorty> cell, Random random) {
        this.cell = cell;
        this.random = random;
    }

    public void execute() {
        System.out.println("\n--- SEARCH FOR DUNNO'S HAT ---");

        // LOCAL CLASS requirement (defined inside method)
        class SearchManager {
            boolean searchForHat(String owner) {
                System.out.println("Search initiated for " + owner + "'s hat...");

                // Search success depends on RANDOMNESS + character traits
                for (Shorty s : cell) {
                    if (s instanceof Inhabitant inhabitant) {
                        // Reasonable inhabitants have higher success rate
                        double successRate = inhabitant.isReasonable() ? 0.8 : 0.3;

                        if (random.nextDouble() < successRate) {
                            System.out.println(" " + inhabitant.name + " found " + owner + "'s hat under a shelf!");
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        SearchManager searcher = new SearchManager();

        if (!searcher.searchForHat("Dunno")) {
            // UNCHECKED EXCEPTION based on runtime conditions
            try {
                throw new ItemNotFoundException("Dunno's hat remains missing after thorough search!");
            } catch (ItemNotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("Dunno and Striga climb onto shelves, disappointed.");
            }
        }
    }
}

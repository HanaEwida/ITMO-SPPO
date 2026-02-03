import java.util.*;

public class PoliceIntervention {
    private final Officer officer;
    private final List<Shorty> cell;

    public PoliceIntervention(Officer officer, List<Shorty> cell) {
        this.officer = officer;
        this.cell = cell;
    }

    public boolean execute() {
        System.out.println("--- POLICE INTERVENTION PHASE ---");

        // Check if anyone is visible (behavior depends on initial state)
        boolean anyoneActive = cell.stream()
                .filter(p -> p instanceof Inhabitant)
                .anyMatch(p -> p.state == Condition.ACTIVE);

        if (!anyoneActive) {
            System.out.println(" No inhabitants visible! Officers stand idle.");
            System.out.println("Inhabitants who were HIDING remain safe.");
            return true; // Continue simulation
        }

        System.out.println("Officers spot active inhabitants and move in!");

        try {
            // ANONYMOUS CLASS requirement: water bucket used during intervention
            Interactable waterBucket = new Interactable() {
                @Override public void performMainAction() {
                    System.out.println(" Water bucket ready for stove dousing!");
                }
                @Override public void act() {}
                @Override public void updateState() {}
            };
            waterBucket.performMainAction();

            // Shock all active inhabitants
            for (Shorty p : cell) {
                if (p instanceof Inhabitant inhabitant && inhabitant.state == Condition.ACTIVE) {
                    officer.shock(inhabitant); // May throw CHECKED exception
                }
            }
            officer.act(); // Officers form line and retreat

            return true;

        } catch (PoliceActionException e) {
            System.out.println("\n " + e.getMessage());
            System.out.println("Baton malfunction! Officers retreat without subduing anyone.");
            return false; // Scenario branches differently - early termination
        }
    }
}
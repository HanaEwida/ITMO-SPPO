import java.util.*;

public class RecoveryPhase {
    private final List<Shorty> cell;
    private final Random random;

    public RecoveryPhase(List<Shorty> cell, Random random) {
        this.cell = cell;
        this.random = random;
    }

    public void execute() {
        System.out.println("\n--- RECOVERY PHASE ---");

        for (Shorty p : cell) {
            if (!(p instanceof Inhabitant inhabitant)) continue;

            System.out.print(inhabitant.name + " (state: " + inhabitant.state + ") → ");

            // Behavior varies based on INITIAL state (HIDING vs STUNNED)
            switch (inhabitant.state) {
                case HIDING -> {
                    System.out.print("was hiding safely → ");
                    inhabitant.updateState(); // HIDING → ACTIVE
                    inhabitant.performMainAction(); // Behavior varies by isReasonable
                }
                case STUNNED -> {
                    // RANDOM recovery timing (33% chance to recover immediately)
                    if (random.nextInt(3) == 0) {
                        System.out.print("recovers quickly → ");
                        inhabitant.updateState(); // STUNNED → RECOVERING
                        inhabitant.updateState(); // RECOVERING → ACTIVE
                        inhabitant.performMainAction();
                    } else {
                        System.out.println("remains stunned on the floor.");
                    }
                }
                case RECOVERING -> {
                    System.out.print("continues recovering → ");
                    inhabitant.updateState(); // RECOVERING → ACTIVE
                    inhabitant.performMainAction();
                }
                case ACTIVE -> {
                    System.out.print("already active → ");
                    inhabitant.performMainAction(); // Behavior varies by isReasonable
                }
            }
        }
    }
}
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== JAIL CELL SIMULATION (SOLID-Compliant) ===\n");

        // Single source of randomness for reproducible variability
        Random random = new Random();

        // SRP: Character creation separated
        CharacterFactory factory = new CharacterFactory(random);
        List<Shorty> cell = factory.createCellInhabitants();
        Officer drigl = factory.createOfficer();

        // SRP: Police intervention separated
        PoliceIntervention intervention = new PoliceIntervention(drigl, cell);
        boolean continueSimulation = intervention.execute();

        if (!continueSimulation) {
            System.out.println("\n=== SIMULATION TERMINATED EARLY (Baton failure) ===");
            return;
        }

        // SRP: Recovery phase separated
        RecoveryPhase recovery = new RecoveryPhase(cell, random);
        recovery.execute();

        // SRP: Search operation separated
        SearchOperation search = new SearchOperation(cell, random);
        search.execute();

        System.out.println("\n=== SIMULATION COMPLETE ===");
    }
}
import java.util.*;

public class CharacterFactory {
    private final Random random;

    public CharacterFactory(Random random) {
        this.random = random;
    }

    public List<Shorty> createCellInhabitants() {
        List<Shorty> inhabitants = new ArrayList<>();

        // Initial characteristics RANDOMLY generated → affects later behavior
        Condition strigaState = random.nextBoolean() ? Condition.ACTIVE : Condition.HIDING;
        boolean strigaReasonable = random.nextBoolean();

        Condition dunnoState = random.nextBoolean() ? Condition.ACTIVE : Condition.HIDING;
        // Dunno is always reasonable (story trait)

        Condition kozlikState = random.nextBoolean() ? Condition.ACTIVE : Condition.HIDING;
        boolean kozlikReasonable = random.nextBoolean();

        System.out.println("=== CELL OCCUPANTS ===");
        System.out.println("Striga: " + strigaState + " | Reasonable: " + strigaReasonable);
        System.out.println("Dunno: " + dunnoState + " | Reasonable: true");
        System.out.println("Kozlik: " + kozlikState + " | Reasonable: " + kozlikReasonable);
        System.out.println();

        inhabitants.add(new Inhabitant("Striga", strigaState, strigaReasonable));
        inhabitants.add(new Inhabitant("Dunno", dunnoState, true));
        inhabitants.add(new Inhabitant("Kozlik", kozlikState, kozlikReasonable));

        return inhabitants;
    }

    public Officer createOfficer() {
        // Baton power RANDOMLY generated → affects shock success
        int batonPower = random.nextInt(3) == 0 ? 0 : 100; // 33% chance of malfunction
        System.out.println("Officer Drigl's baton power: " + batonPower +
                (batonPower == 0 ? "  MALFUNCTIONING" : " FUNCTIONAL"));
        System.out.println();
        return new Officer("Drigl", new Equipment("Shock Baton", batonPower));
    }
}

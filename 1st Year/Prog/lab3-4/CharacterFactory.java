import java.util.*;

public class CharacterFactory {
    private final Random random;

    public CharacterFactory(Random random) {
        this.random = random;
    }

    public CellEnvironment createCellEnvironment() {
        List<Shorty> occupants = new ArrayList<>();

        boolean strigaHides = random.nextBoolean();
        boolean strigaReasonable = random.nextBoolean();
        Condition strigaState = strigaHides ? Condition.HIDING : Condition.ACTIVE;
        Location strigaLoc = strigaHides ? new UnderShelf() : new CellFloor();

        boolean dunnoHides = random.nextBoolean();
        Condition dunnoState = dunnoHides ? Condition.HIDING : Condition.ACTIVE;
        Location dunnoLoc = dunnoHides ? new UnderShelf() : new CellFloor();

        boolean kozlikHides = random.nextBoolean();
        boolean kozlikReasonable = random.nextBoolean();
        Condition kozlikState = kozlikHides ? Condition.HIDING : Condition.ACTIVE;
        Location kozlikLoc = kozlikHides ? new UnderShelf() : new CellFloor();

        System.out.println("=== CELL OCCUPANTS (INITIAL STATE) ===");
        System.out.println("Striga: " + strigaState + " at " + strigaLoc.getDescription() + " | Reasonable: " + strigaReasonable);
        System.out.println("Dunno: " + dunnoState + " at " + dunnoLoc.getDescription() + " | Reasonable: true");
        System.out.println("Kozlik: " + kozlikState + " at " + kozlikLoc.getDescription() + " | Reasonable: " + kozlikReasonable);
        System.out.println();

        occupants.add(new Inhabitant("Striga", strigaState, strigaLoc, strigaReasonable));
        occupants.add(new Inhabitant("Dunno", dunnoState, dunnoLoc, true));
        occupants.add(new Inhabitant("Kozlik", kozlikState, kozlikLoc, kozlikReasonable));

        List<Item> items = new ArrayList<>();
        Location[] possibleLocations = {
                new CellFloor(), new UnderShelf(),
                new Shelf(1), new Shelf(2), new Shelf(3)
        };

        Location hatLocation = possibleLocations[random.nextInt(possibleLocations.length)];
        items.add(new Item("hat", "Dunno", hatLocation));
        items.add(new Item("blanket", "Striga", possibleLocations[random.nextInt(possibleLocations.length)]));
        items.add(new Item("book", "Kozlik", possibleLocations[random.nextInt(possibleLocations.length)]));

        System.out.println("=== MISPLACED ITEMS AFTER FIGHT ===");
        for (Item item : items) {
            System.out.println(item.getName() + " (owner: " + item.getOwner() + ") → " + item.getLocation().getDescription());
        }
        System.out.println();

        int batonPower = random.nextInt(3) == 0 ? 0 : 100;
        System.out.println("Officer Drigl's baton power: " + batonPower +
                (batonPower == 0 ? " → MALFUNCTIONING!" : " → FUNCTIONAL"));
        System.out.println();

        Officer drigl = new Officer("Drigl", new Equipment("Shock Baton", batonPower));
        occupants.add(0, drigl);

        return new CellEnvironment(occupants, items, new Stove(), new WaterSource());
    }
}

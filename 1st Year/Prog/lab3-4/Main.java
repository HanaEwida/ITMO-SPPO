import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== JAIL CELL SIMULATION (TRUE OOP DESIGN) ===\n");
        Random random = new Random();

        CharacterFactory factory = new CharacterFactory(random);
        CellEnvironment cell = factory.createCellEnvironment();
        List<Shorty> occupants = cell.getOccupants();

        Officer drigl = (Officer) occupants.get(0);
        List<Inhabitant> inhabitants = occupants.stream()
                .filter(s -> s instanceof Inhabitant)
                .map(s -> (Inhabitant) s)
                .toList();

        System.out.println("--- POLICE INTERVENTION ---");

        boolean anyoneActive = inhabitants.stream()
                .anyMatch(s -> s.getState() == Condition.ACTIVE);

        if (!anyoneActive) {
            System.out.println("No inhabitants visible! Officers stand idle.");
            System.out.println("Inhabitants hiding under shelves remain safe.\n");
        } else {
            System.out.println("Officers spot active inhabitants and move in!\n");

            try {
                for (Inhabitant inhabitant : inhabitants) {
                    if (inhabitant.getState() == Condition.ACTIVE) {
                        drigl.shock(inhabitant);
                    }
                }

                System.out.println("\n--- STOVE INTERACTION ---");
                drigl.douseStove(cell.getStove(), cell.getWaterSource());

                System.out.println("\n--- OFFICER REACTIONS ---");
                Officer sigl = new Officer("Sigl", new Equipment("Baton", 100));
                Officer zhigl = new Officer("Zhigl", new Equipment("Baton", 100));
                Officer phigl = new Officer("Phigl", new Equipment("Baton", 100));

                Runnable laughter = new Runnable() {
                    public void run() {
                        sigl.laughAtSteam();
                        zhigl.laughAtSteam();
                        phigl.laughAtSteam();
                        System.out.println("After laughing their fill, the officers compose themselves.");
                    }
                };
                laughter.run();

                drigl.formLineWith(List.of(sigl, zhigl, phigl));

            } catch (PoliceActionException e) {
                System.out.println("\n" + e.getMessage());
                System.out.println("Baton malfunction! Officers retreat without subduing anyone.");
                System.out.println("=== SIMULATION TERMINATED EARLY ===");
                return;
            }
        }

        System.out.println("\n--- RECOVERY PHASE ---");

        System.out.println("\nHidden inhabitants emerge from under shelves:");
        for (Inhabitant inhabitant : inhabitants) {
            if (inhabitant.isHiding()) {
                inhabitant.emerge();
            }
        }

        System.out.println("\nStunned inhabitants recover:");
        boolean recovering;
        do {
            recovering = false;
            for (Inhabitant inhabitant : inhabitants) {
                if (inhabitant.getState() == Condition.STUNNED ||
                        inhabitant.getState() == Condition.RECOVERING) {
                    recovering |= inhabitant.recover(random);
                }
            }
            if (recovering) System.out.println("---");
        } while (recovering);

        System.out.println("\n--- CLEANUP & SEARCH ---");

        for (Inhabitant inhabitant : inhabitants) {
            if (inhabitant.isActive() || inhabitant.getState() == Condition.RECOVERING) {
                inhabitant.tidyUp();
            }
        }

        class SearchCoordinator {
            boolean coordinateSearchForHat(Inhabitant searcher1, Inhabitant searcher2, List<Item> items) {
                System.out.println("\n--- SEARCH FOR DUNNO'S HAT ---");
                System.out.println(searcher1.getName() + " and " + searcher2.getName() + " search desperately for the missing hat...");

                Location[] searchLocations = {
                        new CellFloor(), new UnderShelf(),
                        new Shelf(1), new Shelf(2), new Shelf(3)
                };

                for (Location loc : searchLocations) {
                    Item found = searcher1.search(loc, items);
                    if (found != null && found.getName().equals("hat")) {
                        System.out.println(searcher1.getName() + " found " + found.getOwner() + "'s " + found.getName() +
                                " under " + loc.getDescription() + "!");
                        return true;
                    }
                }
                return false;
            }
        }

        SearchCoordinator coordinator = new SearchCoordinator();
        Inhabitant striga = inhabitants.stream().filter(i -> i.getName().equals("Striga")).findFirst().get();
        Inhabitant dunno = inhabitants.stream().filter(i -> i.getName().equals("Dunno")).findFirst().get();

        if (!coordinator.coordinateSearchForHat(striga, dunno, cell.getItems())) {
            try {
                throw new ItemNotFoundException("Dunno's hat remains missing after thorough search!");
            } catch (ItemNotFoundException e) {
                System.out.println("\n" + e.getMessage());
                System.out.println("Striga and Dunno climb onto shelves, disappointed.");

                Inhabitant kozlik = inhabitants.stream().filter(i -> i.getName().equals("Kozlik")).findFirst().get();
                kozlik.approach(dunno);
                System.out.println("Kozlik and Dunno wander between shelves, looking for free space.");
            }
        }

        System.out.println("\n=== SIMULATION COMPLETE ===");
    }
}
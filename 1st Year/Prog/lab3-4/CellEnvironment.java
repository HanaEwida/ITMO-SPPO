import java.util.*;

public class CellEnvironment {
    private final List<Shorty> occupants;
    private final List<Item> items;
    private final Stove stove;
    private final WaterSource waterSource;
    private final List<Shelf> shelves;

    public CellEnvironment(List<Shorty> occupants, List<Item> items, Stove stove, WaterSource waterSource) {
        this.occupants = occupants;
        this.items = items;
        this.stove = stove;
        this.waterSource = waterSource;
        this.shelves = List.of(new Shelf(1), new Shelf(2), new Shelf(3));
    }

    public List<Shorty> getOccupants() { return occupants; }
    public List<Item> getItems() { return items; }
    public Stove getStove() { return stove; }
    public WaterSource getWaterSource() { return waterSource; }
    public List<Shelf> getShelves() { return shelves; }

    public List<Shorty> getHidingOccupants() {
        return occupants.stream().filter(Shorty::isHiding).toList();
    }

    public List<Shorty> getStunnedOccupants() {
        return occupants.stream()
                .filter(s -> s.getState() == Condition.STUNNED || s.getState() == Condition.RECOVERING)
                .toList();
    }
}

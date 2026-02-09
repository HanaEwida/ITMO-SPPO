public class Item {
    private final String name;
    private final String owner;
    private Location location;

    public Item(String name, String owner, Location initialLocation) {
        this.name = name;
        this.owner = owner;
        this.location = initialLocation;
    }

    public boolean isAt(Location loc) { return location.equals(loc); }
    public void moveTo(Location newLoc) { this.location = newLoc; }
    public String getName() { return name; }
    public String getOwner() { return owner; }
    public Location getLocation() { return location; }
}
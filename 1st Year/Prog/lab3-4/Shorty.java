import java.util.Objects;
import java.util.Random;

public abstract class Shorty {
    protected String name;
    protected Condition state;
    protected Location location;
    protected final boolean isReasonable;

    public Shorty(String name, Condition initialState, Location initialLocation, boolean isReasonable) {
        this.name = name;
        this.state = initialState;
        this.location = initialLocation;
        this.isReasonable = isReasonable;
    }

    public void hide() {
        if (state == Condition.ACTIVE && !(location instanceof UnderShelf)) {
            this.location = new UnderShelf();
            this.state = Condition.HIDING;
            System.out.println(name + " hides " + location.getDescription() + ".");
        }
    }

    public void emerge() {
        if (state == Condition.HIDING && location instanceof UnderShelf) {
            this.location = new CellFloor();
            this.state = Condition.ACTIVE;
            System.out.println(name + " emerges from under the shelves.");
        }
    }

    public void receiveShock() {
        if (state == Condition.ACTIVE) {
            this.state = Condition.STUNNED;
            System.out.println(name + " collapses stunned on the " + location.getDescription() + "!");
        }
    }

    public boolean recover(Random random) {
        if (state == Condition.STUNNED && random.nextInt(3) == 0) {
            this.state = Condition.RECOVERING;
            System.out.println(name + " starts to come to their senses.");
            return true;
        } else if (state == Condition.RECOVERING) {
            this.state = Condition.ACTIVE;
            System.out.println(name + " fully recovers and stands up.");
            return true;
        }
        return false;
    }

    public Item search(Location targetLocation, java.util.List<Item> items) {
        if (state != Condition.ACTIVE && state != Condition.RECOVERING) {
            System.out.println(name + " is unable to search while " + state + ".");
            return null;
        }

        double successRate = isReasonable ? 0.8 : 0.3;
        if (Math.random() < successRate) {
            return items.stream()
                    .filter(item -> item.isAt(targetLocation))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public void moveTo(Location newLocation) {
        if (state == Condition.ACTIVE || state == Condition.RECOVERING) {
            this.location = newLocation;
            System.out.println(name + " moves to " + newLocation.getDescription() + ".");
        }
    }

    public void approach(Shorty other) {
        if (state == Condition.ACTIVE && other.state == Condition.ACTIVE) {
            System.out.println(name + " approaches " + other.name + " at " + other.location.getDescription() + ".");
            this.location = other.location;
        }
    }

    public String getName() { return name; }
    public Condition getState() { return state; }
    public Location getLocation() { return location; }
    public boolean isHiding() { return state == Condition.HIDING; }
    public boolean isActive() { return state == Condition.ACTIVE; }
    public boolean isReasonable() { return isReasonable; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shorty shorty)) return false;
        return Objects.equals(name, shorty.name) && state == shorty.state;
    }

    @Override
    public int hashCode() { return Objects.hash(name, state); }

    @Override
    public String toString() {
        return "[Character: " + name + " | State: " + state + " | Location: " + location.getDescription() + "]";
    }
}

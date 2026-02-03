import java.util.Objects;

public abstract class Shorty implements Interactable {
    protected String name;
    protected Condition state;

    public Shorty(String name, Condition state) {
        this.name = name;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shorty shorty)) return false;
        return Objects.equals(name, shorty.name) && state == shorty.state;
    }

    @Override
    public int hashCode() { return Objects.hash(name, state); }

    @Override
    public String toString() { return "[Character: " + name + " | State: " + state + "]"; }
}
public class Inhabitant extends Shorty {
    public Inhabitant(String name, Condition initialState, Location initialLocation, boolean isReasonable) {
        super(name, initialState, initialLocation, isReasonable);
    }

    public void tidyUp() {
        if (state == Condition.ACTIVE || state == Condition.RECOVERING) {
            if (isReasonable()) {
                System.out.println(name + " carefully searches for belongings and helps tidy up the cell.");
            } else {
                System.out.println(name + " frantically searches for their things.");
            }
        }
    }
}

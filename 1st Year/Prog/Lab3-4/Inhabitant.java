public class Inhabitant extends Shorty {
    private boolean isReasonable;

    public Inhabitant(String name, Condition state, boolean isReasonable) {
        super(name, state);
        this.isReasonable = isReasonable;
    }

    public void receiveShock() {
        this.state = Condition.STUNNED;
    }

    @Override
    public void updateState() {
        if (state == Condition.HIDING) {
            state = Condition.ACTIVE;
            System.out.println(name + " emerges from under the shelves.");
        } else if (state == Condition.STUNNED) {
            state = Condition.RECOVERING;
            System.out.println(name + " starts to come to their senses.");
        }
    }

    @Override
    public void act() {
        performMainAction();
    }



    public boolean isReasonable() {
        return isReasonable;
    }

    @Override
    public void performMainAction() {
        if (state == Condition.RECOVERING || state == Condition.ACTIVE) {
            if (isReasonable) {
                System.out.println(name + " carefully searches for belongings and helps tidy up.");
            } else {
                System.out.println(name + " frantically searches for their things.");
            }
        }
    }
}
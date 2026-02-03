public class Officer extends Shorty {
    private Equipment baton;

    public Officer(String name, Equipment baton) {
        super(name, Condition.ACTIVE); // FIXED: CONSCIOUS → ACTIVE
        this.baton = baton;
    }

    public void shock(Shorty target) throws PoliceActionException {
        if (!baton.isFunctional()) {
            throw new PoliceActionException("Baton malfunctioned during use!");
        }
        System.out.println("Officer " + name + " shocks " + target.name + " with " + baton.name());
        if (target instanceof Inhabitant inhabitant) {
            inhabitant.receiveShock();
        }
    }

    @Override
    public void updateState() {
        if (state == Condition.STUNNED) {
            state = Condition.RECOVERING;
            System.out.println("Officer " + name + " is recovering from stun.");
        }
    }

    @Override
    public void act() {
        System.out.println("Officer " + name + " forms a line and retreats to original positions.");
    }

    @Override
    public void performMainAction() {
        act();
    }
}
import java.util.List;

public class Officer extends Shorty {
    private Equipment baton;

    public Officer(String name, Equipment baton) {
        super(name, Condition.ACTIVE, new CellFloor(), true);
        this.baton = baton;
    }

    public void shock(Shorty target) throws PoliceActionException {
        if (!baton.isFunctional()) {
            throw new PoliceActionException("Baton malfunctioned during use!");
        }
        System.out.println("Officer " + name + " shocks " + target.getName() + " with " + baton.name() + "!");
        target.receiveShock();
    }

    public void douseStove(Stove stove, WaterSource waterSource) {
        if (stove.isLit()) {
            waterSource.fetchWater();
            stove.extinguish();
        }
    }

    public void laughAtSteam() {
        System.out.println("Officer " + name + " bursts into loud laughter at the steam-filled cell!");
    }

    public void formLineWith(List<Officer> otherOfficers) {
        StringBuilder line = new StringBuilder("Officers " + name);
        for (Officer o : otherOfficers) {
            line.append(", ").append(o.name);
        }
        System.out.println(line + " form a line and retreat to original positions.");
        System.out.println("The cell door slams shut. The key rattles in the lock. Silence falls.");
    }
}

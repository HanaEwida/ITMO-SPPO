public class Stove {
    private boolean isLit;
    private boolean isKnockedOver;
    private boolean isExtinguished;

    public Stove() {
        this.isKnockedOver = true;
        this.isLit = true;
    }

    public void extinguish() {
        if (isLit && !isExtinguished) {
            isLit = false;
            isExtinguished = true;
            System.out.println("The fire in the overturned stove is extinguished.");
            produceSteam();
        }
    }

    private void produceSteam() {
        System.out.println("Thick steam instantly fills the cell!");
    }

    public boolean isLit() { return isLit; }
}
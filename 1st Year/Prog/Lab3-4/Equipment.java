public record Equipment(String name, int powerLevel) {
    public boolean isFunctional() {
        return powerLevel > 0;
    }
}
public record CellFloor() implements Location {
    public String getDescription() { return "cell floor"; }
    public boolean isHidingSpot() { return false; }
}
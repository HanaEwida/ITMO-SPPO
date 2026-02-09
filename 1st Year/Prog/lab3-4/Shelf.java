public record Shelf(int shelfNumber) implements Location {
    public String getDescription() { return "shelf #" + shelfNumber; }
    public boolean isHidingSpot() { return false; }
}

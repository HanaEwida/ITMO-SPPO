public record UnderShelf() implements Location {
    public String getDescription() { return "under the shelves"; }
    public boolean isHidingSpot() { return true; }
}

public sealed interface Location permits CellFloor, UnderShelf, Shelf {
    String getDescription();
    boolean isHidingSpot();
}
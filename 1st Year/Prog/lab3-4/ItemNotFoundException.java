public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) { super(message); }
    @Override public String getMessage() { return "UNCHECKED ERROR: " + super.getMessage(); }
}

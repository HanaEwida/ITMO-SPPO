public class PoliceActionException extends Exception {
    public PoliceActionException(String message) { super(message); }
    @Override public String getMessage() { return "CHECKED ERROR: " + super.getMessage(); }
}

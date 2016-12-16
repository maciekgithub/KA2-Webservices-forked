package pl.euler.bgs.restapi.web.api.params;

public class IncorrectHeaderException extends IllegalArgumentException {
    public IncorrectHeaderException(String s) {
        super(s);
    }
}

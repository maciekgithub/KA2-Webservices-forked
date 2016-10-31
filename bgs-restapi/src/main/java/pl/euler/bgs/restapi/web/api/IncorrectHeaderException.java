package pl.euler.bgs.restapi.web.api;

public class IncorrectHeaderException extends IllegalArgumentException {
    public IncorrectHeaderException(String s) {
        super(s);
    }
}

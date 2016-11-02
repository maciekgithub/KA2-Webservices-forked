package pl.euler.bgs.restapi.web.api.headers;

public class IncorrectHeaderException extends IllegalArgumentException {
    public IncorrectHeaderException(String s) {
        super(s);
    }
}

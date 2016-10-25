package pl.euler.bgs.restapi.web.api;

public class MissingHeaderException extends IllegalArgumentException {
    public MissingHeaderException(String s) {
        super(s);
    }
}

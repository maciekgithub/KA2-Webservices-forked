package pl.euler.bgs.restapi.web.api.headers;

public class MissingHeaderException extends IllegalArgumentException {
    public MissingHeaderException(String s) {
        super(s);
    }
}

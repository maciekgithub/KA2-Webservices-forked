package pl.euler.bgs.restapi.web.api.params;

public class MissingHeaderException extends IllegalArgumentException {
    public MissingHeaderException(String s) {
        super(s);
    }
}

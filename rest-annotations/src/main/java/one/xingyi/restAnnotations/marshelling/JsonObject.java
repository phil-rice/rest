package one.xingyi.restAnnotations.marshelling;
public class JsonObject {
    final String string;
    public JsonObject(String string) {
        this.string = string;
    }
    @Override public String toString() {
        return string;
    }
}

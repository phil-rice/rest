package one.xingyi.restAnnotations.marshelling;
public interface HasJson {
    <J> J toJson(JsonTC<J> jsonTc);
    default <J> String toJsonString(JsonTC<J> jsonTc) { return jsonTc.fromJ(toJson(jsonTc)); }

}

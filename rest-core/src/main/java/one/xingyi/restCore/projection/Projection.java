package one.xingyi.restCore.projection;
public class Projection {
    public static Projection project(Field... fields) {
        return new Projection();
    }
    public static Field stringField(String name) {
        return new Field();
    }
    public static Field objectField(String name) {
        return new Field();
    }
}


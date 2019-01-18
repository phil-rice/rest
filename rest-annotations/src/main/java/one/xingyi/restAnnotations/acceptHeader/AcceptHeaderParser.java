package one.xingyi.restAnnotations.acceptHeader;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
public interface AcceptHeaderParser extends Function<String, AcceptHeaderDetails> {
    static AcceptHeaderParser parser = new SimpleAcceptHeaderParser();

}

class SimpleAcceptHeaderParser implements AcceptHeaderParser {

    @Override public AcceptHeaderDetails apply(String header) {
        List<String> parts = ListUtils.filter(Arrays.asList(header.split("\\.")), part -> part.length() > 0);
        if (parts.size() < 2) return AcceptHeaderDetails.invalid();
        if (!parts.get(0).equalsIgnoreCase("application/xingyi") || !parts.get(1).equalsIgnoreCase("json"))
            return AcceptHeaderDetails.invalid();
        List<String> lensNames = ListUtils.map(parts.subList(2, parts.size()), s -> trimName(header, s));

        return AcceptHeaderDetails.valid(lensNames);
    }
    private String trimName(String header, String s) {
        if (s.startsWith("lens_")) return s.substring(5);
        throw new RuntimeException("Lens name '" + s + " should start with 'lens_' the header was " + header);
    }
}

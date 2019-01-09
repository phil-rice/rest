package one.xingyi.restAnnotations;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
public class Files {

    public static void setText(Callable<PrintWriter> writer, String text) {
        WrappedException.wrap(() -> {
            try (PrintWriter printWriter = writer.call()) {
                printWriter.print(text);
                printWriter.flush();
            }
        });
    }
}

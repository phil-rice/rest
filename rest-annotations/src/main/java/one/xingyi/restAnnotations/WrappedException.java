package one.xingyi.restAnnotations;

import java.util.concurrent.Callable;
public class WrappedException extends RuntimeException {

    public WrappedException(Throwable cause) {
        super(cause);
    }
    public static <T> T wrap(Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }
    public static void wrap(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }
    public static Throwable unWrap(Throwable e) {
        if (e instanceof WrappedException)
            return e.getCause();
        else return e;
    }
}

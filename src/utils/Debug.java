package utils;

public class Debug {

    private static Logger logger = new StackLogger();


    static private class Logger {
        protected void log(String string) {
            System.out.println(string);
        }
    }

    static private class NullLoger extends Logger {
        protected void log(String string) {
            // Do nothing - loggin is off.
        }
    }

    static private class StackLogger extends Logger {
        protected void log(String string) {
            StackTraceElement[] stack_trace = Thread.currentThread().getStackTrace();
            System.out.println(stack_trace[3] + "\t" + string);
        }
    }


    public static void log(String string) {
        logger.log(string);
    }

    public static void log(int value) {
        logger.log(String.valueOf(value));
    }

    public static void log(Object object) {
        logger.log(object.toString());
    }

}

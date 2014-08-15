package li.raphael.lombock.hello;

public class AccessRestrictionUtils {

    public static boolean isAllowed(String packageName) {
        return Thread.currentThread().getStackTrace()[3].getClassName().startsWith(packageName);
    }

}

package li.raphael.lombok;

import li.raphael.lombock.hello.RestrictedAccess;

public class ExampleUsage {

    @RestrictedAccess("li.raphael.lombok.test")
    public ExampleUsage() {
        System.out.println("This is a private API, so nobody from this package"
                + "shall be able to access this!");
    }

}

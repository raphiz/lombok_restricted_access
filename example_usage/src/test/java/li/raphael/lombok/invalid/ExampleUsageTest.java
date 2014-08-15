package li.raphael.lombok.invalid;

import li.raphael.lombok.ExampleUsage;
import org.junit.Test;

public class ExampleUsageTest {

    @Test(expected = RuntimeException.class)
    public void test() {
        new ExampleUsage();
    }
}

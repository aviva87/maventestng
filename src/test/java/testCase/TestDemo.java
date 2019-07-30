package testCase;

import org.testng.Assert;
import org.testng.annotations.*;

//@Listeners(TestListenerAdapterImp.class)
public class TestDemo {
    @Test
    public void testcase1(){
        Assert.assertTrue(1 == 1);
        System.out.println("testcase1");
    }

    // test case 2
    @Test
    public void testCase2() {
        System.out.println("This is a test case 2");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("This is beforeMethod");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("This is afterMethod");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("This is beforeClass");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("This is afterClass");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("This is beforeTest");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("This is afterTest");
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("This is beforeSuite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("This is afterSuite");
    }
}

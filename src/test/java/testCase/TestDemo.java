package testCase;

import org.testng.Assert;
import org.testng.annotations.*;


//@Listeners(TestListenerAdapterImp.class)
public class TestDemo {
    @Test
    public void testcase1(){

        Assert.assertTrue(1 == 1, "===================assert test===========");
        System.out.println("testcase1");
    }

    // test case 2
    @Test
    public void testCase2() {
        System.out.println("This is a test case 2");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("+++++maventestng TestDemo testCase2 start+++++");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("-----maventestng TestDemo testCase2 end-----");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("$$$$$maventestng TestDemo start$$$$$");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("$$$$$maventestng TestDemo end$$$$$");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("*****maventestng test start*****");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("*****maventestng test end*****");
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("=====maventestng suite start=====");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("=====maventestng suite end=====");
    }
}

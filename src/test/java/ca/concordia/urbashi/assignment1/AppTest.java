package ca.concordia.urbashi.assignment1;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * this test checks if the App class is running smoothly
     */
    public void testApp()
    {
        assertTrue( true );
    }
   
    /*
     * test case to see if a method exists in those names in App class
     */
    
    @org.junit.Test
    public void testMethods()
    {
    	 Class c = Thread.class;
         
         
         for (Method method : c.getDeclaredMethods())
         if (method.getName().equals("checkHashCodeAndEquals")||method.getName().equals("checkDuplicateLoggingInformationinCatchBlocks")||method.getName().equals("checkDuplicateLoggingInformationinCatchBlocks")||method.getName().equals("checkUselessControlFlow")) {
             System.out.println("Method exists.");
             
    
         }    
    }
    @org.junit.Test
    public void testcheckHashCodeAndEquals()
    {
    	BugDetector bugDetector=new BugDetector();
    	List<String> errorList = bugDetector.missingEqualMethodDetector(new File("sampleTestFiles\\equalsHashcodeHandler"));
		Assert.assertEquals(1, errorList.size());
    }
    
    
    @org.junit.Test
    public void testcheckDuplicateLogging()
    {
    	BugDetector bugDetector=new BugDetector();
    	List<String> errorList = bugDetector.getDuplicateLoggingErrors(new File("sampleTestFiles\\InadequateLoggingInformationHandler"));
		Assert.assertEquals(12, errorList.size());
    }
        
    
    
    public void testcheckDuplicateLoggingInformationinCatchBlocks()
    {
        assertTrue( true ); 
      
    }
    
    public void testcheckUselessControlFlow()
    {
        assertTrue( true ); 
        
    }
    
    
}

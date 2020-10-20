/**
 * A test function that has a DUPLICATE LOGGING INFORMATION IN catch block of same try or catch block has no logging information
 */
public class InadequateLoggingInformationInCatchBlocks {
	 public static void anotherMain(String[] args) {
	        try {
	        	
	        }
	        catch(ArrayIndexOutOfBoundsException e) {
	        	System.out.println("hello");
	        	System.out.println("hello 123");
	        }
	        catch(ArrayIndexOutOfBoundsException e) {
	        	System.out.println("hello");
	        	System.out.println("hello 123");
	        }
	        catch(ArrayIndexOutOfBoundsException e) {
	        	//System.out.println("hello");
	        	System.out.println("hello 123");
	        }
	    }
	    public static void another(String[] args) {

	    try {
	    	
	    }
	    catch(ArrayIndexOutOfBoundsException e) {
	    	System.out.println();
	    }}
}
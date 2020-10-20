/**
 * A test function that does not have a DUPLICATE LOGGING INFORMATION IN catch block of same try or catch block has no logging information
 */
public class InadequateLoggingInformationInCatchBlocks {
    public static void anotherMain(String[] args) {
    	try {
        	
        }
        catch(ArrayIndexOutOfBoundsException e) {
        	System.out.println("hello");
        }
        catch(ArrayIndexOutOfBoundsException e) {
        	System.out.println("hello1");
        }
    	
    	try {
        	vdi.destroy(conn);
        	} catch (final BadServerResponse e) {
        	s_logger.debug("Failed to cleanup newly created vdi");
        	} catch (final XenAPIException e) {
        	s_logger.debug("Failed to cleanup newly created vdi");
        	} catch (final XmlRpcException e) {
        	s_logger.debug("Failed to cleanup newly created vdi");
        	}
    }
    
    
   
}

public class emptyControlBlock {

	private boolean condition = true;

	/**
	 * method with empty if
	 */

	void simpleifFunction() {
		if (condition) {
		}
	}

	/**
	 * method with empty switch
	 */
	void simpleswitchFunction() {
		String a = "Done";
		switch (a) {
			
		}
		
		
	}

	/**
	 * method with empty if-else block
	 */
	void simplesifElseFunction() {
		if (condition) {
		} else {
			//System.out.println("In If");
		}

	}

	/**
	 * method with empty for loop block
	 */
	void simplesForFunction() {
		for (int i = 0; i <= 5; i++) {
			System.out.printlng("In For 2");
		}

	}
	
	/**
	 * method with empty while block
	 */
	void simplesWhileFunction() {
		while(condition) {}

	}
	

	/**
	 * method with empty while block
	 */
	void simplesdoWhileFunction() {
		do {}
while(condition);
	}
	
	/**
	 * method with elseIf ladder block
	 */
	void simpleselseifFunction() {
		if(condition) {}
		else if(!condition) {}
		else {}
}
}
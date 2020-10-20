
public class notEmptyControlBlock {
	

		private boolean condition = true;

		/**
		 * method with  if
		 */

		void simpleifFunction() {
			if (condition) {
				System.out.print("In if");
			}
		}

		/**
		 * method with full switch
		 */
		void simpleswitchFunction() {
			int a = 12;
			switch (a) {
				 
			case 12:
				System.out.print("yes");
				break;
			}
			
			
		}

		/**
		 * method with empty if-else block
		 */
		void simplesifElseFunction() {
			if (condition) {
				System.out.print("if");
			} else {
				System.out.println("else");
			}

		}

		/**
		 * method with  for loop block
		 */
		void simplesForFunction() {
			for (int i = 0; i <= 5; i++) {
				System.out.println("In For 2");
			}

		}
		
		/**
		 * method with while block
		 */
		void simplesWhileFunction() {
			
		int b=1;
			while(b<=5) {
				System.out.println("In while");
			}
			
			b++;

		}
		

		/**
		 * method with while block
		 */
		void simplesdoWhileFunction() {
			int c=5;
			do {
				System.out.println("In do-while");
				c--;
			}
	while(c>1);
		}
		
		/**
		 * method with elseIf ladder block
		 */
		void simpleselseifFunction() {
			int i=3;
			if(i==3) {
				System.out.println("In else-if 3");
			}
			else if(i==2) {
				System.out.println("In else-if 2");

			}
			else {
				System.out.println("Not a match");
			}
	}
	}



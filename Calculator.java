/*
 * CISC.3150 HW 7
 * Command Line Calculator following the rules of BODMAS
 * Brackets of Division, Multiplication, Addition, Subtraction
 */

/**
 *
 * @author Jaryl
 */
import java.util.*;

public class Calculator{
	
    public static void main(String[] args){
        
        //https://stackoverflow.com/questions/12906700/postfix-calculator-how-to-deal-with-3-numbers-in-a-row
        //https://docs.oracle.com/javase/8/docs/api/index.html?java/util/StringTokenizer.html
        //https://blog.carsonseese.com/tutorial-java-command-line-calculator/
        switch (args.length) {
            case 1:
                StringTokenizer st = new StringTokenizer(args[0]);
                String[] arg = new String[st.countTokens()];
                for(int x = 0; x < arg.length; x++){
                    arg[x] = st.nextToken();
                }
            
                try{
                    validate(arg);
                   double result = checkPostfix(Postfix(arg));
                    if(result % 1 != 0){
                        System.out.println(result);
                    }
                    else{
                        System.out.println((int)result);
                    } 
                }
                catch(QuitMashingOnYourKeyboardException | AlgebraFailException | UserIsADumbassException | DivideByZeroException ioe){
                    System.out.println(ioe.getMessage());
                }
                break;
            case 0:
                System.out.println("Please input statement in quotation marks");
                break;
            default:
                System.out.println("Statement needs to be in quotation marks.");
                break;
        }
	}

     //Exceptions :)
        
        static class AlgebraFailException extends IllegalArgumentException{

		public AlgebraFailException(){
			System.out.println("A number is missing");
		}
	}
	static class QuitMashingOnYourKeyboardException extends IllegalArgumentException{

		public QuitMashingOnYourKeyboardException(){
			System.out.println("This operation is not supported");
		}
	}
	static class UserIsADumbassException extends IllegalArgumentException{

		public UserIsADumbassException(){
			System.out.println("Please enter a number. Thank you");
		}
	}
	static class DivideByZeroException extends ArithmeticException{

		public DivideByZeroException(){
			System.out.println("Divide by Zero ERROR");
		}
        }
    
        //Make sure that all requirments are met i.e proper syntax 
	public static void validate(String[] arg){
                bracketCheck(arg);
		spaceCheck(arg);
		statementCheck(arg);
	}
        
        //Checks to see if there is brackets 
        public static void bracketCheck(String[] arg){
		int open = 0;
		int close = 0;

        for (String arg1 : arg) {
            if (arg1.equals("(")) {
                open++;
            }
            if (arg1.equals(")")) {
                close++;
            }
        }

		if(open != close){
			System.out.println("Please enter brackets");
			System.exit(0);
		}
	}
        
        //Checks to see if there is space between each character entered
	public static void spaceCheck(String[] arg){

        for (String str : arg) {
            if(str.length() > 1){
                if(!checkNumber(str)){
                    System.out.println("Please seperate every character by space");
                    System.exit(0);
                }
            }
        }
	}
 
        //Checks to see if statement entered is an actual math statement
	 static void statementCheck(String[] arg) throws UserIsADumbassException, AlgebraFailException{
            int operators = 0;
            int operands = 0;
            boolean operand;

               if(checkNumber(arg[0])){
                    operand = true;
                    operands++;
		}
		else{
			operand = false;
			if(!arg[0].equals("(") && !arg[0].equals(")")){
				operators++;
			}
		}
		
		for(int j = 1; j < arg.length; j++){
			String str = arg[j];
			
		if(operand){
			 if(!checkNumber(str)){
                            if(!str.equals("(") && !str.equals(")")){
				operand = false;
				operators++;	
				}
				}
			else{
			throw new AlgebraFailException();
                           }
			}
			else{
                           if(!checkNumber(str)){
                               if(str.equals("(")){
                               } else {
                                   throw new AlgebraFailException();
                               }
                           }
                            else{
                               operands++;
                               operand = true;
                           }
                    }
		}
		
		if(operands - 1 != operators){
			throw new UserIsADumbassException();
		}
	}
         //Checks number
	 static boolean checkNumber(String str){
		try{
			int number = Integer.parseInt(str);

		}catch(NumberFormatException e){
			try{
				double number = Double.parseDouble(str);

			}catch(NumberFormatException eq){
				return false;
			}
		}
		return true;
	}
         //Operations order
	 static int order(String str){
		switch(str){
			case "+": return 1; 
                        case "-": return 1; 
			case "*": return 2; 
			case "/": return 2; 
			default: return -1;
		}
	}   
        
        
        //Implementing stack for Postfix
        //https://codereview.stackexchange.com/questions/35750/postfix-evaluation-using-a-stack
        //https://docs.oracle.com/javase/7/docs/api/java/util/Stack.html
	
         public static String Postfix(String[] arg){
		String pfix = "";
		Stack<String> stack = new Stack<>();

        for (String str : arg) {
            if(checkNumber(str)){
                pfix += str + " ";
            }
            else if(str.equals("(")){
                stack.push(str);
            }
            else if(str.equals(")")){
                
                while(!stack.isEmpty() && !stack.peek().equals("(")){
                    pfix += stack.pop() + " ";
                }
                stack.pop();
            }
            else{
                while(!stack.isEmpty() && order(str) <= order(stack.peek())){
                    pfix += stack.pop() + " ";
                }
                stack.push(str);
            }
        }
		while(!stack.isEmpty()){
			pfix += stack.pop() + " ";
		}
		return pfix;
	}
         //https://www.cs.bu.edu/~hwxi/academic/courses/CS112/Spring10/assignments/03/solution/Calculator.java
	
        public static double checkPostfix(String equ) throws DivideByZeroException, QuitMashingOnYourKeyboardException{
            Stack<Double> stack = new Stack<>();
            StringTokenizer tk = new StringTokenizer(equ);

             while(tk.hasMoreTokens()){
                   String token = tk.nextToken();
		if(checkNumber(token))
                {
		stack.push(Double.parseDouble(token));
                }
              else{
                  double num2 = stack.pop();
                  double num1 = stack.pop();
                   switch(token)
                     {
                      case "+": stack.push(num1 + num2); break;
                      case "-": stack.push(num1 - num2); break;
                      case "*": stack.push(num1 * num2); break;
                       case "/": 
                            if(num2 != 0){Double push = stack.push(num1 / num2);
}
                                        else{
                                            throw new DivideByZeroException();
                                            }
					break;
					default: throw new QuitMashingOnYourKeyboardException();
					}
                }
		}
		return stack.pop();
	}

	
}

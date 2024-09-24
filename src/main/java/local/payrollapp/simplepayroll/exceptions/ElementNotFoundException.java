package local.payrollapp.simplepayroll.exceptions;

public class ElementNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1;
	
	public ElementNotFoundException (String msg) {
		super(msg);
	}
}
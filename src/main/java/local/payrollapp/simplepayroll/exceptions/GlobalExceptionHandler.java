package local.payrollapp.simplepayroll.exceptions;

import java.util.Date;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@ExceptionHandler(ElementNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleElementNotFoundException(ElementNotFoundException ex, WebRequest wRequest, Model model){
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatus(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        
        model.addAttribute("status", errorObject.getStatus());
        model.addAttribute("message", errorObject.getMessage());
		
		return "error";
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoSuchElementException(NoSuchElementException ex, WebRequest wRequest, Model model) {
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatus(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        ex.printStackTrace();
        
        model.addAttribute("status", errorObject.getStatus());
        model.addAttribute("message", errorObject.getMessage());
        
		return "error";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String  handleIllegaArgumentException(IllegalArgumentException ex, WebRequest wRequest, Model model) {
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        ex.printStackTrace();
        
        model.addAttribute("status", errorObject.getStatus());
        model.addAttribute("message", errorObject.getMessage());
        
		return "error";
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleAllUncaughtException(Exception ex, WebRequest wRequest, Model model){
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage("An Internal Server Error Occurred. Contact Developer...");
        errorObject.setTimestamp(new Date());
        ex.printStackTrace();
        
        model.addAttribute("status", errorObject.getStatus());
        model.addAttribute("message", errorObject.getMessage());
		
		return "error";
	}
}

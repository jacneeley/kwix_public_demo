package local.payrollapp.simplepayroll.paystub;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;

import local.payrollapp.simplepayroll.SimplePayroll;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;

@Controller
public class PayrollController {
	
	private final PaystubSrv _paystubSrv;
	private static final Logger log = LoggerFactory.getLogger(PayrollController.class);
	
	public PayrollController(PaystubSrv paystubSrv) {
		this._paystubSrv = paystubSrv;
	}
	
	public List<PaystubResponse> getAllPaystubs(){
		List<Paystub> stubs = _paystubSrv.findAllActivePaystubs();
		List<PaystubResponse> result = new ArrayList<PaystubResponse>();
		for (var stub : stubs) {
			PaystubResponse response = getResponse(stub);
			result.add(response);
		}
		return result;
	}
	
	public List<PaystubResponse> getPaystubsForEmployee(String id) {
		List<Paystub> stubs = _paystubSrv.getPaystubsForEmployee(id);
		List<PaystubResponse> result = new ArrayList<PaystubResponse>();
		for (var stub : stubs) {
			PaystubResponse response = getResponse(stub);
			result.add(response);
		}
		return result;
	}
	
	public List<PaystubResponse> getDeletedPaystubs() {
		List<Paystub> stubs = _paystubSrv.getAllDeletedPaystubs();
		List<PaystubResponse> result = new ArrayList<PaystubResponse>();
		for (var stub : stubs) {
			PaystubResponse response = getResponse(stub);
			result.add(response);
		}
		return result;
	}
	
	public PaystubResponse getPaystub(String id) {
		Paystub stub = _paystubSrv.findByIdAndActive(id).orElseThrow(() -> new ElementNotFoundException(String.format("Paystub: %s could not be found...", id)));
		PaystubResponse response = getResponse(stub);
		return response;
	}
	
	public PaystubResponse createPaystub(PaystubRequest request) {
		Paystub newStub = new Paystub(
				"",
				request.getEmployeeId(),
				request.getFullName(),
				request.getJobsite(),
				request.getPay(),
				request.getHoursWorked(),
				Boolean.TRUE,
				this.convertToLocalDateViaInstant(request.getDayWorked()),
				LocalDate.now(),
				LocalDate.now());
		_paystubSrv.CreatePaystub(newStub);
		PaystubResponse response = getResponse(newStub);
		return response;
	}
	
	public PaystubResponse updatePaystub(String id, PaystubRequest request) {
		Paystub oldStub = _paystubSrv.findByIdAndActive(id).orElseThrow(() -> new ElementNotFoundException(String.format("Paystub: %s could not be found...", id)));
		Paystub updatedStub = new Paystub(
				oldStub.getId(),
				request.getEmployeeId(),
				request.getFullName(),
				request.getJobsite(),
				request.getPay(),
				request.getHoursWorked(),
				request.isActive(),
				this.convertToLocalDateViaInstant(request.getDayWorked()),
				oldStub.getCreateAt(),
				LocalDate.now());
		_paystubSrv.UpdatePaystub(updatedStub);
		PaystubResponse response = getResponse(updatedStub);
		return response;
	}
	
	public void deletePaystub(String id){
		try{
			_paystubSrv.DeletePaystub(id);
			} catch (ElementNotFoundException ex) {
				ex.printStackTrace();
			}
	}
	
	public void restorePaystub(String id) {
		Paystub stub = _paystubSrv.findByIdAndInactive(id).orElseThrow(() -> new ElementNotFoundException(String.format("Deleted Paystub: %s could not be found...", id)));
		Paystub restoreStub = new Paystub(
				stub.getId(),
				stub.getEmployeeId(),
				stub.getFullName(),
				stub.getJobsite(),
				stub.getPay(),
				stub.getHoursWorked(),
				Boolean.TRUE,
				stub.getDayWorked(),
				stub.getCreateAt(),
				LocalDate.now());
		_paystubSrv.UpdatePaystub(restoreStub);
	}

	private PaystubResponse getResponse(Paystub stub) {
		PaystubResponse stubResponse = new PaystubResponse(
				stub.getId(),
				stub.getEmployeeId(),
				stub.getFullName(),
				stub.getJobsite(),
				stub.getPay(),
				stub.getHoursWorked(),
				stub.isActive(),
				stub.getDayWorked(),
				stub.getCreateAt(),
				stub.getUpdateAt());
		return stubResponse;
	}
	
	public String formatedDate(LocalDate date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
		return date.format(df);
	}
	
	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public double roundMoney(double money) {
		DecimalFormat df = new DecimalFormat("#.###");
		String moneyStr = df.format(money);
		return Double.parseDouble(moneyStr);
	}
}
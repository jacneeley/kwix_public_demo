package local.payrollapp.simplepayroll.paystub;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ExtendedPaystubSrv {

	public double getAllHours(List<PaystubResponse> response) {
		double hours = 0;
		for(PaystubResponse stub : response) {
				hours += stub.hoursWorked();
		}
		return hours;
	}
	
	public double getAllPay(List<PaystubResponse> response) {
		double pay = 0;
		for(PaystubResponse stub : response) {
				pay += stub.pay();
		}
		return pay;
	}
	
	public double getTotalPay(double pay, double hours) {
		return pay * hours;
	}
}
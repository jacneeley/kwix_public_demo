package local.payrollapp.simplepayroll.paystub;

import org.springframework.stereotype.Repository;

@Repository
public class ExtendedPaystubRepo {
	public Integer setActiveValue(boolean isActive) {
		return ((isActive) ? 1 : 0);
	}
	
//	public Integer setJobsiteValue(Jobsite jobsite) {
//		return ;
//	}
}
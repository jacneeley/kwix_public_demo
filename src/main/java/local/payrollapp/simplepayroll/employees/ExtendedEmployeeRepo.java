package local.payrollapp.simplepayroll.employees;

import org.springframework.stereotype.Repository;

@Repository
public class ExtendedEmployeeRepo {
	public Integer setActiveValue(boolean isActive) {
		return ((isActive) ? 1 : 0);
	}
}

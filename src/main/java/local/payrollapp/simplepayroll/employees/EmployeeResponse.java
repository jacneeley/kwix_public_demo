package local.payrollapp.simplepayroll.employees;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public record EmployeeResponse(
		 String id,
		 String firstName,
		 String lastName,
		 String phone,
		 double pay,
		 boolean active,
		 LocalDate createAt,
		 LocalDate updateAt) {
	
	public String getFullName() {
		return String.format("%s %s",firstName, lastName);
	}
}
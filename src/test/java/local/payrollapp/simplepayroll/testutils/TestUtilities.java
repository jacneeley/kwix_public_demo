package local.payrollapp.simplepayroll.testutils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

import local.payrollapp.simplepayroll.employees.EmployeeResponse;

public class TestUtilities {
	public List<EmployeeResponse> filterByActiveEmployee(boolean active, List<EmployeeResponse> employees) {
		List<EmployeeResponse> employeesByActive = new ArrayList<>();
		employees.stream()
			.filter(emp -> emp.active() == active)
			.forEach(employeesByActive::add);
		return employeesByActive;
	}
}
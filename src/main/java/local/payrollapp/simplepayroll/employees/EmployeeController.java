package local.payrollapp.simplepayroll.employees;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;

import local.payrollapp.simplepayroll.SimplePayroll;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;

@Controller
public class EmployeeController {
	
	private final EmployeeSrv _empSrv;
	
	public EmployeeController (EmployeeSrv empSrv) {
		this._empSrv = empSrv;
	}
	
	public List<EmployeeResponse> getAllEmployees(){
		List<Employee> emps = _empSrv.getAllEmps();
		List<EmployeeResponse> result = new ArrayList<EmployeeResponse>();
		for (var emp : emps) {
			EmployeeResponse response = getResponse(emp);
			result.add(response);
		}
		return result;
	}
	
	public List<EmployeeResponse> getAllDeletedEmployees(){
		List<Employee> emps = _empSrv.getAllDeletedEmps();
		List<EmployeeResponse> result = new ArrayList<EmployeeResponse>();
		for (var emp : emps) {
			EmployeeResponse response = getResponse(emp);
			result.add(response);
		}
		return result;
	}
	
	public EmployeeResponse getEmployee(String id) {
		Employee emp = _empSrv.getEmp(id).orElseThrow(() -> new ElementNotFoundException(String.format("Employee: %s could not be found...", id)));
		EmployeeResponse response = getResponse(emp);
		return response;
	}
	
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		Employee newEmp = new Employee(
				request.getfirstName(),
				request.getlastName(),
				request.getPhone(),
				request.getPay(),
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		_empSrv.createEmp(newEmp);
		EmployeeResponse response = getResponse(newEmp);
		return response;
	}
	
	public EmployeeResponse updateEmployee(String id, EmployeeRequest request) {
		Employee oldEmp = _empSrv.getEmp(id).orElseThrow(() -> new ElementNotFoundException(String.format("Employee: %s could not be found...", id)));
		Employee updatedEmp = new Employee(
				oldEmp.getId(),
				request.getfirstName(),
				request.getlastName(),
				request.getPhone(),
				request.getPay(),
				request.isActive(),
				oldEmp.getCreateAt(),
				LocalDate.now());
		_empSrv.updateEmp(id, updatedEmp);
		EmployeeResponse response = getResponse(updatedEmp);
		return response;
	}
	
	public void restoreEmployee(String id) {
		Employee emp = _empSrv.getInactiveEmp(id).orElseThrow(() -> new ElementNotFoundException(String.format("Employee: %s could not be found...", id)));
		Employee restoredEmp = new Employee(
				emp.getId(),
				emp.getfirstName(),
				emp.getlastName(),
				emp.getPhone(),
				emp.getPay(),
				Boolean.TRUE,
				emp.getCreateAt(),
				emp.getUpdateAt());
		_empSrv.updateEmp(id, restoredEmp);
	}
	
	public void deleteEmployee(String id) {
		try {
			_empSrv.deleteEmp(id);
		} catch (ElementNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	private EmployeeResponse getResponse(Employee employee) {
		EmployeeResponse empResponse = new EmployeeResponse(
				employee.getId(),
				employee.getfirstName(),
				employee.getlastName(),
				employee.getPhone(),
				employee.getPay(),
				employee.isActive(),
				employee.getCreateAt(),
				employee.getUpdateAt());
		return empResponse;
	}
	
	public String formatedDate(LocalDate date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
		return date.format(df);
	}
	
//	public Employee FindByName(String fullName) {
//		return _empSrv.FindByName(fullName);
//	}
}
package local.payrollapp.simplepayroll.employees;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.testutils.TestUtilities;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objMapper;
	
	@InjectMocks
	TestUtilities testUtils;
	
	@MockBean
	EmployeeController empController;
	
	@MockBean
	EmployeeRepo empRepo;
	
	@MockBean
	EmployeeSrv empSrv;
	
	private final List<EmployeeResponse> employees = new ArrayList<>();	
	
	@SuppressWarnings("deprecation")
	@BeforeAll
	void prepare() {
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeEach
	void setup() {
		
		empSrv.createEmp(
				new Employee(
					"John",
					"Madden",
					"888-888-8888",
					100.00,
					true,
					LocalDate.now(),
					LocalDate.now())
				);
		employees.add(
				new EmployeeResponse(
					"JM8888",
					"John",
					"Madden",
					"888-888-8888",
					100.00,
					true,
					LocalDate.now(),
					LocalDate.now())
				);
		employees.add(
				new EmployeeResponse(
					"HM1111",
					"Hugh",
					"Mungus",
					"111-111-1111",
					10.00,
					true,
					LocalDate.now(),
					LocalDate.now())
				);
		employees.add(new EmployeeResponse(
					"MH2222",
					"Mungus",
					"Hugh",
					"222-222-2222",
					11.00,
					false,
					LocalDate.now(),
					LocalDate.now())
				);
	}
	
	@Test
	void shouldFindAllActiveEmps() throws Exception{
		List<EmployeeResponse> employeesByActive = testUtils.filterByActiveEmployee(true,employees);
		when(empController.getAllEmployees()).thenReturn(employeesByActive);
		Assertions.assertEquals(2, employeesByActive.size());
	}
	
	@Test
	void shouldFindAllInactiveEmps() throws Exception{
		List<EmployeeResponse> employeesByInactive = testUtils.filterByActiveEmployee(false,employees);
		when(empController.getAllDeletedEmployees()).thenReturn(employeesByInactive);
		Assertions.assertEquals(1, employeesByInactive.size());
	}
	
	@Test
	void shouldFindOneActiveEmployee() throws Exception{
		String id = "JM8888";
		EmployeeResponse employee = employees.get(0);
		when(empController.getEmployee(id)).thenReturn(employee);
		Assertions.assertEquals(id, employee.id());
	}
	
	@Test
	void shouldNotFindEmployeeWithInvalidId() {
		String id = ArgumentMatchers.anyString();
		ElementNotFoundException elementNotFound = Assertions.assertThrows(
				ElementNotFoundException.class,
				() -> empSrv.getEmp(id).orElseThrow(() -> new ElementNotFoundException(String.format("Employee: %s could not be found...", id))));
		Assertions.assertTrue(elementNotFound.getMessage().contains("could not be found..."));
	}
	
	@Test
	void shouldCreateNewEmployee() {
		EmployeeRequest request = new EmployeeRequest();
		request.setfirstName("Test");
		request.setlastName("Test");
		request.setPhone("123-123-1234");
		request.setPay(12.0);
		
		Employee newEmp = new Employee(
				"Test1234",
				request.getfirstName(),
				request.getlastName(),
				request.getPhone(),
				request.getPay(),
				true,
				LocalDate.now(),
				LocalDate.now());
		
		EmployeeResponse response = new EmployeeResponse(
				newEmp.getId(),
				newEmp.getfirstName(),
				newEmp.getlastName(),
				newEmp.getPhone(),
				newEmp.getPay(),
				newEmp.isActive(),
				newEmp.getCreateAt(),
				newEmp.getUpdateAt());
		when(empController.createEmployee(request)).thenReturn(response);
		Assertions.assertEquals(request.getPhone(), newEmp.getPhone());
		Assertions.assertEquals(request.getPhone(), response.phone());
		Assertions.assertEquals(newEmp.getId(), response.id());
	}
	
	@Test
	void shouldUpdateEmployee() {
		EmployeeRequest request = new EmployeeRequest();
		request.setfirstName("Test");
		request.setlastName("Test");
		request.setPhone("123-123-1234");
		request.setPay(12.0);
		
		Employee newEmp = new Employee(
				"Test1234",
				request.getfirstName(),
				request.getlastName(),
				request.getPhone(),
				request.getPay(),
				true,
				LocalDate.now(),
				LocalDate.now());
		
		
		request.setfirstName("Test2");
		request.setlastName("Test2");
		request.setPhone("123-123-1234");
		request.setPay(13.0);
		
		EmployeeResponse response = new EmployeeResponse(
				newEmp.getId(),
				request.getfirstName(),
				request.getlastName(),
				request.getPhone(),
				request.getPay(),
				newEmp.isActive(),
				newEmp.getCreateAt(),
				newEmp.getUpdateAt());
		when(empController.updateEmployee(newEmp.getId(), request)).thenReturn(response);
		Assertions.assertEquals(request.getfirstName() + " " + request.getlastName(), response.getFullName());
		Assertions.assertEquals(request.getPhone(), response.phone());
		Assertions.assertTrue(Double.valueOf(request.getPay()).equals(Double.valueOf(response.pay())));
		Assertions.assertEquals(newEmp.getId(), response.id());
		Assertions.assertEquals(newEmp.getCreateAt(), response.createAt());
		Assertions.assertEquals(newEmp.getUpdateAt(), response.updateAt());
		
	}
}

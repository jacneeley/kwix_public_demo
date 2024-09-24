package local.payrollapp.simplepayroll.view;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.employees.EmployeeController;
import local.payrollapp.simplepayroll.employees.EmployeeRepo;
import local.payrollapp.simplepayroll.employees.EmployeeRequest;
import local.payrollapp.simplepayroll.employees.EmployeeResponse;
import local.payrollapp.simplepayroll.employees.EmployeeSrv;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.paystub.ExtendedPaystubSrv;
import local.payrollapp.simplepayroll.paystub.PayrollController;
import local.payrollapp.simplepayroll.paystub.Paystub;
import local.payrollapp.simplepayroll.testutils.TestUtilities;
import local.payrollapp.simplepayroll.utility.CsvWriter;

@WebMvcTest(ViewController.class)
public class ViewControllerTest {
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objMapper;
	
	@InjectMocks
	TestUtilities testUtils;
	
	@MockBean
	EmployeeController empController;
	
	@MockBean
	EmployeeSrv empSrv;
	
	@MockBean
	EmployeeRepo empRepo;
	
	@MockBean
	ExtendedPaystubSrv extPaystubSrv;
	
	@MockBean
	CsvWriter _writer;
	
	@MockBean
	PayrollController payrollController;
	
	private final List<EmployeeResponse> employees = new ArrayList<>();
	private final List<Paystub> paystubs = new ArrayList<>();
	
	@BeforeEach
	void setUp() {
		employees.add(new EmployeeResponse(
				"JM8888",
				"John",
				"Madden",
				"888-888-8888",
				100.00,
				true,
				LocalDate.now(),
				LocalDate.now())
			);
		employees.add(new EmployeeResponse(
				"HM1111",
				"Hugh",
				"Mungus",
				"111-111-1111",
				100.00,
				false,
				LocalDate.now(),
				LocalDate.now())
			);
		paystubs.add(new Paystub(
				UUID.randomUUID().toString(),
				"JM8888",
				"John Madden",
				"Jobsite1",
				12.0,
				12.0,
				true,
				LocalDate.now(),
				LocalDate.now(),
				LocalDate.now()));
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	void shouldFindAllActiveEmployees() throws Exception {
		List<EmployeeResponse> response = testUtils.filterByActiveEmployee(true, employees);
		when(empController.getAllEmployees()).thenReturn(response);
		MvcResult result = mvc.perform(get("/employees"))
			.andExpect(status().isOk())
			.andReturn();
		String pageResponse = result.getResponse().getContentAsString(); 
		List<EmployeeResponse> pageObjects = (List<EmployeeResponse>) result.getModelAndView().getModel().get("employees");
		EmployeeResponse pageObject = pageObjects.get(0);
		Assertions.assertEquals(response.get(0).id(), pageObject.id());
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.id())));
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.getFullName())));
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.phone())));
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.pay())));
		Assertions.assertEquals(response.get(0).active(),pageObject.active());
		Assertions.assertTrue(pageObject.active());
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.createAt())));
		Assertions.assertTrue(pageResponse.contains(String.format("<td>%s</td>", pageObject.updateAt())));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void shouldFindOneActiveEmployeeById() throws Exception {
		EmployeeResponse response = testUtils.filterByActiveEmployee(true, employees).get(0);
		String id = response.id();
		when(empController.getEmployee(id)).thenReturn(response);
		MvcResult result = mvc.perform(get("/employee/" + id))
			.andExpect(status().isOk())
			.andReturn();
		String pageResponse = result.getResponse().getContentAsString();
		EmployeeResponse pageObject = (EmployeeResponse) result.getModelAndView().getModel().get("employee");
		Assertions.assertTrue(pageResponse.contains(pageObject.id()));
		Assertions.assertTrue(pageResponse.contains(pageObject.firstName()));
		Assertions.assertTrue(pageResponse.contains(pageObject.lastName()));
		Assertions.assertTrue(pageResponse.contains(pageObject.phone()));
		Assertions.assertTrue(pageResponse.contains(Double.valueOf(pageObject.pay()).toString()));
		Assertions.assertEquals(response.active(),pageObject.active());
		Assertions.assertTrue(pageResponse.contains(Boolean.valueOf(pageObject.active()).toString()));
		Assertions.assertEquals(response.createAt(),pageObject.createAt());
		Assertions.assertEquals(response.updateAt(),pageObject.updateAt());
	}
	
	@Test
	void shouldNotFindActiveEmployeeById() throws Exception {
		when(empController.getEmployee("TT9999")).thenThrow(new ElementNotFoundException("Employee TT9999 could not be found..."));
		mvc.perform(get("/employee/TT9999"))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	void shouldCreateEmployee() throws Exception {
		var emp = employees.get(0);
		EmployeeRequest request = new EmployeeRequest();
		request.setfirstName("John");
		request.setlastName("Madden");
		request.setPhone("888-888-8888");
		request.setPay(100.0);
		request.setActive(true);
		when(empController.createEmployee(request)).thenReturn(emp);
		MvcResult result = mvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(objMapper.writeValueAsString(request))
				)
		.andExpect(status().isCreated())
		.andExpect(status().is3xxRedirection())
		.andReturn();
		String pageResponse = result.getResponse().getContentAsString();
		System.out.println(pageResponse);
	}
	
//	@Test
//	void shouldUpdateEmployee() throws Exception {
//		String id = "JM8888";
//		EmployeeRequest request = new EmployeeRequest();
//		request.setfirstName("John");
//		request.setlastName("Madden2");
//		request.setPhone("888-888-8888");
//		request.setPay(101.0);
//		EmployeeResponse updatedEmp = new EmployeeResponse(
//					id,
//					request.getfirstName(),
//					request.getlastName(),
//					request.getPhone(),
//					request.getPay(),
//					true,
//					LocalDate.now(),
//					LocalDate.now()
//				);
//		when(empController.updateEmployee(id, request)).thenReturn(updatedEmp);
//	}
}

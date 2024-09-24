package local.payrollapp.simplepayroll.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import local.payrollapp.simplepayroll.employees.EmployeeResponse;
import local.payrollapp.simplepayroll.view.ViewController;

@SpringBootTest(classes = ViewController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ViewControllerIntegratedTest {
	@LocalServerPort
	int randomServerPort;
	
	RestClient client;
	
	@BeforeEach
	void setUp() {
		client = RestClient.create("http://localhost:" + randomServerPort);
	}
	
	@Test
	void shouldFindAllEmployees() {
		List<EmployeeResponse> employees = client.get()
				.uri("/employees")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		assertEquals(2, employees.size());
	}
}
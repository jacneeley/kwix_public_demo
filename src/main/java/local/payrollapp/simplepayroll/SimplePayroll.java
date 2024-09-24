package local.payrollapp.simplepayroll;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;

import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.employees.EmployeeRepo;
import local.payrollapp.simplepayroll.employees.EmployeeSrv;
import local.payrollapp.simplepayroll.employees.ExtendedEmployeeRepo;

@SpringBootApplication
public class SimplePayroll {
	
	private static final Logger log = LoggerFactory.getLogger(SimplePayroll.class);
	private final EmployeeRepo _empRepo;
	
	SimplePayroll(EmployeeRepo empRepo){
		this._empRepo = empRepo;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SimplePayroll.class, args);
		log.info("App Startup Successful!");
	}
	
//	@Bean
//	CommandLineRunner runline() {//test employee object
//		EmployeeSrv empSrv = new EmployeeSrv(_empRepo, null);
//		return args -> {
//			Employee emp = new Employee("Arthur", "Sparks", "999-999-9999", 99.99, true, LocalDate.now(), LocalDate.now());
//			Employee emp2 = new Employee("Jacob", "Neeley", "888-888-8888", 99.99, false, LocalDate.now(), LocalDate.now());
//			empSrv.createEmp(emp);
//			empSrv.createEmp(emp2);
//			log.info("Employee: " + emp.toString());
//		};
//	}
}

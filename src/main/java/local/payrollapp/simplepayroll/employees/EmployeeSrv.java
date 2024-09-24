package local.payrollapp.simplepayroll.employees;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import local.payrollapp.simplepayroll.paystub.Paystub;
import local.payrollapp.simplepayroll.paystub.PaystubSrv;
import local.payrollapp.simplepayroll.view.ViewController;

@Service
public class EmployeeSrv implements IEmpSrv{
	
	private final EmployeeRepo _empRepo;
	private final PaystubSrv _stubSrv;
	private static final Logger log = LoggerFactory.getLogger(EmployeeSrv.class);
	
	public EmployeeSrv(EmployeeRepo empRepo, PaystubSrv stubSrv) {
		this._empRepo = empRepo;
		this._stubSrv = stubSrv;
	}
	
	@Override
	public List<Employee> getAllEmps() {
//		List<Employee> emps = new ArrayList<Employee>(_employees.values());
//		return emps;
		return _empRepo.findAllByActive(true);
	}
	
	@Override
	public Optional<Employee> getEmp(String id) {
		return _empRepo.findByIdAndActive(id, true);
	}
	
	@Override
	public Optional<Employee> getInactiveEmp(String id){
		return _empRepo.findByIdAndActive(id, false);
	}
	
	@Override
	public List<Employee> getAllDeletedEmps() {
		return _empRepo.findAllByActive(false);
	}

	@Override
	public void createEmp(Employee employee) {
		Employee emp = new Employee(
				employee.generateId(),
				employee.getfirstName(),
				employee.getlastName(),
				employee.getPhone(),
				employee.getPay(),
				employee.isActive(),
				employee.getCreateAt(),
				employee.getUpdateAt());
		_empRepo.CreateEmployee(emp);
	}

	@Override
	public void updateEmp(String oldId, Employee employee){
		try {
			String oldPhone = _empRepo.findById(employee.getId()).get().getPhone();
			
			
			if(employee.getPhone().equals(oldPhone)) {
				_empRepo.UpdateEmployee(employee, employee.getId());
			}
			else if(!employee.getPhone().equals(oldPhone)) {
				
				//if phone is changed than a new id must be created.
				this.createEmp(employee);
				this.deleteEmp(employee.getId());
				
				List<Paystub> empStubs = _stubSrv.findAllEmployeePaystubs(oldId);
				if(!empStubs.isEmpty()) {
					for(Paystub stub : empStubs) {
						Paystub updatedStub = new Paystub(
								stub.getId(),
								employee.generateId(),
								stub.getFullName(),
								stub.getJobsite(),
								stub.getPay(),
								stub.getHoursWorked(),
								stub.isActive(),
								stub.getDayWorked(),
								stub.getCreateAt(),
								stub.getUpdateAt());
						_stubSrv.UpdatePaystubs(updatedStub);
					}
				}
			}
		} 
		catch(NoSuchElementException ex) {}
	}

	@Override
	public void deleteEmp(String id) {
		_empRepo.DeleteEmployee(id);
	}
	
	@PostConstruct 
	private void init() {
		Employee emp = new Employee("John", "Smith", "999-999-9999", 99.99, true, LocalDate.now(), LocalDate.now());
		Employee emp2 = new Employee("Adam", "Smith", "888-888-8888", 99.99, false, LocalDate.now(), LocalDate.now());
		this.createEmp(emp);
		log.info("created: " + emp.toString());
		this.createEmp(emp2);
		log.info("created: " + emp2.toString());
	}
}
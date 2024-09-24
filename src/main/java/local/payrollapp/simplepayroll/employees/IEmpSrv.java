package local.payrollapp.simplepayroll.employees;

import java.util.List;
import java.util.Optional;

public interface IEmpSrv {
	List<Employee>getAllEmps();
	List<Employee>getAllDeletedEmps();
	Optional<Employee> getEmp(String id);
	Optional<Employee> getInactiveEmp(String id);
	void createEmp(Employee employee);
	void updateEmp(String oldId, Employee employee);
	void deleteEmp(String id);
}

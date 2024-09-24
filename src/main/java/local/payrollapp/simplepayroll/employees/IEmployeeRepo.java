package local.payrollapp.simplepayroll.employees;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface IEmployeeRepo{
	Optional<Employee> findById(String id);
	List<Employee> findAllByActive(boolean active);
	Optional<Employee> findByIdAndActive(String id, boolean active);
	void CreateEmployee(Employee employee);
	void UpdateEmployee(Employee employee, String id);
	void DeleteEmployee(String id);
}

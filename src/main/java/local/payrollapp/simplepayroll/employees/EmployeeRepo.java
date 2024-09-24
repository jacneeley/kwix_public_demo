package local.payrollapp.simplepayroll.employees;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class EmployeeRepo implements IEmployeeRepo {
	
	private final JdbcClient _jdbcClient;
	private final ExtendedEmployeeRepo _extEmpRepo;
	
	public EmployeeRepo(JdbcClient jdbcClient, ExtendedEmployeeRepo extEmpRepo) {
		this._jdbcClient = jdbcClient;
		this._extEmpRepo = extEmpRepo;
	}
	
	@Override
	public Optional<Employee> findById(String id) {
		return _jdbcClient.sql("SELECT * FROM EMPLOYEE WHERE id = ?")
				.param(id)
				.query(Employee.class)
				.optional();
	}
	
	@Override
	public List<Employee> findAllByActive(boolean active) {
		Integer activeValue = null;
		if (active) {
			activeValue = 1;
		}
		else if (!active) {
			activeValue = 0;
		}
		List<Employee> allActive = _jdbcClient.sql("SELECT * FROM EMPLOYEE WHERE active = :Active")
				.param("Active", activeValue)
				.query(Employee.class).list();
		return allActive;
	}

	@Override
	public Optional<Employee> findByIdAndActive(String id, boolean active) {
		Integer activeValue = null;
		if (active) {
			activeValue = 1;
		}
		else if (!active) {
			activeValue = 0;
		}
		return _jdbcClient.sql("SELECT * FROM EMPLOYEE WHERE active = ? AND id = ?")
				.params(activeValue, id)
				.query(Employee.class)
				.optional();
	}

	@Override
	public void CreateEmployee(Employee employee) {
		var created = _jdbcClient.sql("INSERT INTO EMPLOYEE("
				+ "id,"
				+ "first_name,"
				+ "last_name,"
				+ "phone,"
				+ "pay,"
				+ "active,"
				+ "create_at,"
				+ "update_at"
				+ ")"
				+ "values(?,?,?,?,?,?,?,?)")
				.params(List.of(employee.getId() ,employee.getfirstName(), employee.getlastName(),
						employee.getPhone(), employee.getPay(), this.setActiveValue(employee.isActive()), employee.getCreateAt(), employee.getUpdateAt()))
				.update();
		Assert.state(created == 1, "Failed to create: " + employee.toString());
	}

	@Override
	public void UpdateEmployee(Employee employee, String id) {
		var updated = _jdbcClient.sql("UPDATE EMPLOYEE SET "
				+ "first_name=?, "
				+ "last_name=?, "
				+ "phone=?, "
				+ "pay=?, "
				+ "active=?, "
				+ "create_at=?, "
				+ "update_at=? "
				+ "WHERE id=?")
				.params(List.of(employee.getfirstName(), employee.getlastName(), employee.getPhone(),
						employee.getPay(), this.setActiveValue(employee.isActive()), employee.getCreateAt(), employee.getUpdateAt(), id))
				.update();
		Assert.state(updated == 1, "Failed to update: " + employee.toString());
	}

	@Override
	public void DeleteEmployee(String id) {
		var deleted = _jdbcClient.sql("DELETE FROM EMPLOYEE WHERE id = :Id")
				.param("Id", id)
				.update();
		Assert.state(deleted == 1, "Failed to Delete @ employee id: " + id);
	}
	
	private Integer setActiveValue(boolean isActive) {
		return _extEmpRepo.setActiveValue(isActive);
	}
}

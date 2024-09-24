package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class PaystubRepo implements IPaystubRepo{
	
	private final JdbcClient _jdbcClient;
	private final ExtendedPaystubRepo _extPaystubRepo;
	
	public PaystubRepo (JdbcClient jdbcClient, ExtendedPaystubRepo extPaystubRepo) {
		this._jdbcClient = jdbcClient;
		this._extPaystubRepo = extPaystubRepo;
	}
	
	@Override
	public List<Paystub> findEmployeePaystubs(String id) {
		return _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE employee_id = ?")
		.param(id)
		.query(Paystub.class).list();
	}
	
	@Override
	public List<Paystub> findEmployeePaystubsByActive(String id, boolean active) {
		Integer activeValue = null;
		if (active) {
			activeValue = 1;
		}
		else if (!active) {
			activeValue = 0;
		}
		List<Paystub> allActive = _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ? AND employee_id = ?")
				.params(activeValue, id)
				.query(Paystub.class).list();
		return allActive;
	}
	
	@Override
	public List<Paystub> findAllPaystubsByActive(boolean active) {
		Integer activeValue = null;
		if (active) {
			activeValue = 1;
		}
		else if (!active) {
			activeValue = 0;
		}
		List<Paystub> allActive = _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ?")
				.params(activeValue)
				.query(Paystub.class).list();
		return allActive;
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id, boolean active) {
		Integer activeValue = null;
		if (active) {
			activeValue = 1;
		}
		else if (!active) {
			activeValue = 0;
		}
		return _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ? AND id = ?")
				.params(activeValue, id)
				.query(Paystub.class)
				.optional();
	}

	@Override
	public void CreatePaystub(Paystub paystub) {
		var created = _jdbcClient.sql("INSERT INTO PAYSTUB("
				+ "id,"
				+ "employee_id,"
				+ "full_name,"
				+ "jobsite,"
				+ "pay,"
				+ "hours_worked,"
				+ "active,"
				+ "day_worked,"
				+ "create_at,"
				+ "update_at)"
				+ "values(?,?,?,?,?,?,?,?,?,?)")
				.params(List.of(paystub.getId(), paystub.getEmployeeId(), paystub.getFullName(), paystub.getJobsite(),
						paystub.getPay(), paystub.getHoursWorked(), this.setActiveValue(paystub.isActive()), 
						paystub.getDayWorked(), paystub.getCreateAt(), paystub.getUpdateAt()))
				.update();
		Assert.state(created == 1, "Failed to create: " + paystub.toString());
	}

	@Override
	public void UpdatePaystub(Paystub paystub, String id) {
		var updated = _jdbcClient.sql("UPDATE PAYSTUB SET "
				+ "full_name=?,"
				+ "jobsite=?,"
				+ "pay=?,"
				+ "hours_worked=?,"
				+ "active=?,"
				+ "day_worked=?,"
				+ "create_at=?,"
				+ "update_at=?"
				+ " WHERE id=? AND employee_id=?")
				.params(List.of(paystub.getFullName(), paystub.getJobsite(), paystub.getPay(),paystub.getHoursWorked(),
						this.setActiveValue(paystub.isActive()), paystub.getDayWorked(), paystub.getCreateAt(), paystub.getUpdateAt(),
						id, paystub.getEmployeeId()))
				.update();
		Assert.state(updated == 1, "Failed to update: " + paystub.toString());
	}


	@Override
	public void UpdatePaystubs(Paystub paystub, String id) {
		var updated = _jdbcClient.sql("UPDATE PAYSTUB SET "
				+ "employee_id=?,"
				+ "full_name=?,"
				+ "jobsite=?,"
				+ "pay=?,"
				+ "hours_worked=?,"
				+ "active=?,"
				+ "day_worked=?,"
				+ "create_at=?,"
				+ "update_at=?"
				+ " WHERE id=?")
				.params(List.of(paystub.getEmployeeId(), paystub.getFullName(), paystub.getJobsite(), paystub.getPay(),paystub.getHoursWorked(),
						this.setActiveValue(paystub.isActive()), paystub.getDayWorked(), paystub.getCreateAt(), paystub.getUpdateAt(),
						id))
				.update();
		Assert.state(updated == 1, "Failed to update: " + paystub.toString());
		
	}

	@Override
	public void DeletePaystub(String id) {
		var deleted = _jdbcClient.sql("DELETE FROM PAYSTUB WHERE id = :Id")
				.param("Id", id)
				.update();
		Assert.state(deleted == 1, "Failed to Delete @ employee id: " + id);
	}
	
	private Integer setActiveValue(boolean isActive) {
		return _extPaystubRepo.setActiveValue(isActive);
	}
}

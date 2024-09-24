package local.payrollapp.simplepayroll.paystub;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Paystub {
	@Id
	private String id;
	private String employeeId;
	private String fullName;
	private String jobsite;
	private double pay;
	private double hoursWorked;
	private boolean active;
	private LocalDate dayWorked;
	private LocalDate createAt;
	private LocalDate updateAt;
	
	public String getId() {
		return id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getJobsite() {
		return jobsite;
	}

	public double getPay() {
		return pay;
	}

	public double getHoursWorked() {
		return hoursWorked;
	}

	public boolean isActive() {
		return active;
	}

	public LocalDate getDayWorked() {
		return dayWorked;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public LocalDate getUpdateAt() {
		return updateAt;
	}

	public Paystub(String id, String employeeId, String fullName, String jobsite, double pay, double hoursWorked,
			boolean active, LocalDate dayWorked, LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.employeeId = employeeId;
		this.fullName = fullName;
		this.jobsite = jobsite;
		this.pay = pay;
		this.hoursWorked = hoursWorked;
		this.active = active;
		this.dayWorked = dayWorked;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
	public String generateId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "Paystub [id=" + id + ", employeeId=" + employeeId + ", fullName=" + fullName + ", jobsite=" + jobsite
				+ ", pay=" + pay + ", hours=" + hoursWorked + ", day=" + dayWorked + ", active=" + active + ", createAt=" + createAt
				+ ", updateAt=" + updateAt + "]";
	}
}

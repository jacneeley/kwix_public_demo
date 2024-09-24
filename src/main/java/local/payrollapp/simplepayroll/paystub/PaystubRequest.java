package local.payrollapp.simplepayroll.paystub;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class PaystubRequest {
	private String employeeId;
	private String fullName;
	private String jobsite;
	
	@Positive(message="Pay must be greater than ZERO.")
	private double pay;
	
	@Positive(message="Hours Worked must be greater than ZERO.")
	private double hoursWorked;
	
	private boolean active;
	
	//@Pattern(regexp="[0-9]{2}/[0-9]{2}/[0-9]{4}\n", message="Enter a valid date.")
	private Date dayWorked;
	
	private LocalDate createAt;
	private LocalDate updateAt;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getJobsite() {
		return jobsite;
	}
	public void setJobsite(String jobsite) {
		this.jobsite = jobsite;
	}
	public double getPay() {
		return pay;
	}
	public void setPay(double pay) {
		this.pay = pay;
	}
	public double getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getDayWorked() {
		return dayWorked;
	}
	public void setDayWorked(Date dayWorked) {
		this.dayWorked = dayWorked;
	}
}

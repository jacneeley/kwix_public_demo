package local.payrollapp.simplepayroll.employees;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;

@Entity
public class Employee {
	@jakarta.persistence.Id
	private String id;
	private String firstName;
	private String lastName;
	private String phone;
	private double pay;
	boolean active;
	private LocalDate createAt;
	private LocalDate updateAt;
	
	public String getId() {
		return id;
	}
	public String getfirstName() {
		return firstName;
	}
	public String getlastName() {
		return lastName;
	}
	public String getPhone() {
		return phone;
	}
	public double getPay() {
		return pay;
	}
	public boolean isActive() {
		return active;
	}
	public LocalDate getCreateAt() {
		return createAt;
	}
	public LocalDate getUpdateAt() {
		return updateAt;
	}
	
	public Employee() {}
	
	public Employee(String firstName, String lastName, String phone, double pay, boolean active,
			LocalDate createAt, LocalDate updateAt) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.pay = pay;
		this.active = active;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
	public Employee(String id, String firstName, String lastName, String phone, double pay, boolean active,
			LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.pay = pay;
		this.active = active;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, active, createAt, firstName, lastName, pay, phone, updateAt);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id) && active == other.active && Objects.equals(createAt, other.createAt)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Double.doubleToLongBits(pay) == Double.doubleToLongBits(other.pay)
				&& Objects.equals(phone, other.phone) && Objects.equals(updateAt, other.updateAt);
	}
	
	@Override
	public String toString() {
		return "employees [Id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", pay=" + pay
				+ ", active=" + active + ", createAt=" + createAt + ", updateAt=" + updateAt + "]";
	}
	
	String generateId() {
		//Random rand = new Random();
		String first = String.valueOf(firstName.charAt(0));
		String last = String.valueOf(lastName.charAt(0));
		String last4 = phone.split("-")[2];
		//int randInt = rand.nextInt(1000, 10000);
		
		return first + last + last4;
	}
	
	public String getFullName() {
		return String.format("%s %s",firstName, lastName);
	}
}

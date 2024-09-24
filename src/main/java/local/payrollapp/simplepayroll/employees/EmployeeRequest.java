package local.payrollapp.simplepayroll.employees;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EmployeeRequest{
	 private String id;
	 
	 @NotEmpty(message = "First Name cannot be empty.")
	 @Size(min=1, max=100, message="First name must be between 1 and 100 characters")
	 @Pattern(regexp="^[a-zA-Z]+$", message="First Name must only contain letters.")
	 private String firstName;
	 
	 @NotEmpty(message = "Last Name cannot be empty.")
	 @Size(min=1, max=100, message="Last name must be between 1 and 100 characters")
	 @Pattern(regexp="^[a-zA-Z]+$", message="Last Name must only contain letters.")
	 private String lastName;
	 
	 @NotEmpty(message = "Phone cannot be empty.")
	 @Pattern(regexp="^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message="Enter a valid phone number.")
	 private String phone;
	 
	 @Positive(message="Pay must be greater than ZERO.")
	 private double pay;
	 
	 private boolean active;
	 private LocalDate createAt;
	 private LocalDate updateAt;
	 
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

		public void setfirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setlastName(String lastName) {
			this.lastName = lastName;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public void setPay(double pay) {
			this.pay = pay;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}
}
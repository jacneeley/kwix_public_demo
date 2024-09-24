package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

public interface IPaystubRepo {
	List<Paystub> findEmployeePaystubs(String id);
	List<Paystub> findEmployeePaystubsByActive(String id, boolean active);
	List<Paystub> findAllPaystubsByActive(boolean active);
	Optional<Paystub> findByIdAndActive(String Id, boolean active);
	void CreatePaystub(Paystub paystub);
	void UpdatePaystub(Paystub paystub, String id);
	void UpdatePaystubs(Paystub paystub, String id);
	void DeletePaystub(String id);
}
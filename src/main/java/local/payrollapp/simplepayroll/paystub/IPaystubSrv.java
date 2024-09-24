package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

public interface IPaystubSrv {
	List<Paystub> getPaystubsForEmployee(String id);
	Optional<Paystub> findByIdAndActive(String id);
	Optional<Paystub> findByIdAndInactive(String id);
	void CreatePaystub(Paystub paystub);
	void UpdatePaystub(Paystub paystub);
	void UpdatePaystubs(Paystub paystub);
	void DeletePaystub(String id);
}

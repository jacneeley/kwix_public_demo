package local.payrollapp.simplepayroll.paystub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PaystubSrv implements IPaystubSrv{
	
	private final PaystubRepo _stubRepo;
	private static HashMap<String, Paystub> _paystubs = new HashMap<String, Paystub>(); 
	
	PaystubSrv(PaystubRepo stubRepo){
		this._stubRepo = stubRepo;
	}
	
	public List<Paystub> findAllEmployeePaystubs(String id){
		return _stubRepo.findEmployeePaystubs(id);
	}
	
	public List<Paystub> findAllActivePaystubs() {
		return _stubRepo.findAllPaystubsByActive(true);
	}
	
	@Override
	public List<Paystub> getPaystubsForEmployee(String id) {
//		List<Paystub> paystubs = new ArrayList<Paystub>(_paystubs.values());
//		return paystubs;
		return _stubRepo.findEmployeePaystubsByActive(id, true);
	}
	
	public List<Paystub> getAllDeletedPaystubs() {
		return _stubRepo.findAllPaystubsByActive(false);
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id) {
		return _stubRepo.findByIdAndActive(id, true);
	}
	
	public Optional<Paystub> findByIdAndInactive(String id) {
//		return _paystubs.get(id);
		return _stubRepo.findByIdAndActive(id, false);
	}

	@Override
	public void CreatePaystub(Paystub paystub) {
//		_paystubs.put(paystub.getId(), paystub);
		Paystub stub = new Paystub(
				paystub.generateId(),
				paystub.getEmployeeId(),
				paystub.getFullName(),
				paystub.getJobsite(),
				paystub.getPay(),
				paystub.getHoursWorked(),
				paystub.isActive(),
				paystub.getDayWorked(),
				paystub.getCreateAt(),
				paystub.getUpdateAt());
		_stubRepo.CreatePaystub(stub);
	}

	@Override
	public void UpdatePaystub(Paystub paystub) {
		_stubRepo.UpdatePaystub(paystub, paystub.getId());
	}
	
	@Override
	public void UpdatePaystubs(Paystub paystub) {//update paystubs in bulk to maintain history if employee id changes.
		_stubRepo.UpdatePaystubs(paystub, paystub.getId());
	}

	@Override
	public void DeletePaystub(String id) {
		_stubRepo.DeletePaystub(id);
	}
}
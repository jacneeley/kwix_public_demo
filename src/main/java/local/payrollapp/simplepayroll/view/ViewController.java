package local.payrollapp.simplepayroll.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.Valid;
import local.payrollapp.simplepayroll.AppConstants;
import local.payrollapp.simplepayroll.employees.EmployeeController;
import local.payrollapp.simplepayroll.employees.EmployeeRequest;
import local.payrollapp.simplepayroll.employees.EmployeeResponse;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.paystub.ExtendedPaystubSrv;
import local.payrollapp.simplepayroll.paystub.Jobsite;
import local.payrollapp.simplepayroll.paystub.PayrollController;
import local.payrollapp.simplepayroll.paystub.PaystubRequest;
import local.payrollapp.simplepayroll.paystub.PaystubResponse;
import local.payrollapp.simplepayroll.utility.CsvWriter;
import local.payrollapp.simplepayroll.utility.DeleteFiles;

@Controller
@RequestMapping("/")
public class ViewController {
	
	private final EmployeeController _empCtrlr;
	private final PayrollController _payrollCtrlr;
	private final ExtendedPaystubSrv _extStubSrv;
	private final CsvWriter _writer;
	private final AppConstants _const;
	private static final Logger log = LoggerFactory.getLogger(ViewController.class);
	
	ViewController(EmployeeController empCtrlr, PayrollController payrollCtrlr,
			ExtendedPaystubSrv extStubSrv, CsvWriter writer, AppConstants consts){
		this._empCtrlr = empCtrlr;
		this._payrollCtrlr = payrollCtrlr;
		this._extStubSrv = extStubSrv;
		this._writer = writer;
		this._const = consts;
	}
	
	@GetMapping("/")
	public String viewIndex(Model model) {
		model.addAttribute("ver",_const.getVer());
		return "index";
	}
	
	/* employees */
	@GetMapping("/employees")
	public String getEmployees(Model model) {
		List<EmployeeResponse> emps = _empCtrlr.getAllEmployees();
		EmployeeRequest emp = new EmployeeRequest();
		model.addAttribute("employees",emps);
		model.addAttribute("employee",emp);
		model.addAttribute("ver",_const.getVer());
		return "employees";
	}
	
	@GetMapping("/employee/{id}")
	public String getEmployee(Model model, @PathVariable("id") String id) throws ElementNotFoundException {
		EmployeeResponse emp = _empCtrlr.getEmployee(id);
		model.addAttribute("employee", emp);
		model.addAttribute("ver",_const.getVer());
		return "employee";
	}
	
	@PostMapping("/employees")
	public RedirectView createEmployee(@Valid @ModelAttribute("employee") EmployeeRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		String msg = "";
		boolean errorsPresent = result.hasErrors();
		if(errorsPresent) {
			String errList = HumanReadibleErrors(result.getAllErrors());
			log.warn("Issue(s): " + errList);
			msg = String.format("Failed to add %s %s. Reason: BAD REQUEST - %s",request.getfirstName(), request.getlastName(), errList);
		}
		else {
			EmployeeResponse newEmp = _empCtrlr.createEmployee(request);
			msg = "Employee: " + newEmp.getFullName() + " was added.";
		}
		RedirectView redirectView = new RedirectView("/employees", true);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@PostMapping("/employee/{id}")
	public RedirectView updateEmployee(@PathVariable("id") String id, @Valid @ModelAttribute("employee") EmployeeRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		String msg = "";
		String redirect = "";
		boolean errorsPresent = result.hasErrors();
		if(errorsPresent) {
			String errList = HumanReadibleErrors(result.getAllErrors());
			log.warn("Issue(s): " + errList);
			msg = String.format("Failed to update. Reason: BAD REQUEST - %s", errList);
			redirect = "/employee/" + id;
		}
		else {
			EmployeeResponse updatedEmp = _empCtrlr.updateEmployee(id, request);
			msg = ((!updatedEmp.active()) ? "Employee was deleted successfully." : "Employee was updated successfully.");
			redirect = "/employees";
		}
		RedirectView redirectView = new RedirectView(redirect, true);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@GetMapping("/deletedemployees")
	public String getDeletedEmployees(Model model) {
		List<EmployeeResponse> emps = _empCtrlr.getAllDeletedEmployees();
		//EmployeeRequest emp = new EmployeeRequest();
		model.addAttribute("employees",emps);
		model.addAttribute("ver",_const.getVer());
		return "deletedemployees";
	}
	
	@GetMapping("/restoreemployee/{id}")
	public RedirectView restoreEmployee(RedirectAttributes redirectAttributes, @PathVariable("id") String id) throws ElementNotFoundException {
		_empCtrlr.restoreEmployee(id);
		String msg = String.format("successfully restored employee: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedemployees", true);
		return redirectView;
	}
	
	@GetMapping("/deletedemployees/{id}")
	public RedirectView employeeToBeDeleted(RedirectAttributes redirectAttributes, @PathVariable("id") String id) {
		_empCtrlr.deleteEmployee(id);
		String msg = String.format("successfully deleted Employee: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedemployees", true);
		return redirectView;
	}
	
	/* payroll */
	@GetMapping("/payroll")
	public String getAllEmployeesForPayroll(Model model) {
		List<EmployeeResponse> emps = _empCtrlr.getAllEmployees();
		List<PaystubResponse> stubs = _payrollCtrlr.getAllPaystubs();
		model.addAttribute("empController", _empCtrlr);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("employees",emps);
		model.addAttribute("paystubs",stubs);
		model.addAttribute("ver",_const.getVer());
		return "payroll";
	}
	
	@GetMapping("/paystubs/{id}")
	public String getAllPaystubsByEmployeeId(Model model, @PathVariable("id") String id) throws ElementNotFoundException{
		EmployeeResponse emp = _empCtrlr.getEmployee(id);
		PaystubRequest stub = new PaystubRequest();
		this.setEmployeeDetails(stub, emp.id(), emp.getFullName(), emp.pay());
		List<PaystubResponse> stubs = _payrollCtrlr.getPaystubsForEmployee(id);
		model.addAttribute("jobsites", Jobsite.values());
		model.addAttribute("employee", emp);
		model.addAttribute("paystubs", stubs);
		model.addAttribute("paystub", stub);
		model.addAttribute("extStubSrv", _extStubSrv);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("ver",_const.getVer());
		return "paystubs";
	}
	
	@GetMapping("/paystub/{id}")
	public String getPaystub(Model model, @PathVariable("id") String id) throws ElementNotFoundException{
		PaystubResponse stub = _payrollCtrlr.getPaystub(id);
		model.addAttribute("jobsites", Jobsite.values());
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("paystub", stub);
		model.addAttribute("ver",_const.getVer());
		return "paystub";
	}
	
	@PostMapping("/paystubs/{id}")
	public RedirectView createPaystub(@Valid @ModelAttribute("paystub") PaystubRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		String msg = "";
		RedirectView redirectView;
		boolean errorsPresent = result.hasErrors();
		if(errorsPresent) {
			String errList = this.HumanReadibleErrors(result.getAllErrors());
			log.warn("Issue(s): " + errList);
			msg = String.format("Failed to add paystub for employee: %s. Reason - BAD REQUEST: %s", request.getFullName(), errList);
			redirectView = new RedirectView("/paystubs/" + request.getEmployeeId(), true);
		}
		else {
			PaystubResponse newStub = _payrollCtrlr.createPaystub(request);
			msg = "Paystub for Employee: " + newStub.fullName() + " was added.";
			redirectView = new RedirectView("/paystubs/" + newStub.employeeId(), true);
		}		
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@PostMapping("/paystub/{id}")
	public RedirectView updatePaystub(@PathVariable("id") String id, @Valid @ModelAttribute("paystub") PaystubRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		String msg = "";
		RedirectView redirectView;
		boolean errorsPresent = result.hasErrors();
		if(errorsPresent) {
			String errList = this.HumanReadibleErrors(result.getAllErrors());
			log.warn("Issue(s): " + errList);
			msg = String.format("Failed to update paystub. Reason - BAD REQUEST: %s", errList);
			redirectView = new RedirectView("/paystub/" + id, true);
		}
		else {
			PaystubResponse updatedStub = _payrollCtrlr.updatePaystub(id, request);
			if(!updatedStub.active()) {
				msg = "Paystub Was Deleted Successfully.";
				redirectView = new RedirectView("/paystubs/" + updatedStub.employeeId(), true);
			}
			else {
				msg = "Paystub was updated.";
				redirectView = new RedirectView("/paystub/" + updatedStub.id(), true);
			}
		}
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@GetMapping("/deletedpaystubs")
	public String getDeletedPaystubs(Model model ) {
		List<PaystubResponse> stubs = _payrollCtrlr.getDeletedPaystubs();
		model.addAttribute("paystubs",stubs);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("ver",_const.getVer());
		return "deletedpaystubs";
	}
	
	@GetMapping("/restorepaystub/{id}")
	public RedirectView restorePaystub(RedirectAttributes redirectAttributes, @PathVariable("id") String id) throws ElementNotFoundException {
		_payrollCtrlr.restorePaystub(id);
		String msg = String.format("successfully restored paystub: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedpaystubs", true);
		return redirectView;
	}
	
	@GetMapping("/deletedpaystubs/{id}")
	public RedirectView paystubToBeDeleted(RedirectAttributes redirectAttributes, @PathVariable("id") String id) {
		_payrollCtrlr.deletePaystub(id);
		String msg = String.format("successfully deleted paystub: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedpaystubs", true);
		return redirectView;
	}
	
	/* csv writer */
	@GetMapping("/export")
	public RedirectView writePaystubsToCsv(RedirectAttributes redirectAttributes) throws IOException {
		List<PaystubResponse> stubs = _payrollCtrlr.getAllPaystubs();
		_writer.writePaystubsToCSV(stubs);
		String msg = String.format("CSV Created Successfully!");
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/download/" + _writer.getFileName(), true);
		return redirectView;
	}
	
	/* utility methods */
	private void setEmployeeDetails(PaystubRequest request, String id, String fullName, double pay ) {
		request.setEmployeeId(id);
		request.setFullName(fullName);
		request.setPay(pay);
	}
	
	private String HumanReadibleErrors(List<ObjectError> errors) {
		String errs = "";
		for(ObjectError error : errors) {
			errs += error.getDefaultMessage() + "\s";
		}
		return errs;
	}
}
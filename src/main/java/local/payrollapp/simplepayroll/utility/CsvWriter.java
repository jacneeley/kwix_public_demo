package local.payrollapp.simplepayroll.utility;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import local.payrollapp.simplepayroll.paystub.PaystubResponse;

@Service
public class CsvWriter {
	
	private static final String CSV_SEP = ",";
	private String file;
	private String fileName;
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void writePaystubsToCSV(List<PaystubResponse> stubList) throws IOException {
		String path = System.getProperty("user.dir") + "/src/main/resources/static/sheets/";
		this.setFile(path);
		this.setFileName(LocalDate.now().toString() + "_paystubs.csv");
		BufferedWriter bw = null;
		try {
			Double hours = 0.0;
			Double pmts = 0.0;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFile() + this.getFileName()), "UTF-8"));
			StringBuffer headerLine = new StringBuffer();
			String header = "id,employeeId,fullName,jobsite,pay,hoursWorked,active,dayWorked,createAt,updateAt,";
			headerLine.append(header);
			bw.write(headerLine.toString());
            bw.newLine();
			for (PaystubResponse stub : stubList) {
				StringBuffer stubLine = new StringBuffer();
				stubLine.append(stub.id());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.employeeId());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.fullName());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.jobsite());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.pay());
				pmts += stub.pay();
				stubLine.append(CSV_SEP);
				stubLine.append(stub.hoursWorked());
				hours += stub.hoursWorked();
				stubLine.append(CSV_SEP);
				stubLine.append(stub.active());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.dayWorked());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.createAt());
				stubLine.append(CSV_SEP);
				stubLine.append(stub.updateAt());
				stubLine.append(CSV_SEP);
				bw.write(stubLine.toString());
                bw.newLine();
			}
			Double totalPay = hours * pmts;
			String total = "total hours, labor expense,";
			StringBuffer totalLine = new StringBuffer();
			totalLine.append(total);
			bw.write(totalLine.toString());
            bw.newLine();
            
            String totals = hours.toString() + "," + totalPay.toString(); 
			StringBuffer totalsLine = new StringBuffer();
			totalsLine.append(totals);
			bw.write(totalsLine.toString());
            
            bw.flush();
            bw.close();
		} 
		catch (UnsupportedEncodingException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (FileNotFoundException e){ 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (IOException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
	}
}
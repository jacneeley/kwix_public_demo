package local.payrollapp.simplepayroll;

import org.springframework.stereotype.Component;

@Component
public class AppConstants {
	private String ver = "Ver. Public PRE-ALPHA";

	public String getVer() {
		return ver;
	}
}

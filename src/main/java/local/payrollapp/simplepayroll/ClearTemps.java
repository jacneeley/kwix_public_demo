package local.payrollapp.simplepayroll;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import local.payrollapp.simplepayroll.utility.DeleteFiles;

@Component
@EnableScheduling
public class ClearTemps {
	private final DeleteFiles _delFiles;
	
	public ClearTemps(DeleteFiles delFiles) {
		this._delFiles = delFiles;
	}
	
	//this works for now, but maybe there is a way to do this when the download method is called...
	//@Scheduled(fixedRate = 780000)
	@EventListener(ApplicationReadyEvent.class)
	public void deleteFiles() {
		_delFiles.deleteFiles();
	}
}
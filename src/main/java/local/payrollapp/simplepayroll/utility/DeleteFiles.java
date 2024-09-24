package local.payrollapp.simplepayroll.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeleteFiles {
	private static final Logger log = LoggerFactory.getLogger(DeleteFiles.class);
	
	public void deleteFile(String filePath, String fileName) {
		String fPath = String.format("%s/%s", filePath, fileName);
		Path path = FileSystems.getDefault().getPath(fPath);
		try {
			Files.delete(path);
		}
		catch(NoSuchFileException e) {
			log.error(System.err.format("%s: no such file or dir...", filePath + fileName).toString());
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFiles() {
		log.info("Checking for stale files...");
		File dir = new File(System.getProperty("user.dir") + "/src/main/resources/static/sheets/");
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (!Integer.valueOf(files.length).equals(0)){
				for (File f : files) {
					deleteFile(dir.toString(), f.getName());
				}
				log.info("Files Cleared!");
			}
			else {
				log.info("No files to delete.");
			}
		}
	}
}
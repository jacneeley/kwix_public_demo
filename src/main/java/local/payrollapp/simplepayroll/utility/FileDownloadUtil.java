package local.payrollapp.simplepayroll.utility;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadUtil {
	String path = System.getProperty("user.dir") + "/src/main/resources/static/sheets/";
	private final Path fileStorageLocation = Paths.get(path);
	
	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok()
					.header("Content-Disposition", "attatchment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}

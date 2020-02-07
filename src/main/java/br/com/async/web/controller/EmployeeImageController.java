package br.com.async.web.controller;

import br.com.async.model.Employee;
import br.com.async.repository.EmployeeRepository;
import br.com.async.web.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/employee-management/employees/{id}/photo")
@PropertySource("classpath:application.properties")
public class EmployeeImageController {

	@Autowired
	private EmployeeRepository repository;

	private File uploadDirRoot;

	@Autowired
	EmployeeImageController(@Value("${image.upload.dir}") String uploadDir, EmployeeRepository repository) {
		this.uploadDirRoot = new File(uploadDir);
		this.repository = repository;
	}

	@GetMapping
	ResponseEntity<Resource> read(@PathVariable Long id) {
		return this.repository.findById(id).map(employee -> {
			File file = fileFor(employee);
			Resource fileSystemResource = new FileSystemResource(file);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
		}).orElseThrow(() -> new RecordNotFoundException("Image for available"));
	}

	@Async("asyncExecutor")
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, consumes = { "multipart/form-data" })
	Callable<ResponseEntity<?>> write(HttpServletRequest request, @PathVariable Long id, @RequestParam("file") MultipartFile file)
			throws Exception {
		boolean asyncSupported = request.isAsyncSupported();
		boolean asyncStarted = request.isAsyncStarted();
		System.out.println(asyncStarted);
		System.out.println(asyncSupported);

		return () -> this.repository.findById(id).map(employee -> {
			File fileForEmployee = null;

			try {
				fileForEmployee = uploadPath(employee, file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			Enumeration<String> attributeNames = request.getAsyncContext().getRequest().getAttributeNames();
			System.out.println(attributeNames);


			try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(fileForEmployee)) {
				FileCopyUtils.copy(in, out);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

			URI location = fromCurrentRequest().buildAndExpand(id).toUri();

			return ResponseEntity.created(location).build();
		}).orElseThrow(() -> new RecordNotFoundException("Employee id is not present in database"));
	}

	@PostMapping(value = "/multiple", consumes = { "multipart/form-data" })
	Callable<ResponseEntity<?>> writeMultiple(@PathVariable Long id, @RequestParam("files") MultipartFile[] files)
			throws Exception {
		return () -> this.repository.findById(id).map(employee -> {
			
			Arrays.asList(files).stream().forEach(file -> {
				File fileForEmployee;
				try {
					fileForEmployee = uploadPath(employee, file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(fileForEmployee)) {
					FileCopyUtils.copy(in, out);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			});

			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new RecordNotFoundException("Employee id is not present in database"));
	}

	private File fileFor(Employee e) {
		File uploadPath = Paths.get(this.uploadDirRoot.getPath(), e.getId().toString()).toFile();

		return new File(uploadPath.getAbsolutePath(), Long.toString(e.getId()) + ".jpg");
	}
	
	private File uploadPath(Employee e, MultipartFile file) throws IOException {
		File uploadPath = Paths.get(this.uploadDirRoot.getPath(), e.getId().toString()).toFile();
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		return new File(uploadPath.getAbsolutePath(), file.getOriginalFilename());
	}
}
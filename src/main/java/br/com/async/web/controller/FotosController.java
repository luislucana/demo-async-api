package br.com.async.web.controller;

import br.com.async.util.FooRunnable;
import br.com.async.util.FotoStorageRunnable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@RequestMapping("/fotos")
public class FotosController {

	@PostMapping(value = "/fotos")
	public DeferredResult<String> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<String> resultado = new DeferredResult<>();

		Thread thread = new Thread(new FotoStorageRunnable(files, resultado));
		thread.start();

		System.out.println("iniciou a thread");
		return resultado;
	}

	@PostMapping(value = "/fotos2", produces = MediaType.TEXT_PLAIN_VALUE)
	public String upload2(@RequestParam("files[]") MultipartFile[] files) {
		Thread thread = new Thread(new FooRunnable(files));
		thread.start();

		System.out.println("iniciou a thread!!!");
		return "fim";
	}
	
}

package br.com.async.web.controller;

import br.com.async.util.FooRunnable;
import br.com.async.util.FotoStorageRunnable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ForkJoinPool;

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

	//@Async("asyncExecutor")
	@PostMapping(value = "/fotos2", produces = MediaType.TEXT_PLAIN_VALUE)
	public String upload2(@RequestParam("files[]") MultipartFile[] files, HttpServletRequest request) {

		boolean asyncStarted = request.isAsyncStarted();
		boolean asyncSupported = request.isAsyncSupported();
		System.out.println(asyncStarted);
		System.out.println(asyncSupported);

		Thread thread = new Thread(new FooRunnable(files));
		thread.start();

		System.out.println("iniciou a thread!!!");
		System.out.println("iniciou a thread2!!!");
		System.out.println("iniciou a thread3!!!");
		System.out.println("iniciou a thread4!!!");
		System.out.println("iniciou a thread5!!!");
		System.out.println("iniciou a thread6!!!");
		System.out.println("iniciou a thread7!!!");
		System.out.println("iniciou a thread8!!!");
		return "fim";
	}

	@Async("asyncExecutor")
	@GetMapping("/asynchronous-request")
	public DeferredResult <ResponseEntity< ? >> asynchronousRequestProcessing(final Model model) {

		System.out.println("Started processing asynchronous request");
		final DeferredResult < ResponseEntity < ? >> deferredResult = new DeferredResult < > ();

		/**
		 * Section to simulate slow running thread blocking process
		 */
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		//forkJoinPool.submit(new FooRunnable(null));
		forkJoinPool.submit(() -> {
			System.out.println("Processing request in new thread");

			try {
				Thread.sleep(2000);
				System.out.println("blablabla");
				Thread.sleep(2000);

			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("InterruptedException while executing the thread {}");
			}

			System.out.println("pre-setresult");
			deferredResult.setResult(ResponseEntity.ok("OK"));
			System.out.println("pos-setresult");
		});

		System.out.println("a");
		System.out.println("b");

		return deferredResult;
	}
}

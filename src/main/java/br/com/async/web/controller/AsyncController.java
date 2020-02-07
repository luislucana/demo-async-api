package br.com.async.web.controller;

import br.com.async.model.EmployeeAddresses;
import br.com.async.model.EmployeeNames;
import br.com.async.model.EmployeePhone;
import br.com.async.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
public class AsyncController {

	private static Logger log = LoggerFactory.getLogger(AsyncController.class);

	@Autowired
	private AsyncService service;

	@RequestMapping(value = "/testAsynch", method = RequestMethod.GET)
	public void testAsynch() throws InterruptedException, ExecutionException {
		log.info("testAsynch Start");

		CompletableFuture<EmployeeAddresses> employeeAddress = service.getEmployeeAddress();
		CompletableFuture<EmployeeNames> employeeName = service.getEmployeeName();
		CompletableFuture<EmployeePhone> employeePhone = service.getEmployeePhone();

		// Wait until they are all done
		CompletableFuture.allOf(employeeAddress, employeeName, employeePhone).join();

		System.out.println("------------------------------------------------------------------------------");

		log.info("EmployeeAddress--> " + employeeAddress.get());
		log.info("EmployeeName--> " + employeeName.get());
		log.info("EmployeePhone--> " + employeePhone.get());
	}

	@GetMapping(path = "/testingAsync")
	public DeferredResult<String> value() throws ExecutionException, InterruptedException, TimeoutException {
		AsyncRestTemplate restTemplate = new AsyncRestTemplate();
		String baseUrl = "http://localhost:8081/addresses";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String value = "";

		HttpEntity entity = new HttpEntity("parameters", requestHeaders);
		final DeferredResult<String> result = new DeferredResult<>();
		ListenableFuture<ResponseEntity<EmployeeAddresses>> futureEntity = restTemplate.getForEntity(baseUrl, EmployeeAddresses.class);

		futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<EmployeeAddresses>>() {
			@Override
			public void onSuccess(ResponseEntity<EmployeeAddresses> resultado) {
				System.out.println("Inicio do onSuccess");
				System.out.println(resultado.getBody().toString());
				result.setResult(resultado.getBody().toString());
				System.out.println("Fim do onSuccess");
			}

			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Inicio do onFailure");
				result.setErrorResult(ex.getMessage());
				System.out.println("Fim do onFailure");
			}
		});

		System.out.println("fim do testingAsync!");
		return result;
	}
}

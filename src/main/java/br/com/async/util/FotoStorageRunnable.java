package br.com.async.util;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public class FotoStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<String> resultado;

	public FotoStorageRunnable(MultipartFile[] files) {
		this.files = files;
	}
	
	public FotoStorageRunnable(MultipartFile[] files, DeferredResult<String> resultado) {
		this.files = files;
		this.resultado = resultado;
	}

	@Override
	public void run() {
		System.out.println(">>> files: " + files[0].getSize());
		// TODO: Salvar a foto no sistema de arquivos...
		if (resultado != null) {
			resultado.setResult("OK! Foto recebida!");
		}
		System.out.println("fim do metodo run()");
	}

}

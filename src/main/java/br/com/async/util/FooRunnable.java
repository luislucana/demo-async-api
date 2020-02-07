package br.com.async.util;

import org.springframework.web.multipart.MultipartFile;

public class FooRunnable implements Runnable {
    private MultipartFile[] files;

    public FooRunnable(MultipartFile[] files) {
        this.files = files;
    }

    @Override
    public void run() {
        System.out.println(">>> files: " + files[0].getSize());
        // TODO: Salvar a foto no sistema de arquivos...
        System.out.println("fim do metodo run()!!!");
    }
}

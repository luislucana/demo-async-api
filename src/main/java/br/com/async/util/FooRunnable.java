package br.com.async.util;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

//@Scope("prototype")
public class FooRunnable implements Runnable {
    private MultipartFile[] files;

    public FooRunnable(MultipartFile[] files) {
        this.files = files;
    }

    //@Async("asyncExecutor")
    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.files != null)
            System.out.println(">>> files: " + files[0].getSize());
        // TODO: Salvar a foto no sistema de arquivos...
        System.out.println("fim do metodo run()!!!");
    }
}

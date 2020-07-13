package com.algaworks.ecommerce.concorrencia;

import org.junit.Test;

public class ThreadTest {
    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            //
        }
    }

    private static void log(Object obj) {
        System.out.println("[LOG " + System.currentTimeMillis() + "] " + obj);
    }

    @Test
    public void entenderThreads() {
        Runnable runnable1 = () -> {
            log("Runnable 01 vai esperar 5 segundos.");
            esperar(5);
            log("Runnable 01 concluído");
        };

        Runnable runnable2 = () -> {
            log("Runnable 02 vai esperar 1 segundo.");
            esperar(1);
            log("Runnable 02 concluído");
        };

        Runnable runnable3 = () -> {
            log("Runnable 03 vai esperar 3 segundos.");
            esperar(3);
            log("Runnable 03 concluído");
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log("Encerrando o método de teste");
    }
}

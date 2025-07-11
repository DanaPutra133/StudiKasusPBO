package com.kelompok3_multi;

import java.util.concurrent.TimeUnit;

public class ThreadPersiapan implements Runnable {
    private String namaDocuments;
    private volatile boolean isDONE = false;

    public ThreadPersiapan(String namaDocuments) {
        this.namaDocuments = namaDocuments;
    }


    public void run() {
        System.out.println(main.ANSI_BLUE + ">>> Memulai persiapan dokumen: '" + namaDocuments + "' <<<" + main.ANSI_RESET);
        try {
            System.out.println(main.ANSI_YELLOW + "  [Persiapan " + namaDocuments + "] Validasi format..." + main.ANSI_RESET);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(main.ANSI_YELLOW + "  [Persiapan " + namaDocuments + "] Pengaturan margin..." + main.ANSI_RESET);
            TimeUnit.SECONDS.sleep(1);

            for (int i = 0; i < 5; i++) {
                System.out.print(main.ANSI_YELLOW + "." + main.ANSI_RESET);
                TimeUnit.MILLISECONDS.sleep(200);
            }
            System.out.println();

            this.isDONE = true;
            System.out.println(main.ANSI_GREEN + ">>> Persiapan dokumen '" + namaDocuments + "' SELESAI! ✔" + main.ANSI_RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.isDONE = false;
            System.err.println(main.ANSI_RED + "!!! ERROR: Persiapan dokumen '" + namaDocuments + "' diinterupsi: " + e.getMessage() + main.ANSI_RESET);
        }
    }

    public boolean isDONE() {
        return isDONE;
    }
}
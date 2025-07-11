package com.kelompok3_multi;

import java.util.concurrent.TimeUnit;

public class ThreadCetak implements Runnable {
    private String namaDocuments;

    public ThreadCetak(String namaDocuments) {
        this.namaDocuments = namaDocuments;
    }


    public void run() {
        System.out.println(main.ANSI_BLUE + "\n>>> Memulai pencetakan dokumen: '" + namaDocuments + "' <<<" + main.ANSI_RESET);
        try {
            System.out.println(main.ANSI_YELLOW + "  [Cetak " + namaDocuments + "] Mengirim ke printer..." + main.ANSI_RESET);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(main.ANSI_YELLOW + "  [Cetak " + namaDocuments + "] Mencetak halaman..." + main.ANSI_RESET);
            TimeUnit.SECONDS.sleep(1);

            for (int i = 0; i < 3; i++) {
                System.out.print(main.ANSI_YELLOW + "#" + main.ANSI_RESET);
                TimeUnit.MILLISECONDS.sleep(200);
            }
            System.out.println();

            System.out.println(main.ANSI_GREEN + ">>> Pencetakan dokumen '" + namaDocuments + "' BERHASIL! ✔" + main.ANSI_RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(main.ANSI_RED + "!!! ERROR: Pencetakan dokumen '" + namaDocuments + "' diinterupsi: " + e.getMessage() + main.ANSI_RESET);
        }
    }
}
package com.kelompok3;

public class ThreadCetak implements Runnable {
    private String namaDocuments;

    public ThreadCetak(String namaDocuments) {
        this.namaDocuments = namaDocuments;
    }

    public void run(){
        System.out.println("memulai proses cetak dokumen " + namaDocuments );
        try {
            for ( int i =0; i < 3; i++){

               Thread.sleep(1000);

                //blok sini untuk animasi loading
                // sampai sini
//                System.out.println("dokumen siap di cetak " + namaDocuments);
                Thread.sleep(300);
            }
            System.out.println("proses mencetak dokumen ");
            Thread.sleep(1000);
            System.out.println("mencetak dokumen " + namaDocuments);
            Thread.sleep(1000);

            System.out.println("dokumen " + namaDocuments + " berhasil di cetak ");
            System.out.println("Ini dia dokumen nya yang berasil di cetak atas nama dokumen: " + namaDocuments);
        } catch (InterruptedException e) {
            //error ketika dokumen gagal di siapakan
            Thread.currentThread().interrupt();
            System.out.println("error menyiapkan dokumen, dokumen di intrupsi " + namaDocuments );
        }
    }
}

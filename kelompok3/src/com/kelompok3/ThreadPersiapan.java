package com.kelompok3;

public class ThreadPersiapan implements Runnable {
    private String namaDocuments;
    private boolean isDONE;
    //private

    public ThreadPersiapan (String namaDocuments) {
        this.namaDocuments = namaDocuments;
        this.isDONE = true;

    }
    public void run() {
        System.out.println("memulai persiapan dokumen yang akan di cetak " + namaDocuments );
        try {
            //simulasi persiapan dokumen
            for ( int i =0; i < 3; i++){

                Thread.sleep(1000);

                //blok sini untuk animasi loading
                // sampai sini
//                System.out.println("dokumen siap di cetak " + namaDocuments);
                System.out.println("#");
                Thread.sleep(300);
            }

        } catch (InterruptedException e) {
            //error ketika dokumen gagal di siapakan
            Thread.currentThread().interrupt();
            System.out.println("error menyiapkan dokumen, dokumen di intrupsi " + namaDocuments );
        }

    }
    public boolean isDONE() {
        return isDONE;
    }

}

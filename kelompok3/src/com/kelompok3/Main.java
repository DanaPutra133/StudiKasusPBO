package com.kelompok3;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SISTEM PERCETAKAN KELOMPOK 3");

        try {
            // Menunggu 3 detik untuk masuk ke proses input dokumen
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("Proses tidur diinterupsi: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        // Input dokumen dari user
        System.out.println("Masukan nama dokumen yang akan di cetak:");
        String namaDocuments = scanner.nextLine();

        // Inisialisasi thread persiapan
        ThreadPersiapan persiapanTugas = new ThreadPersiapan(namaDocuments);
        // Ubah nama variabel Thread agar tidak sama dengan nama kelas
        Thread threadPersiapan = new Thread(persiapanTugas, "persiapan dokumen");

        // Inisialisasi thread cetak
        ThreadCetak tugasCetak = new ThreadCetak(namaDocuments); // Gunakan nama variabel yang berbeda
        // Ubah nama variabel Thread agar tidak sama dengan nama kelas
        Thread threadCetak = new Thread(tugasCetak, "pencetakan dokumen"); // Beri nama yang lebih spesifik

        threadPersiapan.start(); // Memulai thread persiapan

        System.out.println("Menunggu persiapan dokumen...");
        char[] animasiChars = {'/', '|', '-', '\\'}; // Karakter animasi
        int i = 0;
        // Untuk menghitung berapa kali animasi ditampilkan

        // Loop animasi loading
        while (threadPersiapan.isAlive()) {
            String animasiChar = String.valueOf(animasiChars[i++ % animasiChars.length]);
            // Menggunakan \r untuk kembali ke awal baris dan menimpa teks sebelumnya
            System.out.print("\rSedang menyiapkan dokumen " + namaDocuments + " " + animasiChar + " ");

            try {
                Thread.sleep(150); // Jeda singkat agar animasi terlihat (misal 150ms)
            } catch (InterruptedException e) {
                System.err.println("\nMain thread diinterupsi saat animasi: " + e.getMessage());
                Thread.currentThread().interrupt();
                break; // Keluar dari loop animasi jika diinterupsi
            }
        }

        // Setelah loop selesai, cetak baris baru untuk membersihkan animasi loading
        System.out.println(); // Pindah ke baris baru setelah animasi selesai

        String statusAkhirPersiapan = "isAlive():" + threadPersiapan.isAlive();
        System.out.println("PERSIAPAN SELESAI (" + statusAkhirPersiapan + ")");

        // Pastikan thread persiapan benar-benar selesai sebelum melanjutkan
        try {
            threadPersiapan.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread diinterupsi saat menunggu join persiapan: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        // Cek status persiapan dokumen
        if (persiapanTugas.isDONE()) {
            System.out.println("Status: Dokumen '" + namaDocuments + "' siap di cetak.");
            threadCetak.start(); // Memulai thread cetak

            // Anda mungkin ingin menunggu threadCetak selesai juga
            try {
                threadCetak.join();
            } catch (InterruptedException e) {
                System.err.println("Main thread diinterupsi saat menunggu join cetak: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            System.out.println("Pencetakan dokumen '" + namaDocuments + "' selesai.");
        } else {
            System.out.println("Dokumen gagal di cetak karena persiapan nya gagal.");
        }

        System.out.println(" ----- SELESAI ------ ");
        scanner.close();
    }
}
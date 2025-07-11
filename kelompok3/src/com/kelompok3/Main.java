package com.kelompok3;

import java.util.Scanner;

public class Main {
    // warna dari text console nya
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "===     SISTEM PERCETAKAN KELOMPOK 3   ===" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);

        try {
            System.out.println("Sistem sedang memulai... " + ANSI_YELLOW + "(Mohon tunggu)" + ANSI_RESET);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println(ANSI_RED + "Proses startup diinterupsi: " + e.getMessage() + ANSI_RESET);
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + ANSI_PURPLE + ">>>>> Masukan Detail Dokumen <<<<<" + ANSI_RESET);
        System.out.print("Masukan nama dokumen yang akan di cetak: ");
        // input nama dokumen dari user
        String namaDocuments = scanner.nextLine();

        ThreadPersiapan persiapanTugas = new ThreadPersiapan(namaDocuments);
        Thread threadPersiapan = new Thread(persiapanTugas, "thread-persiapan");

        ThreadCetak tugasCetak = new ThreadCetak(namaDocuments);
        Thread threadCetak = new Thread(tugasCetak, "thread-pencetakan");

        System.out.println("\n" + ANSI_BLUE + "Memulai proses persiapan untuk dokumen: '" + namaDocuments + "'..." + ANSI_RESET);
        threadPersiapan.start();

        char[] animasiChars = {'/', '|', '-', '\\'}; // karakter animasi
        int i = 0;

        while (threadPersiapan.isAlive()) {
            String animasiChar = String.valueOf(animasiChars[i++ % animasiChars.length]);
            System.out.print("\r" + ANSI_YELLOW + "  ⏳ Sedang menyiapkan " + namaDocuments + " " + animasiChar + ANSI_RESET);

            try {
                Thread.sleep(150); // jeda biar animasi nya ada
            } catch (InterruptedException e) {
                System.err.println("\n" + ANSI_RED + "Main thread diinterupsi saat animasi persiapan: " + e.getMessage() + ANSI_RESET);
                Thread.currentThread().interrupt();
                break; // Keluar dari loop animasi jika diinterupsi
            }
        }

        System.out.println();

        try {
            threadPersiapan.join();
        } catch (InterruptedException e) {
            System.err.println(ANSI_RED + "Main thread diinterupsi saat menunggu join persiapan: " + e.getMessage() + ANSI_RESET);
            Thread.currentThread().interrupt();
        }

        // Cek status akhir persiapan dokumen
        // kalau udah selesai threadcetak baru jalan
        // jika threadpersiapan membalikan nilai true ke isDONE maka perintah cetak akan di jalankan
        if (persiapanTugas.isDONE()) {
            System.out.println(ANSI_GREEN + "✔ Persiapan dokumen '" + namaDocuments + "' SELESAI!" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Memulai proses pencetakan..." + ANSI_RESET);
            threadCetak.start(); // Memulai thread cetak

            i = 0;
            while (threadCetak.isAlive()) {
                String animasiChar = String.valueOf(animasiChars[i++ % animasiChars.length]);
                System.out.print("\r" + ANSI_YELLOW + "  🖨️ Sedang mencetak " + namaDocuments + " " + animasiChar + ANSI_RESET);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.err.println("\n" + ANSI_RED + "Main thread diinterupsi saat animasi pencetakan: " + e.getMessage() + ANSI_RESET);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println();

            try {
                threadCetak.join();
            } catch (InterruptedException e) {
                System.err.println(ANSI_RED + "Main thread diinterupsi saat menunggu join cetak: " + e.getMessage() + ANSI_RESET);
                Thread.currentThread().interrupt();
            }
            System.out.println(ANSI_GREEN + "✔ Pencetakan dokumen '" + namaDocuments + "' BERHASIL!" + ANSI_RESET);

        } else {
            System.out.println(ANSI_RED + "✖ Dokumen '" + namaDocuments + "' GAGAL dicetak karena persiapan tidak selesai." + ANSI_RESET);
        }

        System.out.println("\n" + ANSI_CYAN + "========================================" + ANSI_RESET);
        System.out.println(ANSI_CYAN + " ----- SELESAI. TERIMA KASIH! ------ " + ANSI_RESET);
        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);
        scanner.close();
    }
}
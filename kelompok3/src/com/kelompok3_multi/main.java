package com.kelompok3_multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class main {
    // Warna ANSI untuk konsol
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> documentNames = new ArrayList<>(); // Untuk menyimpan nama-nama dokumen

        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "===     SISTEM PERCETAKAN KELOMPOK 3   ===" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);

        // Simulasi startup
        try {
            System.out.println("Sistem sedang memulai... " + ANSI_YELLOW + "(Mohon tunggu)" + ANSI_RESET);
            Thread.sleep(2000); // Durasi startup lebih singkat
        } catch (InterruptedException e) {
            System.err.println(ANSI_RED + "Proses startup diinterupsi: " + e.getMessage() + ANSI_RESET);
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + ANSI_PURPLE + ">>>>> Masukan Detail Dokumen <<<<<" + ANSI_RESET);
        System.out.println("Masukkan nama dokumen satu per satu. Ketik '" + ANSI_GREEN + "selesai" + ANSI_RESET + "' untuk mengakhiri input.");

        String inputDocName;
        int docCounter = 1;
        while (true) {
            System.out.print(ANSI_BLUE + "Nama dokumen ke-" + docCounter + " (atau 'selesai'): " + ANSI_RESET);
            inputDocName = scanner.nextLine();
            if (inputDocName.equalsIgnoreCase("selesai")) {
                break;
            }
            if (!inputDocName.trim().isEmpty()) {
                documentNames.add(inputDocName.trim());
                docCounter++;
            } else {
                System.out.println(ANSI_YELLOW + "Nama dokumen tidak boleh kosong. Silakan coba lagi." + ANSI_RESET);
            }
        }

        if (documentNames.isEmpty()) {
            System.out.println(ANSI_RED + "Tidak ada dokumen untuk dicetak. Program selesai." + ANSI_RESET);
            scanner.close();
            return;
        }

        System.out.println("\n" + ANSI_CYAN + "Memulai proses untuk " + documentNames.size() + " dokumen." + ANSI_RESET);
        System.out.println(ANSI_CYAN + "Daftar dokumen: " + documentNames + "\n" + ANSI_RESET);

        List<Thread> preparationThreads = new ArrayList<>();
        List<ThreadPersiapan> preparationTasks = new ArrayList<>();

        System.out.println(ANSI_PURPLE + "--- FASE 1: Memulai SEMUA persiapan dokumen secara paralel ---" + ANSI_RESET);
        for (String docName : documentNames) {
            ThreadPersiapan persiapanTask = new ThreadPersiapan(docName);
            Thread threadPersiapan = new Thread(persiapanTask, "Persiapan-" + docName);

            preparationThreads.add(threadPersiapan);
            preparationTasks.add(persiapanTask); // Menyimpan objek task untuk cek isDONE()

            threadPersiapan.start();
            System.out.println(ANSI_BLUE + "[Main] Thread persiapan untuk '" + docName + "' telah dimulai. (Thread ID: " + threadPersiapan.getId() + ")" + ANSI_RESET);
        }

        System.out.println(ANSI_PURPLE + "\n--- FASE 2: Memantau dan menunggu SEMUA persiapan selesai ---" + ANSI_RESET);

        String overallMessage = "Status umum persiapan: ";
        char[] animationChars = {'|', '/', '-', '\\'};
        int aniIndex = 0;

        boolean anyThreadAlive;
        do {
            anyThreadAlive = false;
            for (Thread thread : preparationThreads) {
                if (thread.isAlive()) {
                    anyThreadAlive = true;
                    break;
                }
            }

            String animationChar = String.valueOf(animationChars[aniIndex++ % animationChars.length]);
            String aliveStatus = anyThreadAlive ? "masih berjalan..." : "semua selesai.";

            System.out.print(
                    "\r" + ANSI_YELLOW + "  ⏳ " + overallMessage + animationChar + " (" + aliveStatus + ")        " + ANSI_RESET
            );

            try {
                TimeUnit.MILLISECONDS.sleep(200); // Interval pembaruan animasi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(ANSI_RED + "\n!!! ERROR: Main thread diinterupsi saat menunggu semua persiapan: " + e.getMessage() + ANSI_RESET);
                break;
            }
        } while (anyThreadAlive);

        System.out.println(ANSI_GREEN + "\r  ✔ " + overallMessage + " -- SEMUA PERSIAPAN SELESAI!            " + ANSI_RESET);
        System.out.println(); // Baris baru untuk kerapian

        System.out.println(ANSI_PURPLE + "--- FASE 3: Memulai pencetakan dokumen (berurutan per dokumen) ---" + ANSI_RESET);
        for (int j = 0; j < documentNames.size(); j++) {
            String currentDocName = documentNames.get(j);
            Thread currentPreparationThread = preparationThreads.get(j);
            ThreadPersiapan currentPreparationTask = preparationTasks.get(j);

            System.out.println(ANSI_BLUE + "\n[Main] Menunggu persiapan untuk '" + currentDocName + "' selesai sepenuhnya (join)..." + ANSI_RESET);
            try {
                currentPreparationThread.join();
                System.out.println(ANSI_BLUE + "[Main] Persiapan '" + currentDocName + "' telah selesai di-join." + ANSI_RESET);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(ANSI_RED + "!!! ERROR: Main thread diinterupsi saat join persiapan '" + currentDocName + "': " + e.getMessage() + ANSI_RESET);
                continue;
            }

            if (currentPreparationTask.isDONE()) {
                System.out.println(ANSI_GREEN + "[Main] Status: Dokumen '" + currentDocName + "' sudah siap dicetak!" + ANSI_RESET);
                ThreadCetak cetakTask = new ThreadCetak(currentDocName);
                Thread threadCetak = new Thread(cetakTask, "Cetak-" + currentDocName);
                threadCetak.start();

                aniIndex = 0;
                while (threadCetak.isAlive()) {
                    String animationChar = String.valueOf(animationChars[aniIndex++ % animationChars.length]);
                    System.out.print(ANSI_YELLOW + "\r  🖨️ Sedang mencetak " + currentDocName + " " + animationChar + "           " + ANSI_RESET); // Tambah spasi
                    try {
                        TimeUnit.MILLISECONDS.sleep(150);
                    } catch (InterruptedException e) {
                        System.err.println(ANSI_RED + "\n!!! ERROR: Main thread diinterupsi saat animasi pencetakan: " + e.getMessage() + ANSI_RESET);
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println();

                try {
                    threadCetak.join();
                    System.out.println(ANSI_GREEN + "[Main] Pencetakan '" + currentDocName + "' telah selesai di-join." + ANSI_RESET);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(ANSI_RED + "!!! ERROR: Main thread diinterupsi saat join pencetakan '" + currentDocName + "': "  + ANSI_RESET);
                }
                System.out.println(ANSI_GREEN + "✔ Pencetakan dokumen '" + currentDocName + "' BERHASIL! (Selesai)" + ANSI_RESET);

            } else {
                System.out.println(ANSI_RED + "✖ Dokumen '" + currentDocName + "' GAGAL dicetak karena persiapan tidak selesai." + ANSI_RESET);
            }
        }

        System.out.println("\n" + ANSI_CYAN + "========================================" + ANSI_RESET);
        System.out.println(ANSI_CYAN + " ----- SEMUA PROSES SELESAI. TERIMA KASIH! ------ " + ANSI_RESET);
        System.out.println(ANSI_CYAN + "========================================" + ANSI_RESET);
        scanner.close();
    }
}
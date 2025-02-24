import java.io.*;
import java.util.*;



public class Main {
    public static void main(String[] args) {
        System.out.println("---------------- IQ Puzzler ----------------");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file test case (ex : tes): ");
        String fileName1 = scanner.nextLine();
        String fileName = ("../test/" + fileName1 +".txt");

        try {
            PuzzleSolver solver = new PuzzleSolver(fileName);
            long start = System.currentTimeMillis();
            boolean solved = solver.solve();
            long finish = System.currentTimeMillis();

            if (solved) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println();
                System.out.println("Solusi ditemukan: ");
                solver.printBoard();

                System.out.println("Jumlah iterasi: " + solver.getIterationCount() + " kali");
                System.out.println("Waktu pencarian: " + (finish - start) + " ms");
                
                System.out.print("Simpan Solusi? (Y/N): ");
                String save = scanner.nextLine();
                if (save.equalsIgnoreCase("Y")) {
                    System.out.print("Masukkan nama file solusi tanpa .txt (ex : solusi1): ");
                    String fileName2 = scanner.nextLine();
                    String Solutionfile = ("../test/" + fileName2 +".txt");
                    solver.saveSolution(Solutionfile);
                    System.out.println("Solusi berhasil disimpan.");
                }
                else {
                    System.out.println("Solusi tidak disimpan.");
                }
            }
            else {
                System.out.println("Solusi tidak ditemukan.");
            }
        } catch (IOException e) {
            System.err.println("File tidak ditemukan.: " + e.getMessage());
        }
        scanner.close();
    }
}
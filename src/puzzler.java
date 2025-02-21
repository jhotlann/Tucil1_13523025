import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class puzzler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath;
        
        try {
            System.out.print("Masukkan nama file txt: ");
            String filename = scanner.next();
            filePath = "../test/" + filename + ".txt";
            System.out.println("Mencoba akses file di: " + new File(filePath).getCanonicalPath());

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            String line;
            line = reader.readLine();
            String[] number = line.split(" ");
            int n = Integer.parseInt(number[0]);
            int m = Integer.parseInt(number[1]);
            int p = Integer.parseInt(number[2]);

            String s = reader.readLine();

            String[] blocks = new String[p];
            for (int i=0; i<p; i++) {
                blocks[i] = reader.readLine();
            }
            reader.close();

            char[][] matrix = new char[n][m];

            if (s == "DEFAULT") {
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
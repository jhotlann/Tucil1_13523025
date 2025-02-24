import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PuzzleSolver {
    private char[][] board; 
    private List<Block> blocks;
    private int N, M;
    private int iterationCount;

    private void readInput(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String[] dims = reader.readLine().split(" ");
        N = Integer.parseInt(dims[0]);
        M = Integer.parseInt(dims[1]);
        int P = Integer.parseInt(dims[2]);
        
        String type = reader.readLine();

        if (N <= 0 || M <= 0){
            System.out.println("Error: Ukuran papan tidak valid");
            System.exit(0);
        } else {
            board = new char[N][M];
            for (char [] row : board) {
                Arrays.fill(row, '.');
            }
        }
        
        blocks = new ArrayList<>();
        Set<Character> usedSymbols = new HashSet<>(); 

        String currentSymbol = null;
        List<String> currentShape = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue; 
    
            char firstChar = line.charAt(0);
    
            if (!Character.isUpperCase(firstChar) || firstChar < 'A' || firstChar > 'Z') {
                System.out.println("Karakter blok "+ firstChar + " tidak valid. Masukkan karakter antara A-Z. ");
                System.exit(0);
            }
    
            if (currentSymbol == null || firstChar != currentSymbol.charAt(0)) {
                if (!currentShape.isEmpty()) {
                    // Validasi: Pastikan blok tidak kosong
                    if (!isValidCharacter(currentShape)) {
                        System.out.println("Blok " + currentSymbol + " tidak valid!");
                        System.exit(0);
                    }
    
                    blocks.add(new Block(currentSymbol.charAt(0), new ArrayList<>(currentShape)));
                    currentShape.clear();
                }
                currentSymbol = String.valueOf(firstChar);
    
                if (usedSymbols.contains(currentSymbol.charAt(0))) {
                    System.out.println("Blok " + currentSymbol + " duplikasi.");
                    System.exit(0);
                }
                usedSymbols.add(currentSymbol.charAt(0));
            }
            currentShape.add(line);
        }
    
        if (!currentShape.isEmpty()) {
            if (!isValidCharacter(currentShape)) {
                System.out.println("Bentuk blok " + currentSymbol + " tidak valid!");
                System.exit(0);
            }
            blocks.add(new Block(currentSymbol.charAt(0), new ArrayList<>(currentShape)));
        }
    
        reader.close();
    
        if (blocks.size() != P) {
            System.out.println("Jumlah blok tidak sesuai dengan masukan P=" + P );
            System.exit(0);
        }
    
        int totalBlockCells = 0;
        for (Block block : blocks) {
            totalBlockCells += block.coordinates.size();
        }

        int totalBoardCells = N * M;
        if (totalBlockCells < totalBoardCells) {
            System.out.println("Blok kurang! Total blok masukkan = " + totalBlockCells + ", tetapi papan memerlukan " + totalBoardCells);
            System.exit(0);
        } else if (totalBlockCells > totalBoardCells) {
            System.out.println("Blok lebih! Total blok masukkan = " + totalBlockCells + ", tetapi papan hanya memiliki " + totalBoardCells);
            System.exit(0);
        }
    }

    private boolean isValidCharacter(List<String> shape) {
        for (String row : shape) {
            for (char c : row.toCharArray()) {
                if (Character.isUpperCase(c) && c >= 'A' && c <= 'Z') {
                    return true; 
                }
            }
        }
        return false; 
    }

    private boolean canPlace(Block block, int startX, int startY) {
        for (int[] cell : block.coordinates) {
            int x = startX + cell[0];
            int y = startY + cell[1];
            if (x < 0 || x >= N || y < 0 || y >= M || board[x][y] != '.') {
                return false;
            }
        }
        return true;
    }

    private void placeBlock(Block block, int startX, int startY, char symbol) {
        for (int[] cell : block.coordinates) {
            int x = startX + cell[0];
            int y = startY + cell[1];
            board[x][y] = symbol;
        }
    }

    private void removeBlock(Block block, int startX, int startY) {
        for (int[] cell : block.coordinates) {
            int x = startX + cell[0];
            int y = startY + cell[1];
            board[x][y] = '.';
        }
    }


    private boolean isValidBoard() {
        int filledCells = 0;
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '.') filledCells++;
            }
        }
        return filledCells == (N * M);
    }
        public void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(Utility.getColor(cell) + " ");
            }
            System.out.println();
        }
    }

    public PuzzleSolver(String filename) throws IOException {
        readInput(filename);
    }

    public boolean solve() {
        System.out.println("Pencarian sedang berlangsung");
        return recursiveSolve(0);
    }

    private boolean recursiveSolve(int index) {
        if (index == blocks.size()) return isValidBoard();
        Block block = blocks.get(index);
        List<Block> orientations = block.generateOrientations();
        for (Block orient : orientations) {  
            for (int a = 0; a < N; a++) {
                for (int c = 0; c < M; c++) {
                    if (canPlace(orient, a, c)) {
                        placeBlock(orient, a, c, block.symbol);
                        clearScreen();
                        printBoard();
                        try { Thread.sleep(0); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                        
                        if (recursiveSolve(index + 1)){
                            return true;
                        } 
                        removeBlock(orient, a, c);
                        clearScreen();
                        printBoard();
                        try { Thread.sleep(0); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } 
                        iterationCount++;
                    }
                }
            }
        }
        return false; 
    }


    public void saveSolution(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (char[] row : board) {
            for (char cell : row) {
                writer.write(cell + " ");
            }
            writer.newLine();
        }
        writer.close();
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public char[][] getBoard() {
        return board;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
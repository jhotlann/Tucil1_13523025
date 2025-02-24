import java.util.*;

public class Block {
    public char symbol;
    public List<int[]> coordinates;

    public Block(char symbol, List<String> shape) {
        this.symbol = symbol;
        this.coordinates = parseShape(shape);
    }
    
    private List<int[]> parseShape(List<String> shape) {
        List<int[]> coord = new ArrayList<>();
        for (int i = 0; i < shape.size(); i++) {
            for (int j = 0; j < shape.get(i).length(); j++) {
                if (shape.get(i).charAt(j) != '.') {
                    coord.add(new int[]{i, j});
                }
            }
        }
        return coord;
    }
    
    public List<Block> generateOrientations() {
        Set<String> seen = new HashSet<>();
        List<Block> orientations = new ArrayList<>();
        char[][] currentShape = toMatrix(this.coordinates);

        for (int i = 0; i < 4; i++) { 
            String shapeStr = matrixToString(currentShape);

            if (seen.add(shapeStr)) {
                orientations.add(new Block(this.symbol, matrixToShape(currentShape)));

                // Cerminkan dan cek bentuk uniknya
                char[][] flipped = flip(currentShape);
                shapeStr = matrixToString(flipped);
                if (seen.add(shapeStr)) {
                    orientations.add(new Block(this.symbol, matrixToShape(flipped)));
                }
            }
            currentShape = rotate(currentShape);
        }
        return orientations;
    }
    
    private char[][] rotate(char[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        char[][] rotated = new char[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - i - 1] = matrix[i][j];
            }
        }
        return rotated;
    }
    
    private char[][] flip(char[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        char[][] flipped = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flipped[i][cols - j - 1] = matrix[i][j];
            }
        }
        return flipped;
    }

    private char[][] toMatrix(List<int[]> coords) {
        int maxX = 0, maxY = 0;
        for (int[] c : coords) {
            maxX = Math.max(maxX, c[0]);
            maxY = Math.max(maxY, c[1]);
        }

        char[][] shape = new char[maxX + 1][maxY + 1];
        for (char[] row : shape) Arrays.fill(row, '.');

        for (int[] c : coords) {
            shape[c[0]][c[1]] = this.symbol;
        }
        return shape;
    }
    
    private String matrixToString(char[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : matrix) {
            sb.append(new String(row)).append("\n");
        }
        return sb.toString();
    }
    
    private List<String> matrixToShape(char[][] matrix) {
        List<String> result = new ArrayList<>();
        for (char[] row : matrix) {
            result.add(new String(row));
        }
        return result;
    }
}
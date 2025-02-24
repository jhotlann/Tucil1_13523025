public class Utility {
    public static String getColor(char c) {
        if (c == '.') return "\u001B[37m.\u001B[0m";
        int color = 31 + (c - 'A') % 6; // 6 warna berbeda
        return "\u001B[" + color + "m" + c + "\u001B[0m";
    }
}


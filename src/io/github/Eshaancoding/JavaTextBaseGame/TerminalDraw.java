package io.github.Eshaancoding.JavaTextBaseGame;

public class TerminalDraw {

    // made using ANSI escape codes :)
    // https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit

    public static void moveCursor (int column, int row) {
        // column is up and down
        // row is right and left
        System.out.print("\u001B["+column+";"+row+"H");
    }

    public static void clearScreen () {
        System.out.print("\u001B[2J");
    }

    public static void showCursor () {
        System.out.print("\u001B[?25h");
    }

    public static void hideCursor () {
        System.out.print("\u001B[?25l");
    }

    public static void setForegroundColor (int r, int g, int b) {
        System.out.print("\u001B[38;2;"+r+";"+g+";"+b+"m");
    }

    public static void setBackgroundColor (int r, int g, int b) {
        System.out.print("\u001B[48;2;"+r+";"+g+";"+b+"m");
    }

    public static void setDefault () {
        System.out.print("\u001B[0m");
    }

    public static void waitMS (int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
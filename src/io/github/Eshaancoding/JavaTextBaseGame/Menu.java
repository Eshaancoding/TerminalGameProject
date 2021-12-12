package io.github.Eshaancoding.JavaTextBaseGame;

import java.lang.Math;
// For Drawing Characters: https://en.wikipedia.org/wiki/Box-drawing_character

public class Menu {
    int max_row;
    int max_column;
    boolean in_menu = true;
    boolean does_quit = false;
    boolean play_selected = true;

    public Menu (int max_row, int max_column) {
        this.max_column = max_column; // up & down
        this.max_row = max_row;       // left & right
        StringBuilder display = new StringBuilder();
        // Draw Display Borders
        for (int column = 0; column < max_column; column++) {
            for (int row = 0; row < max_row; row++) {
                if (column == 0 && row == 0) {
                    display.append("┌");
                }
                else if (column == 0 && row == max_row - 1) {
                    display.append("┐");
                }
                else if (column == max_column - 1 && row == 0) {
                    display.append("└");
                }
                else if (column == max_column - 1 && row == max_row - 1) {
                    display.append("┘");
                }
                else if (column == 0 || column == max_column - 1) {
                    display.append("─");
                } else if (row == 0 || row == max_row - 1) {
                    display.append("│");
                } else {
                    display.append(" ");
                }
            }
            display.append("\n");
        }

        // set up terminal and draw
        TerminalDraw.hideCursor();
        TerminalDraw.moveCursor(0,0);
        System.out.print(display);

        // draw heading
        TerminalDraw.moveCursor(4, (this.max_row / 2) - 14);
        System.out.println("3d Shooter Game in Terminal");

        // draw options
        TerminalDraw.moveCursor((this.max_column / 2) - 2, (this.max_row / 2) - 2);
        TerminalDraw.setForegroundColor(0,0,0);
        TerminalDraw.setBackgroundColor(255, 255, 0);
        System.out.println("Play");
        TerminalDraw.setDefault();
        TerminalDraw.moveCursor((this.max_column / 2) + 2, (this.max_row / 2) - 2);
        System.out.println("Quit");
    }

    public void toggle_selected () {
        if (this.play_selected) {
            TerminalDraw.moveCursor((this.max_column / 2) - 2, (this.max_row / 2) - 2);
            System.out.println("Play");
            TerminalDraw.setDefault();
            TerminalDraw.setForegroundColor(0,0,0);
            TerminalDraw.setBackgroundColor(255, 255, 0);
            TerminalDraw.moveCursor((this.max_column / 2) + 2, (this.max_row / 2) - 2);
            System.out.println("Quit");
            TerminalDraw.setDefault();
            this.play_selected = false;
        } else {
            TerminalDraw.moveCursor((this.max_column / 2) - 2, (this.max_row / 2) - 2);
            TerminalDraw.setForegroundColor(0,0,0);
            TerminalDraw.setBackgroundColor(255, 255, 0);
            System.out.println("Play");
            TerminalDraw.setDefault();
            TerminalDraw.moveCursor((this.max_column / 2) + 2, (this.max_row / 2) - 2);
            System.out.println("Quit");
            this.play_selected = true;
        }
    }
    public void select () {
        if (this.play_selected) {
            this.in_menu = false;
        }
        else {
            does_quit = true;
        }
    }
}

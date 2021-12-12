package io.github.Eshaancoding.JavaTextBaseGame;

import java.io.*;
import java.lang.Math;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.*;
// heavily inspired by: https://www.youtube.com/watch?v=xW8skO7MFYw
// Thanks to https://stackoverflow.com/questions/11823095/whats-the-fastest-way-to-output-a-string-to-system-out for fast printing

public class Game {
    int max_row;
    int max_column;
    float playerX;
    float playerY;
    float direction;
    float fov;
    String game;
    float max_distance;
    Map map;
    OutputStream out = new BufferedOutputStream ( System.out );
    Box directed_box;
    float directed_box_distance;

    public void move_forward () {
        playerY += cos(direction);
        playerX += sin(direction);
    }
    public void move_backward () {
        playerY -= cos(direction);
        playerX -= sin(direction);
    }
    public void turn_right () { direction += 0.05; }
    public void turn_left () {
        direction -= 0.05;
    }
    public Game (int max_row, int max_column) {
        max_distance = 1000;
        fov = 3.1415927f / 4f;
        game = "";
        this.max_row = max_row;
        this.max_column = max_column;
        /* map.txt spec
        e = enemy
        v = door
        d = door with victory
        p = player
        # = wall
        . = empty space
        a = ak47
        f =
        */
        map = new Map("C:\\Users\\eshaa\\IdeaProjects\\TextGame\\src\\io\\github\\Eshaancoding\\JavaTextBaseGame\\Map");
        playerX = 180; // including walls
        playerY = 60; // including walls

    }
    public void start () {
        // Draw Display Borders
        StringBuilder display = new StringBuilder();
        for (int column = 0; column < max_column; column++) {
            for (int row = 0; row < max_row; row++) {
                if (column == max_column - 1 && (row == max_row / 2 - 20 || row == max_row / 2 + 20 || row == max_row / 2 - 7 || row == max_row / 2 + 7)) {
                    display.append("┴");
                }
                else if ((column > max_column - 5 && column < max_column - 1) && (row == max_row / 2 - 20 || row == max_row / 2 + 20 || row == max_row / 2 - 7 || row == max_row / 2 + 7)) {
                    display.append("│");
                }
                else if (column == max_column - 5 && row == max_row / 2 - 20) {
                    display.append("┌");
                }
                else if (column == max_column - 5 && row == max_row / 2 + 20) {
                    display.append("┐");
                }
                else if (column == max_column - 5 && (row == max_row / 2 - 7 || row == max_row / 2 + 7)) {
                    display.append("┬");
                }
                else if (column == max_column - 5 && (row > max_row / 2 - 20 && row < max_row / 2 + 20)) {
                    display.append("─");
                }
                else if (column == 0 && row == 0) {
                    display.append("┌");
                } else if (column == 0 && row == max_row - 1) {
                    display.append("┐");
                } else if (column == max_column - 1 && row == 0) {
                    display.append("└");
                } else if (column == max_column - 1 && row == max_row - 1) {
                    display.append("┘");
                } else if (column == 0 || column == max_column - 1) {
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
        TerminalDraw.moveCursor(0,0);
        System.out.print(display);
        System.out.print(game);

    }
    public void draw () {
        StringBuilder stringBuilder = new StringBuilder();
        // put out rays for every width
        for (int i = 2; i < this.max_row; i++) {
            // get ray angle
            float ray_angle = direction - (fov / 2) + (i / (float)max_row) * fov;

            // start raytracing
            float distance;
            for (distance = 0.1f; distance < max_distance; distance += 0.1) {
                float x_pos = playerX + distance * (float)sin(ray_angle);
                float y_pos = playerY + distance * (float)cos(ray_angle);
                Box result = map.find_box(x_pos, y_pos);
                int height_draw = Math.round(((-1f/500f)*distance+1f) * max_column);
                int color_draw = Math.round(((-1f/500f)*distance+1f) * 255);
                if (result != null && !result.representation.equals(".") && !result.representation.equals("p")) {
                    if (Math.round(ray_angle * 100) == Math.round(direction * 100)) {
                        this.directed_box = result;
                        this.directed_box_distance = distance;
                    }

                    for (int q = 2; q < this.max_column; q++) {
                        // don't draw in inventory
                        if (i >= max_row / 2 - 20 && i <= max_row / 2 + 21 && q > max_column - 5) {
                            continue;
                        }
                        // draw
                        stringBuilder.append("\u001B[").append(q).append(";").append(i).append("H");
                        if (q >= (this.max_column / 2) - (height_draw / 2) && q <= (this.max_column / 2) + (height_draw / 2)) {
                            if (result.representation.equals("v")) {
                                stringBuilder.append("\u001B[38;2;").append(0).append(";").append(color_draw).append(";").append(0).append("m");
                            }
                            else if (result.representation.equals("e") && q >= (this.max_column / 2) - (height_draw / 5) && q <= (this.max_column / 2) + (height_draw / 5)) {
                                stringBuilder.append("\u001B[38;2;").append(color_draw).append(";").append(0).append(";").append(0).append("m");
                            }
                            else if (result.representation.equals("d")) {
                                stringBuilder.append("\u001B[38;2;").append(color_draw).append(";").append(0).append(";").append(color_draw).append("m");
                            }
                            else {
                                stringBuilder.append("\u001B[38;2;").append(color_draw).append(";").append(color_draw).append(";").append(color_draw).append("m");
                            }
                            stringBuilder.append("█");
                        } else {
                            stringBuilder.append("\u001B[0m");
                            stringBuilder.append(" ");
                        }
                    }
                    break;
                }
            }
        }
        TerminalDraw.moveCursor(0,0);
        System.out.print(stringBuilder);
        // tips/directions
        switch (directed_box.representation) {
            case "v" -> {
                int color_draw = Math.round(((-1f / 500f) * directed_box_distance + 1f) * 255);
                TerminalDraw.moveCursor(max_column / 2, max_row / 2 - 2);
                TerminalDraw.setBackgroundColor(0, color_draw, 0);
                System.out.print("Ak47");
                TerminalDraw.moveCursor(max_column / 2, max_row / 2 + 2);
                System.out.print("Q to swap");
            }
            case "d" -> {
                int color_draw = Math.round(((-1f / 500f) * directed_box_distance + 1f) * 255);
                TerminalDraw.setBackgroundColor(color_draw, 0, color_draw);
                TerminalDraw.moveCursor(max_column / 2 - 1, max_row / 2 - 2);
                System.out.print("Door");
                TerminalDraw.moveCursor(max_column / 2 + 1, max_row / 2 - 7);
                System.out.print("e to interact");
            }
            case "e" -> {
                int color_draw = Math.round(((-1f / 500f) * directed_box_distance + 1f) * 255);
                TerminalDraw.setBackgroundColor(color_draw, 0, 0);
                TerminalDraw.moveCursor(max_column / 2 - 5, max_row / 2 - 3);
                System.out.print("Enemy");
            }
        }
    }
}
package io.github.Eshaancoding.JavaTextBaseGame;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {
    private int dim_box = 20;
    private String game;
    private final List<Box> boxes = new ArrayList<>();
    public Map (String pathname) {
        // get map from .txt file

        try {
            File file = new File(pathname);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                this.game += data + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        // set lines_game to 30
        // set y to 29

        // for each character, get box
        assert game != null;
        List<String> lines_game = game.lines().toList();
        for (int y = lines_game.size() - 1; y > -1; y--) {
            for (int x = 0; x < lines_game.get(y).length(); x++) {
                Box new_box = new Box(x*dim_box,(lines_game.size()-1-y)*dim_box, dim_box , dim_box, lines_game.get(y).substring(x,x+1));
                boxes.add(new_box);
            }
        }
    }
    public Map dim_box(int _dim_box) {
        this.dim_box = _dim_box;
        return this;
    }
    public Box find_box (float x, float y) {
        for (Box box : boxes) {
            if (box.point_in_box(x, y)) {
                return box;
            }
        }
        return null;
    }

}

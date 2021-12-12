package io.github.Eshaancoding.JavaTextBaseGame;

/*
Note! (only works for Windows Users, not sure how to make it run for linux)
In order to run this program properly, you don't use the IntelliJ terminal, because it doesn't support ANSI escape codes, which is used to do unique actions with the terminal.
First, get the running command. To do this, run the program and look at the command you see in the terminal. Copy that command.
Then, open up command prompt and copy & paste the running command into the command prompt and press enter. The program should now run.
Next time you want to run you could just build the program and use the same running command in the command prompt.
 */

// Jnativehook is just for handling key presses.
// from: https://stackoverflow.com/questions/21969954/how-to-detect-a-key-press-in-java
// library: https://github.com/kwhat/jnativehook/
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.io.IOException;
import java.lang.annotation.Native;
import java.util.Scanner;

public class Main implements NativeKeyListener {

    private static Menu menu;
    private static Game game;

    public static void end (boolean erase) {
        // exit program
        TerminalDraw.setDefault();
        TerminalDraw.moveCursor(0,0);
        if (erase) TerminalDraw.clearScreen();
        TerminalDraw.showCursor();

        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        System.exit(0);

    }

    public static void main (String args[]) {
        // **************** setup keyboard input and terminal ****************
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Main());
        TerminalDraw.clearScreen();
        TerminalDraw.setDefault();

        // *************** Determine Terminal Size ***************
        int max_row;
        int max_column;
        Scanner scan = new Scanner(System.in);
        System.out.print("\u001b[s");
        System.out.print("\u001b[5000;5000H");
        System.out.print("\u001b[6n");
        System.out.println("\u001b[0;0H");
        System.out.println("Press Enter to start game (ignore whatever below):");
        String parse = scan.nextLine().substring(2);
        parse = parse.substring(0,parse.length()-1);
        int index = parse.indexOf(";");
        max_column = Integer.parseInt(parse.substring(0, index)) - 2;
        max_row = Integer.parseInt(parse.substring(index+1));

        // *************** Main Loop ****************
        menu = new Menu(max_row, max_column);
        game = new Game(max_row, max_column);
        while (!menu.does_quit) {
            Thread.onSpinWait();
            if (!menu.in_menu){
                // we are playing the game
                game.draw();
            }

        }
        end(true);
    }
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keycode = e.getKeyCode();

        if (keycode == NativeKeyEvent.VC_ESCAPE) {
            end(true);
        }

        try {
            if ((keycode == NativeKeyEvent.VC_UP || keycode == NativeKeyEvent.VC_DOWN) && menu.in_menu) {
                menu.toggle_selected();
            } else if (keycode == NativeKeyEvent.VC_ENTER && menu.in_menu) {
                menu.select();
                if (!menu.in_menu) {
                    // we pressed play
                    game.start();
                }
            }
        } catch (java.lang.NullPointerException ignored) {}
        if ((keycode == NativeKeyEvent.VC_UP || keycode == NativeKeyEvent.VC_W) && !menu.in_menu) {
            game.move_forward();
        }
        if (keycode == NativeKeyEvent.VC_DOWN || keycode == NativeKeyEvent.VC_S && !menu.in_menu) {
            game.move_backward();
        }
        if ((keycode == NativeKeyEvent.VC_RIGHT || keycode == NativeKeyEvent.VC_D) && !menu.in_menu) {
            game.turn_right();
        }
        if ((keycode == NativeKeyEvent.VC_LEFT || keycode == NativeKeyEvent.VC_A) && !menu.in_menu) {
            game.turn_left();
        }
        if ((keycode == NativeKeyEvent.VC_EQUALS) && !menu.in_menu) {
            game.fov -= 0.25;
        }
        if ((keycode == NativeKeyEvent.VC_MINUS) && !menu.in_menu) {
            game.fov += 0.25;
        }

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {

    }
}
/* running key cmd:
C:\Users\eshaa\.jdks\corretto-16.0.2\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.3\lib\idea_rt.jar=58945:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.3\bin" -Dfile.encoding=UTF-8 -classpath C:\Users\eshaa\IdeaProjects\TextGame\out\production\TextGame;C:\Users\eshaa\Downloads\jnativehook-2.2.1.jar io.github.Eshaancoding.JavaTextBaseGame.Main
 */
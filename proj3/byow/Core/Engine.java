package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static GameUI WORLD;
    public static MapGenerator.Position avatarPosition;
    public static TETile[][] currentWorldFrame = new TETile[0][0];
    public static MapGenerator.Theme currentTheme;
    public static String fullInputs = "";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        WORLD = new GameUI(true);
        WORLD.mainMenu();

        long seed;
        int inputLength = 0;
        String seedHolder = "";
        String movementHolder = "";
        boolean isSeed = false;
        boolean hasKey = false;

        while (!StdDraw.isKeyPressed(83) || !StdDraw.isKeyPressed(115)) {
            while (!StdDraw.hasNextKeyTyped()) {
                //ignored
            }
            if (StdDraw.isKeyPressed(76) || StdDraw.isKeyPressed(108)) {
                if (movementHolder.equals("")) {
                    System.exit(0);
                } else {
                    //load game from saveFile here;
                    currentWorldFrame = interactWithInputString(seedHolder + movementHolder);
                }
            }
            if (StdDraw.isKeyPressed(81) || StdDraw.isKeyPressed(113)) {
                System.exit(0);
            }

            char inputCharacter = StdDraw.nextKeyTyped();
            System.out.print(inputCharacter);

            if (((inputCharacter == 'n') || (inputCharacter == 'N')) && (inputLength == 0)) {
                fullInputs += inputCharacter;
                isSeed = true;
                WORLD.seedScreen(seedHolder);
                inputLength++;
            }
            if ((inputLength > 0) && isSeed && Character.isDigit(inputCharacter)) {
                seedHolder += inputCharacter;
                WORLD.seedScreen(seedHolder);
            }
            if (((inputCharacter == 's') || (inputCharacter == 'S')) && isSeed) {
                seed = Long.parseLong(seedHolder);
                WORLD.generate(seed, true);
                break;
            }
        }
        while (!StdDraw.isKeyPressed(81) || !StdDraw.isKeyPressed(113)) {
            while (!StdDraw.hasNextKeyTyped()) {
                int mouseCoordX = (int) StdDraw.mouseX();
                int mouseCoordY = (int) StdDraw.mouseY();
                if (mouseCoordX > 0 && mouseCoordX < WIDTH && mouseCoordY > 0 && mouseCoordY < HEIGHT) {
                    String tileDescription = currentWorldFrame[mouseCoordX][mouseCoordY].description();

                    StdDraw.clear(StdDraw.BLACK);
                    ter.renderFrame(currentWorldFrame);
                    Font hudFont = new Font("Monoco", Font.BOLD, 16);
                    StdDraw.setFont(hudFont);
                    StdDraw.setPenColor(Color.WHITE);
                    if (tileDescription.equals("you") || tileDescription.equals("nothing")) {
                        StdDraw.textLeft(1, 38, "This tile is " + tileDescription);
                    } else {
                        StdDraw.textLeft(1, 38, "This tile is a " + tileDescription);
                    }
                    if (!hasKey) {
                        StdDraw.textRight(79,38, "No Key.");
                    } else {
                        StdDraw.textRight(79,38, "Has Key.");
                    }

                    StdDraw.show();
                }
            }


            char movementInput = StdDraw.nextKeyTyped();
            System.out.print(movementInput);

            MapGenerator.Position movedPos = null;
            if ((movementInput == 'w')) {
                movementHolder += movementInput;
                movedPos = new MapGenerator.Position(avatarPosition.x, avatarPosition.y + 1);
            }
            if ((movementInput == 's')) {
                movementHolder += movementInput;
                movedPos = new MapGenerator.Position(avatarPosition.x, avatarPosition.y - 1);
            }
            if ((movementInput == 'a')) {
                movementHolder += movementInput;
                movedPos = new MapGenerator.Position(avatarPosition.x - 1, avatarPosition.y);
            }
            if ((movementInput == 'd')) {
                movementHolder += movementInput;
                movedPos = new MapGenerator.Position(avatarPosition.x + 1, avatarPosition.y);
            }

            if (movementInput == ':') {
                File saveFile = new File("/Users/alexchen/Downloads/cs61b/fa22-proj3-g160/proj3/byow/saveFile.txt");
                try {
                    saveFile.createNewFile();
                    FileWriter writer = new FileWriter("saveFile.txt");
                    fullInputs += seedHolder + movementHolder;
                    System.out.println(fullInputs);
                    writer.write(fullInputs);
                    writer.close();
                    System.exit(0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (movedPos != null) {
                if (isValidTile(movedPos, hasKey)) {
                    if (isKey(movedPos)) {
                        hasKey = true;
                    }
                    if (isFlower(movedPos)) {
                        WORLD.encounterScreen();
                        break;
                    }
                    if (isLockedDoor(movedPos) && hasKey) {
                        WORLD.winScreen();
                        break;
                    }
                    WORLD.moveAvatar(movedPos, true);
                    avatarPosition = movedPos;
                }
            }
        }
    }

    public boolean isValidTile(MapGenerator.Position nextPos, boolean hasKey) {
        TETile[][] tiles = currentWorldFrame;
        return (tiles[nextPos.x][nextPos.y].equals(currentTheme.floor)) ||
                (tiles[nextPos.x][nextPos.y].equals(Tileset.KEY)) ||
                (tiles[nextPos.x][nextPos.y].equals(Tileset.FLOWER)) ||
                (tiles[nextPos.x][nextPos.y].equals(Tileset.LOCKED_DOOR) && hasKey);
    }

    public boolean isKey(MapGenerator.Position nextPos) {
        TETile[][] tiles = currentWorldFrame;
        return tiles[nextPos.x][nextPos.y].equals(Tileset.KEY);
    }

    public boolean isLockedDoor(MapGenerator.Position nextPos) {
        TETile[][] tiles = currentWorldFrame;
        return tiles[nextPos.x][nextPos.y].equals(Tileset.LOCKED_DOOR);
    }

    public boolean isFlower(MapGenerator.Position nextPos) {
        TETile[][] tiles = currentWorldFrame;
        return tiles[nextPos.x][nextPos.y].equals(Tileset.FLOWER);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard).
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        WORLD = new GameUI(false);

        long seed;
        String seedHolder = "";

        int inputLength = 0;
        boolean isSeed = false;
        boolean hasKey = false;
        int currIndex = 0;

        for (char currChar : input.toLowerCase().toCharArray()) {
            System.out.print(currChar);
            currIndex++;

            if (((currChar == 'n') || (currChar == 'N')) && (inputLength == 0)) {
                isSeed = true;
                inputLength++;
            }
            if ((inputLength > 0) && isSeed && Character.isDigit(currChar)) {
                seedHolder += currChar;
            }
            if (((currChar == 's') || (currChar == 'S')) && isSeed) {
                seed = Long.parseLong(seedHolder);
                WORLD.generate(seed, false);
                break;
            }
        }

        char[] charArray = input.toLowerCase().toCharArray();
        for (int i = currIndex; i < charArray.length; i++) {
            char movementInput = charArray[i];
            System.out.print(movementInput);

            MapGenerator.Position movedPos = null;
            if ((movementInput == 'w')) {
                movedPos = new MapGenerator.Position(avatarPosition.x, avatarPosition.y + 1);
            }
            if ((movementInput == 's')) {
                movedPos = new MapGenerator.Position(avatarPosition.x, avatarPosition.y - 1);
            }
            if ((movementInput == 'a')) {
                movedPos = new MapGenerator.Position(avatarPosition.x - 1, avatarPosition.y);
            }
            if ((movementInput == 'd')) {
                movedPos = new MapGenerator.Position(avatarPosition.x + 1, avatarPosition.y);
            }
            if (movedPos != null) {
                if (isValidTile(movedPos, hasKey)) {
                    if (isKey(movedPos)) {
                        hasKey = true;
                    }
                    if (isLockedDoor(movedPos) && hasKey) {
                        //WORLD.winScreen();
                        break;
                    }
                    WORLD.moveAvatar(movedPos, false);
                    avatarPosition = movedPos;
                }
            }
        }
//        ter.renderFrame(currentWorldFrame);
        return currentWorldFrame;
    }
}
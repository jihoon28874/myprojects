package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class GameUI {
    TERenderer ter;
    long seed;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    Random RANDOM;
    Font font = new Font("Monaco", Font.BOLD, 16);

    public GameUI(boolean doRender) {
        if(doRender) {
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            RANDOM = new Random(seed);
        }
    }

    public void mainMenu() {
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font menuFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "CS61B: Build Your Own World!");
        font = new Font("Monaco", Font.BOLD, 28);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Quit (Q)");
        StdDraw.show();
    }

    public void seedScreen(String currSeed) {
        StdDraw.clear(StdDraw.BLACK);
        Font seedScreenFont = new Font("Monoco", Font.BOLD, 28);
        StdDraw.setFont(seedScreenFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Enter a seed:");
        StdDraw.line(20, HEIGHT / 2, WIDTH - 20, HEIGHT / 2);
        StdDraw.text(WIDTH / 2, HEIGHT - 19, currSeed);
        StdDraw.text(WIDTH / 2, HEIGHT - 30, "Press (s) to start");

        StdDraw.show();
    }

    public void generate(long seed, boolean doRender) {
        TETile[][] curWorldFrame = new TETile[WIDTH][HEIGHT];
        if (doRender) {
            ter.initialize(WIDTH, HEIGHT);
        }

        MapGenerator world = new MapGenerator(seed);
        world.buildWorld(curWorldFrame);
        if (doRender){
            ter.renderFrame(curWorldFrame);
        }
        Engine.currentWorldFrame = curWorldFrame;
    }

    public void moveAvatar(MapGenerator.Position newPos, boolean doMove) {
        MapGenerator.Position currPos = Engine.avatarPosition;
        System.out.println(newPos);
        Engine.currentWorldFrame[newPos.x][newPos.y] = Tileset.AVATAR;
        Engine.currentWorldFrame[currPos.x][currPos.y] = Engine.currentTheme.floor;
        if (doMove){
            ter.renderFrame(Engine.currentWorldFrame);
        }
    }

    public void winScreen() {
        StdDraw.clear(StdDraw.BLACK);
        Font seedScreenFont = new Font("Monoco", Font.BOLD, 28);
        StdDraw.setFont(seedScreenFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Congrats! You won!!");

        StdDraw.show();
    }

    public void encounterScreen() {
        StdDraw.clear(StdDraw.BLACK);
        Font encounterScreenFont = new Font("Monoco", Font.BOLD, 28);
        StdDraw.setFont(encounterScreenFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Collect all the flowers to escape!");
        StdDraw.show();
        StdDraw.pause(2000);

        StdDraw.clear(StdDraw.BLACK);
        TETile[][] encounterWorldFrame = new TETile[WIDTH][HEIGHT];
        MapGenerator world = new MapGenerator(seed);
        world.buildEncounterWorld(encounterWorldFrame);
        ter.renderFrame(encounterWorldFrame);
    }

}
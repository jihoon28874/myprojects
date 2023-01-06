package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Random;

public class MapGenerator {
    public Random RANDOM;
    public static HashMap<Integer, Room> roomTracker = new HashMap<>();

    MapGenerator(long seed) {
        RANDOM = new Random(seed);
    }

    /**
     * Creates a WIDTH by HEIGHT size room at Position p
     *
     * @param tiles     Singular tile in the world.
     * @param p         Coordinate position of the start of the room.
     * @param width     Width size of the room.
     * @param height    Height size of the room.
     * @param currTheme Theme tiles for the floors or walls.
     */
    public void drawRoom(TETile[][] tiles, Position p, int width, int height, Theme currTheme) {
        int roomHeight = 0;
        while (roomHeight < height) {
            if (roomHeight == 0) {
                for (int dx = 0; dx < width; dx++) {
                    tiles[p.x + dx][p.y] = currTheme.wall;
                }
            } else {
                for (int dx = 0; dx < width; dx++) {
                    if (dx == 0) {
                        tiles[p.x + dx][p.y] = currTheme.wall;
                    } else if (dx < width - 1) {
                        tiles[p.x + dx][p.y] = currTheme.floor;
                    } else {
                        tiles[p.x + width - 1][p.y] = currTheme.wall;
                    }
                }
            }
            if (roomHeight == height - 1) {
                for (int dx = 0; dx < width; dx++) {
                    tiles[p.x + dx][p.y] = currTheme.wall;
                }
            }
            roomHeight++;
            p = p.shift(0, 1);
        }
    }

    /**
     * Randomly builds NUM rooms starting at Position p.
     *
     * @param tiles Singular tile in the world.
     */
    public void buildWorld(TETile[][] tiles) {
        Theme currTheme = selectRandomTheme();
        Engine.currentTheme = currTheme;
        Position p = new Position(RANDOM.nextInt(tiles[0].length), RANDOM.nextInt(tiles.length - 10));
        int maxRooms = RANDOM.nextInt(10, 21);

        fillBoardWithNothing(tiles);

        int randomKeyRoom = RANDOM.nextInt(maxRooms);
        int randomExitRoom = RANDOM.nextInt(maxRooms);
        int randomAvatarRoom = RANDOM.nextInt(maxRooms);
        int randomFlowerRoom = RANDOM.nextInt(maxRooms);

        int numRooms = 0;
        while (numRooms < maxRooms) {
            int width = RANDOM.nextInt(5, 10);
            int height = RANDOM.nextInt(5, 10);
            if (p.x + width > Engine.WIDTH || p.y + height > Engine.HEIGHT || !tiles[p.x][p.y].equals(Tileset.NOTHING)) {
                p = new Position(RANDOM.nextInt(Engine.WIDTH), RANDOM.nextInt(33));
            } else {
                Room newRoom = new Room(p, width, height);
                roomTracker.put(numRooms, newRoom);
                System.out.println("Origin coordinates: " + newRoom.p.x + ", " + newRoom.p.y);

                drawRoom(tiles, newRoom.p, newRoom.width, newRoom.height, currTheme);

                numRooms++;
            }
        }
        roomConnector(tiles, roomTracker, currTheme);

        Room keyRoom = roomTracker.get(randomKeyRoom);
        Position randomFloor = randomFloor(keyRoom.p, keyRoom.width, keyRoom.height);
        System.out.println("Key coordinates: " + randomFloor.x + ", " + randomFloor.y);
        tiles[randomFloor.x][randomFloor.y] = Tileset.KEY;

        Room exitRoom = roomTracker.get(randomExitRoom);
        Position randomPerimeter = randomWall(exitRoom.p, exitRoom.width, exitRoom.height);
        while (!tiles[randomPerimeter.x][randomPerimeter.y].equals(currTheme.wall)) {
            randomPerimeter = randomWall(exitRoom.p, exitRoom.width, exitRoom.height);
        }
        System.out.println("Exit coordinates: " + randomPerimeter.x + ", " + randomPerimeter.y);
        tiles[randomPerimeter.x][randomPerimeter.y] = Tileset.LOCKED_DOOR;

        Room avatarRoom = roomTracker.get(randomAvatarRoom);
        Position avatarFloor = randomFloor(avatarRoom.p, avatarRoom.width, avatarRoom.height);
        if (tiles[avatarFloor.x][avatarFloor.y].equals(Tileset.KEY)) {
            avatarFloor = randomFloor(avatarRoom.p, avatarRoom.width, avatarRoom.height);
        }
        System.out.println("Avatar coordinates: " + avatarFloor.x + ", " + avatarFloor.y);
        tiles[avatarFloor.x][avatarFloor.y] = Tileset.AVATAR;
        Engine.avatarPosition = new Position(avatarFloor.x, avatarFloor.y);

        Room flowerRoom = roomTracker.get(randomFlowerRoom);
        Position flowerFloor = randomFloor(flowerRoom.p, flowerRoom.width, flowerRoom.height);
        tiles[flowerFloor.x][flowerFloor.y] = Tileset.FLOWER;
    }


    public void buildEncounterWorld(TETile[][] tiles) {
        Theme currTheme = selectRandomTheme();
        Engine.currentTheme = currTheme;
        Position p = new Position(RANDOM.nextInt(tiles[0].length), RANDOM.nextInt(tiles.length));
        int maxRooms = RANDOM.nextInt(1, 3);

        fillBoardWithNothing(tiles);

        int randomAvatarRoom = RANDOM.nextInt(maxRooms);
        int randomFlowerRoom = RANDOM.nextInt(maxRooms);

        int numRooms = 0;
        while (numRooms < maxRooms) {
            int width = RANDOM.nextInt(5, 10);
            int height = RANDOM.nextInt(5, 10);
            if (p.x + width > Engine.WIDTH || p.y + height > Engine.HEIGHT || !tiles[p.x][p.y].equals(Tileset.NOTHING)) {
                p = new Position(RANDOM.nextInt(Engine.WIDTH), RANDOM.nextInt(35));
            } else {
                Room newRoom = new Room(p, width, height);
                roomTracker.put(numRooms, newRoom);
                System.out.println("Origin coordinates: " + newRoom.p.x + ", " + newRoom.p.y);

                drawRoom(tiles, newRoom.p, newRoom.width, newRoom.height, currTheme);

                numRooms++;
            }
        }
        roomConnector(tiles, roomTracker, currTheme);

        Room avatarRoom = roomTracker.get(randomAvatarRoom);
        Position avatarFloor = randomFloor(avatarRoom.p, avatarRoom.width, avatarRoom.height);
        System.out.println("Avatar coordinates: " + avatarFloor.x + ", " + avatarFloor.y);
        tiles[avatarFloor.x][avatarFloor.y] = Tileset.AVATAR;
        Engine.avatarPosition = new Position(avatarFloor.x, avatarFloor.y);

        Room flowerRoom = roomTracker.get(randomFlowerRoom);
        Position flowerFloor = randomFloor(flowerRoom.p, flowerRoom.width, flowerRoom.height);
        tiles[flowerFloor.x][flowerFloor.y] = Tileset.FLOWER;
    }

    /**
     * Helper method that randomly pick a Position along one of the sides of the Room.
     *
     * @param p      Start Position.
     * @param width  Width size of the room.
     * @param height Height size of the room.
     * @return Randomly chosen coordinate along the perimeters of the Room.
     */
    private Position randomWall(Position p, int width, int height) {
        int wallAxis = RANDOM.nextInt(4);
        if (wallAxis == 0) {
            return new Position(p.x + RANDOM.nextInt(1, width - 2), p.y);
        } else if (wallAxis == 1) {
            return new Position(p.x + RANDOM.nextInt(1, width - 2), p.y + (height - 1));
        } else if (wallAxis == 2) {
            return new Position(p.x, p.y + RANDOM.nextInt(1, height - 2));
        } else {
            return new Position(p.x + (width - 1), p.y + RANDOM.nextInt(1, height - 2));
        }
    }

    /**
     * Randomly get a Position within the room with dimensions width by height.
     *
     * @param p      Origin Position to generate random coordinate from.
     * @param width  Maximum width of the room.
     * @param height Maximum height of the room.
     * @return Random Position within a Room.
     */
    public Position randomFloor(Position p, int width, int height) {
        int rx = RANDOM.nextInt(p.x + 1, p.x + (width - 2));
        int ry = RANDOM.nextInt(p.y + 1, p.y + (height - 2));
        return new Position(rx, ry);
    }

    /**
     * Go through the HashMap mapOfRooms to connect each room together.
     *
     * @param tiles      Singular tile in the world.
     * @param mapOfRooms HashMap holding each Room built.
     * @param currTheme  Theme tiles for the floors or walls.
     */
    public void roomConnector(TETile[][] tiles, HashMap<Integer, Room> mapOfRooms, Theme currTheme) {
        for (int i = 0; i < mapOfRooms.size() - 1; i++) {
            Room firstRoom = mapOfRooms.get(i);
            Room secondRoom = mapOfRooms.get(i + 1);
            Position first = randomFloor(firstRoom.p, firstRoom.width, firstRoom.height);
            System.out.println("First coord " + first.x + ", " + first.y);
            Position second = randomFloor(secondRoom.p, secondRoom.width, secondRoom.height);
            System.out.println("Second coord " + second.x + ", " + second.y);
            hallwayBuilder(tiles, first, second, currTheme);
        }
    }

    /**
     * Check if there is corner at the current Position and then fill it if true.
     *
     * @param tiles     Singular tile in the world.
     * @param p         Current Position that needs to start checking corners.
     * @param currTheme Theme tiles for the floors or walls.
     */
    public void cornerFiller(TETile[][] tiles, Position p, Theme currTheme) {
        if (tiles[p.x + 1][p.y - 1].description().equals("nothing")) {
            tiles[p.x + 1][p.y - 1] = currTheme.wall;
        }
        if (tiles[p.x][p.y - 1].description().equals("nothing")) {
            tiles[p.x][p.y - 1] = currTheme.wall;
        }
        if (tiles[p.x][p.y + 1].description().equals("nothing")) {
            tiles[p.x][p.y + 1] = currTheme.wall;
        }
        if (tiles[p.x + 1][p.y + 1].description().equals("nothing")) {
            tiles[p.x + 1][p.y + 1] = currTheme.wall;
        }
    }

    /**
     * Build hallways based on two random Positions within two Rooms
     *
     * @param tiles     Singular tile in the world.
     * @param first     First random Position in a Room.
     * @param second    Second random Position in a different Room.
     * @param currTheme Theme tiles for the floors or walls.
     */
    public void hallwayBuilder(TETile[][] tiles, Position first, Position second, Theme currTheme) {
        Position currPos;
        if (first.x < second.x) {
            currPos = first;
            for (int g = first.x; g <= second.x; g++) {
                if (tiles[g][first.y + 1].description().equals("nothing")) {
                    tiles[g][first.y + 1] = currTheme.wall;
                }
                tiles[g][first.y] = currTheme.floor;
                if (tiles[g][first.y - 1].description().equals("nothing")) {
                    tiles[g][first.y - 1] = currTheme.wall;
                }
                currPos = currPos.shift(1, 0);
            }
            cornerFiller(tiles, currPos, currTheme);
        } else {
            currPos = second;
            for (int h = second.x; h <= first.x; h++) {
                if (tiles[h][second.y + 1].description().equals("nothing")) {
                    tiles[h][second.y + 1] = currTheme.wall;
                }
                tiles[h][second.y] = currTheme.floor;
                if (tiles[h][second.y - 1].description().equals("nothing")) {
                    tiles[h][second.y - 1] = currTheme.wall;
                }
                currPos = currPos.shift(1, 0);
            }
            cornerFiller(tiles, currPos, currTheme);
        }
        System.out.println("Coord after horizontal " + currPos.x + ", " + currPos.y);
        if (first.y < second.y) {
            for (int i = first.y; i <= second.y; i++) {
                if (tiles[currPos.x - 1][i].description().equals("nothing")) {
                    tiles[currPos.x - 1][i] = currTheme.wall;
                }
                tiles[currPos.x][i] = currTheme.floor;
                if (tiles[currPos.x + 1][i].description().equals("nothing")) {
                    tiles[currPos.x + 1][i] = currTheme.wall;
                }
                currPos = currPos.shift(0, 1);
            }
        } else {
            currPos = new Position(currPos.x, second.y);
            for (int j = second.y; j <= first.y; j++) {
                if (tiles[currPos.x - 1][j].description().equals("nothing")) {
                    tiles[currPos.x - 1][j] = currTheme.wall;
                }
                tiles[currPos.x][j] = currTheme.floor;
                if (tiles[currPos.x + 1][j].description().equals("nothing")) {
                    tiles[currPos.x + 1][j] = currTheme.wall;
                }
                currPos = currPos.shift(0, 1);
            }
        }
        System.out.println("Ending Coord " + currPos.x + ", " + currPos.y);
        System.out.println();
    }

    /**
     * Create a random Theme set.
     *
     * @return Theme tiles for the floors or walls.
     */
    public Theme selectRandomTheme() {
        int randomTheme = RANDOM.nextInt(3);
        return themeSelector(randomTheme);
    }

    /**
     * Helper method that picks a random set of floor and wall tiles for the World's theme.
     *
     * @param randomTheme Random integer to determine the world theme.
     * @return Theme tiles for the floors or walls.
     */
    private Theme themeSelector(int randomTheme) {
        Theme selectedTheme;
        if (randomTheme == 0) {
            TETile wallTheme = Tileset.TREE;
            TETile floorTheme = Tileset.GRASS;
            selectedTheme = new Theme(wallTheme, floorTheme);
            return selectedTheme;
        } else if (randomTheme == 1) {
            TETile wallTheme = Tileset.MOUNTAIN;
            TETile floorTheme = Tileset.SAND;
            selectedTheme = new Theme(wallTheme, floorTheme);
            return selectedTheme;
        } else {
            TETile wallTheme = Tileset.WALL;
            TETile floorTheme = Tileset.FLOOR;
            selectedTheme = new Theme(wallTheme, floorTheme);
            return selectedTheme;
        }
    }

    /**
     * Fills the given 2D array of tiles with blank tiles.
     *
     * @param tiles Singular tile in the world.
     */
    public void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Private helper class to store wall and floor tiles.
     */
    public static class Theme {
        TETile wall;
        TETile floor;

        Theme(TETile wall, TETile floor) {
            this.wall = wall;
            this.floor = floor;
        }
    }

    /**
     * Private helper class to deal with positions.
     */
    public static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shift(int dx, int dy) {
            return new Position(this.x + dx, this.y + dy);
        }
    }

    /**
     * Private helper class to help with managing the attributes of a Room.
     */
    private static class Room {
        Position p;
        int width;
        int height;

        public Room(Position p, int width, int height) {
            this.p = p;
            this.width = width;
            this.height = height;
        }

    }

    /*public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Position anchor = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        buildWorld(world);
        ter.renderFrame(world);
    }*/
}
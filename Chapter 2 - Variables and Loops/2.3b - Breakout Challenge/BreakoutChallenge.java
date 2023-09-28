
public class BreakoutChallenge {
    /* Challenge Goal
     *  In this challenge, you are implementing part of a break-out style game.
     * If you are not familiar with this type of game, check out this wiki page (and a youtube video)...
     *  https://en.wikipedia.org/wiki/Breakout_(video_game)
     *  https://www.youtube.com/watch?v=hW7Sg5pXAok 
     * 
     * The game is partially/mostly implemented for you. You are not writing the game's simulation code.
     *  What you are writing is the graphics (i.e. the display). Everything else is already completed. 
     *  The display should be implemented with ASCII style graphics, print/println...the things you are familiar with. 
     *  You get to pick the display details (like what character is used for the walls, ball, etc). 
     *  Just make it look good, make it your own.
     * 
     * The game has side/top walls, a ball, a paddle at the bottom (controlled with the mouse), and bricks.
     * 
     **********************************************************************************************
     *** THIS IS THE ONLY FILE YOU SHOULD MODIFY FOR THE MAIN/DISPLAY PORTION OF THIS CHALLENGE ***
     **********************************************************************************************
     *  
     * But...if manage to complete this, an additional challenge is to modify the game in some way.
     *  One possibility is to add support for a 'multi-ball' powerup. For this additional challenge, 
     * you can (and should) modify the other files. Be creative, make it fun.
     * 
     */
    private static Simulation sim = new Simulation();

    public static void main(String[] args) {
        do {
            drawFrame();
        } while (sim.isGameActive());
    }

    static final char CEILING_CHAR = '=';
    static final char WALL_CHAR = '|';
    static final char BRICK_CHAR = '@';
    static final char PADDLE_CHAR = '-';
    static final char BALL_CHAR = 'o';
    static final char EMPTY_CHAR = ' ';
    static final int GRID_SIZE = 2;

    static void drawFrame() {
        int gridOffsetX, gridOffsetY;
        for (int y = Simulation.GRID_HEIGHT * GRID_SIZE; y >= 0; y--) {
            gridOffsetY = y % GRID_SIZE;
            y /= GRID_SIZE;

            System.out.print(WALL_CHAR);
            for (int x = 0; x < Simulation.GRID_WIDTH * GRID_SIZE; x++) {
                gridOffsetX = x % GRID_SIZE;
                x /= GRID_SIZE;

                if (y == Simulation.GRID_HEIGHT) {
                    System.out.print(CEILING_CHAR);
                }
                else if (sim.isBrickInGridSquare(x, y)) {
                    System.out.print(BRICK_CHAR);
                }
                else if (sim.isBallInGridSquare(x, y)) {
                    System.out.print(BALL_CHAR);
                }
                else if (sim.isPaddleInGridSquare(x, y)) {
                    System.out.print(PADDLE_CHAR);
                }
                else {
                    System.out.print(EMPTY_CHAR);
                }

                x = (x * GRID_SIZE) + gridOffsetX;
            }
            System.out.print(WALL_CHAR);

            System.out.println();
            y = (y * GRID_SIZE) + gridOffsetY;
        }
        sim.endFrame();
    }
}

package rapid.widget;

import rapid.exp.LevelNotFound;
import rapid.Env;

import java.io.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :      BMMap
 * Version :    ver 0.1
 * Usage :      The map object that stores all game info
 */

public class BMMap implements Serializable {
    private int width, height;      // width and height of map
    private BMWidget map[][];       // map
    private BMPerson person;        // bot
    private BMTargetBlock target_blocks[];  // target blocks
    private BMBox boxes[];                  // boxes

    // back steps stacks
    private int push_stack_dir[] = new int[1000];       // push direction
    private int push_stack_cur_x[] = new int[1000];     // pos x
    private int push_stack_cur_y[] = new int[1000];     // pos y
    private int n_push_stack = 0;   // the stack size


    // shared private queue
    static private int
        queue_x[] = new int[100],
        queue_y[] = new int[100];
    static private boolean vis[][] = new boolean[100][100];

    private int cur_level; // current level
    private int cur_step;  // current step

    /**
     * the Current level
     * @return current level
     */
    public int getLevel() {
        return cur_level;
    }

    /**
     * Load map specified by level
     * @param level the level to load
     * @throws LevelNotFound
     */
    public void loadLevel(int level)
    throws LevelNotFound {
        // reset variables
        cur_step = 0;
        n_push_stack = 0;
        cur_level = level;

        // use IOHelper to read map
        IOHelper ioh = new IOHelper();
        ioh.readMap(level);


        // get width and height
        width = ioh.getWidth();
        height = ioh.getHeight();

        // create a new map
        map = new BMWidget[height][width];

        // Temporary lists
        ArrayList target_block_arr = new ArrayList();
        ArrayList box_arr = new ArrayList();

        // for each widgets in original map
        for(int i = 0;i < height;i ++) {
            for(int j = 0;j < width;j ++){
                BMWidget cur = null;    // Current widget
                BMBox box;      // sometimes we need a second widget
                int cval = ioh.getMapAt(i, j);
                switch (cval) {
                    case Env.PLAYER_FACE_EAST:
                    case Env.PLAYER_FACE_NORTH:
                    case Env.PLAYER_FACE_SOUTH:
                    case Env.PLAYER_FACE_WEST:      // Create Player
                        // The player is in a empty block
                        cur = new BMEmptyBlock(i, j);
                        person = new BMPerson(i, j, cval);
                        ((BMContainer)cur).putIn(person);
                        break;
                    case Env.BLOCK_NUM_BOX:     // Create Box
                        // The box is in a empty block
                        cur = new BMEmptyBlock(i, j);
                        box = new BMBox(i, j);
                        ((BMContainer)cur).putIn(box);
                        box_arr.add(box);
                        break;
                    case Env.BLOCK_NUM_EMPTY:  // Create Empty Block
                        cur = new BMEmptyBlock(i, j);
                        break;
                    case Env.BLOCK_NUM_TARGET: // Create a null target
                        cur = new BMTargetBlock(i, j);
                        target_block_arr.add(cur);
                        break;
                    case Env.BLOCK_NUM_FILLED_TARGET: // a filled target
                        cur = new BMTargetBlock(i, j);
                        target_block_arr.add(cur);
                        box = new BMBox(i, j);
                        ((BMContainer)cur).putIn(box);
                        box_arr.add(box);
                        break;
                    case Env.BLOCK_NUM_WALL:    // a wall
                        cur = new BMWall(i, j);
                        break;
                    case Env.BLOCK_NUM_NULL: // null widget outside the wall
                        cur = new BMNull(i, j);
                        break;
                } // switch

                map[i][j] = cur;    // put widget into map
            } // for j
        } // for i

        // put widget from list to array
        target_blocks = new BMTargetBlock[target_block_arr.size()];
        for(int i = 0;i < target_block_arr.size();i ++) {
            target_blocks[i] = (BMTargetBlock)target_block_arr.get(i);
        }

        boxes = new BMBox[box_arr.size()];
        for(int i = 0;i < box_arr.size();i ++) {
            boxes[i] = (BMBox)box_arr.get(i);
        }
    }

    /**
     * get the Widget in the map
     * @param i pos x
     * @param j pos y
     * @return the widget at [i,j]
     */
    public BMWidget getWidgetAt(int i,int j) {
        return map[i][j];
    }

    /**
     * get the width of map
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * get the height of map
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * try to move the bot to specified direction
     * @param dir direction to move, Env.DIRECTION_*
     */
    public void movePerson(int dir) {
        int next_pos[] = person.move(dir);
        int nx = next_pos[0],   // next x
            ny = next_pos[1];   // next y

        if(isOutOfBoundary(nx, ny)) {
            return;
        }

        if(!(getWidgetAt(nx, ny) instanceof BMContainer)) {
            return;
        }

        BMContainer next = (BMContainer)getWidgetAt(nx, ny),    // next container
                cur = (BMContainer)getWidgetAt(person.getX(), person.getY());   // current container

        if(next.isPassable()) { // can pass, move directly
            next.putIn(person);
            cur.putOut();
            cur_step ++;
        } else if(next.getInner().getTypeId() == BMWidget.BLOCK_TYPE_BOX) { // can't pass and is a box
            BMBox box = (BMBox)next.getInner();
            next_pos = box.move(dir);
            int box_nx = next_pos[0],
                box_ny = next_pos[1];

            if(!isOutOfBoundary(box_nx, box_ny) && getWidgetAt(box_nx, box_ny).isPassable()) { // the box is pushable
                ((BMContainer)getWidgetAt(box_nx, box_ny)).putIn(box);
                next.putIn(person);
                cur.putOut();

                cur_step ++;
                /* This is a push operation*/
                push_stack_dir[n_push_stack] = dir;
                push_stack_cur_x[n_push_stack] = box.getX();
                push_stack_cur_y[n_push_stack ++] = box.getY();
            }
        }
    }

    /**
     * is position [x, y] out of boundary?
     * @param x pos x
     * @param y pos y
     * @return true or false
     */
    private boolean isOutOfBoundary(int x, int y) {
        return  x < 0 || x >= height || y < 0 || y >= width;
    }

    /**
     * the box can push on the direction?
     * @param box the box to push
     * @param dir the direction push to
     * @return
     */
    private boolean canPushBoxToDirection(BMBox box,int dir) {
        int pos[] = box.move(dir);

        return getWidgetAt(pos[0], pos[1]).isPassable() ||
            (pos[0] == person.getX() && pos[1] == person.getY());
    }

    /**
     * is this box pushable?
     * @param box box to push
     * @return
     */
    private boolean isBoxMovable(BMBox box) {
        if((
            canPushBoxToDirection(box, BMMovable.DIRECTION_LEFT) &&
            canPushBoxToDirection(box, BMMovable.DIRECTION_RIGHT)
        ) || (
            canPushBoxToDirection(box, BMMovable.DIRECTION_UP) &&
            canPushBoxToDirection(box, BMMovable.DIRECTION_DOWN)
        )) {
            return true;
        }

        return false;
    }

    /**
     * Dead stage B
     * @return
     */
    private boolean checkFailedB() {
        // reset vis array
        for(int i = 0;i < height;i ++)
            for(int j = 0;j < width;j++)
                vis[i][j] = false;

        // reset queue
        int head = 0, tail = 0;

        // push start position int queue
        queue_x[tail] = person.getX();
        queue_y[tail ++] = person.getY();
        vis[person.getX()][person.getY()] = true;

        // bfs
        while(head < tail) {
            int x = queue_x[head],
                y = queue_y[head];
            head ++;    // dequeue

            // expand it
            for(int i = 0;i < BMMovable.DIRECTIONS.length;i ++) {
                int pos[] = BMMovable.moveToDirection(x, y, BMMovable.DIRECTIONS[i]);
                int cx = pos[0],
                    cy = pos[1];

                if(!isOutOfBoundary(cx, cy) && !vis[cx][cy]) {
                    if(getWidgetAt(cx, cy).isPassable()) { // can directly move to
                        vis[cx][cy] = true;
                        queue_x[tail] = cx;
                        queue_y[tail ++] = cy;
                    } else if(getWidgetAt(cx, cy) instanceof BMContainer) {
                        BMContainer con = (BMContainer)getWidgetAt(cx, cy);
                        if(con.getInner().getTypeId() == BMWidget.BLOCK_TYPE_BOX) {  // or we found a box
                            BMBox box = (BMBox)con.getInner();

                            if(isBoxMovable(box)) { // if this box is movable, then this is not DeaD stage B
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * is Dead Stage A or Dead Stage B?
     * @return
     */
    public boolean isFailed() {
        /* Failed type A */

        boolean flag_a = true;
        for(int i = 0;i < boxes.length;i ++) {
            BMBox box = boxes[i];
            if(isBoxMovable(box)) {
                flag_a = false;
                break;
            }
        }

        if(flag_a) {
            return true;
        }

        /* Failed type B */
        if(checkFailedB()) {
            return true;
        }

        return false;
    }

    /**
     * have we won?
     * @return
     */
    public boolean isWon() {
        for(int i = 0;i < target_blocks.length;i ++) {
            if(!target_blocks[i].isFilled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * back steps
     * @param step step to back
     */
    public void moveBack(int step) {
        cur_step ++;    // back steps is equal to one operation
        if(step < 1) step = 1;
        if(step > n_push_stack || n_push_stack == 0) {  // this guy is trying to restart game
            try {
                int t = cur_step;
                loadLevel(cur_level);
                cur_step = t;
            } catch (LevelNotFound e) {

            }
            return;
        }

        // step back one by one
        for(int i = 0;i < step;i ++,n_push_stack --) {
            int dir = push_stack_dir[n_push_stack - 1],
                x = push_stack_cur_x[n_push_stack - 1],
                y = push_stack_cur_y[n_push_stack - 1];

            int pos[];
            BMContainer con1;

            con1 = (BMContainer)getWidgetAt(x, y);
            BMBox box = (BMBox)con1.getInner();
            con1.putOut();

            // Move person to attach the box
            con1 = (BMContainer)getWidgetAt(person.getX(), person.getY());
            con1.putOut();

            pos = box.moveBack(dir);
            person.moveTo(pos[0], pos[1]);
            pos = person.moveBack(dir);
            ((BMContainer)getWidgetAt(pos[0], pos[1])).putIn(person);

            // Move the box
            pos = box.moveBack(dir);
            ((BMContainer)getWidgetAt(pos[0], pos[1])).putIn(box);
        }
    }

    /**
     * get cur steps
     * @return cur steps
     */
    public int getCurrentStep() {
        return cur_step;
    }
}

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class : IOHelper
 * Version : 0.1
 * Usage :  Read the map file and give some utils to handle the map
 *          If need help please refer to Developer Manual
 */

class IOHelper {
    private int width, height;
    private char[][] map;

    public void readMap(int level)
        throws LevelNotFound {
        try {
            File map_file = new File(Env.MAP_DIRECTORY + level + ".map");
            Scanner input = new Scanner(map_file);

            map = new char[30][30];
            width = height = 0;

            while(input.hasNext()) {
                map[height] = input.nextLine().toCharArray();
                height ++;
            }
            width = map[0].length;
        } catch (FileNotFoundException e) {
            throw new LevelNotFound(level);
        }
    }

    public int getMapAt(int x, int y) {
        return map[x][y] - '0';
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
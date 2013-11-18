package bin.widget;

import bin.exp.LevelNotFound;
import bin.io.IOHelper;
import bin.Env;

import java.io.*;
import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class BMMap implements Serializable {
    private int width, height;
    private BMWidget map[][];
    private BMPerson person;
    private BMTargetBlock target_blocks[];
    private BMBox boxes[];
    private int push_stack_dir[] = new int[1000];
    private int push_stack_cur_x[] = new int[1000];
    private int push_stack_cur_y[] = new int[1000];
    private int n_push_stack = 0;


    static private int
        queue_x[] = new int[100],
        queue_y[] = new int[100];

    static private boolean vis[][] = new boolean[100][100];

    private int cur_level;
    private int cur_step;

    public int getLevel() {
        return cur_level;
    }

    public void loadLevel(int level)
    throws LevelNotFound {
        cur_step = 0;
        n_push_stack = 0;
        cur_level = level;

        IOHelper ioh = new IOHelper();
        ioh.readMap(level);

        width = ioh.getWidth();
        height = ioh.getHeight();

        map = new BMWidget[height][width];

        ArrayList target_block_arr = new ArrayList();
        ArrayList box_arr = new ArrayList();

        for(int i = 0;i < height;i ++) {
            for(int j = 0;j < width;j ++){
                BMWidget cur = null;
                BMBox box;
                int cval = ioh.getMapAt(i, j);
                switch (cval) {
                    case Env.PLAYER_FACE_EAST:
                    case Env.PLAYER_FACE_NORTH:
                    case Env.PLAYER_FACE_SOUTH:
                    case Env.PLAYER_FACE_WEST:
                        cur = new BMEmptyBlock(i, j);
                        person = new BMPerson(i, j, cval);
                        ((BMContainer)cur).putIn(person);
                        break;
                    case Env.BLOCK_NUM_BOX:
                        cur = new BMEmptyBlock(i, j);
                        box = new BMBox(i, j);
                        ((BMContainer)cur).putIn(box);
                        box_arr.add(box);
                        break;
                    case Env.BLOCK_NUM_EMPTY:
                        cur = new BMEmptyBlock(i, j);
                        break;
                    case Env.BLOCK_NUM_TARGET:
                        cur = new BMTargetBlock(i, j);
                        target_block_arr.add(cur);
                        break;
                    case Env.BLOCK_NUM_FILLED_TARGET:
                        cur = new BMTargetBlock(i, j);
                        target_block_arr.add(cur);
                        box = new BMBox(i, j);
                        ((BMContainer)cur).putIn(box);
                        box_arr.add(box);
                        break;
                    case Env.BLOCK_NUM_WALL:
                        cur = new BMWall(i, j);
                        break;
                    case Env.BLOCK_NUM_NULL:
                        cur = new BMNull(i, j);
                        break;
                } // switch

                map[i][j] = cur;
            } // for j
        } // for i

        target_blocks = new BMTargetBlock[target_block_arr.size()];
        for(int i = 0;i < target_block_arr.size();i ++) {
            target_blocks[i] = (BMTargetBlock)target_block_arr.get(i);
        }

        boxes = new BMBox[box_arr.size()];
        for(int i = 0;i < box_arr.size();i ++) {
            boxes[i] = (BMBox)box_arr.get(i);
        }
    }

    public void saveGame() {
        try {
            FileOutputStream fs = new FileOutputStream(Env.getSaveFileName());
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String[] getSaveList() {
        File dir = new File(Env.SAVE_DIR);
        File files[] = dir.listFiles();
        ArrayList t = new ArrayList();
        for(int i = 0;i < files.length;i ++) {
            if(files[i].isFile() && files[i].getName().endsWith(".save")) {
                t.add(new String(files[i].getName()));
            }
        }
        String ret[] = new String[t.size()];
        for(int i = 0;i < t.size();i ++)
            ret[i] = (String)t.get(i);
        return ret;
    }

    public static BMMap readGame(String save_name) {
        BMMap map = null;
        try {
            FileInputStream fs = new FileInputStream(Env.SAVE_DIR + "/" + save_name);
            ObjectInputStream os = new ObjectInputStream(fs);
            map = (BMMap)os.readObject();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public BMWidget getWidgetAt(int i,int j) {
        return map[i][j];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void movePerson(int dir) {
        int next_pos[] = person.move(dir);
        int nx = next_pos[0],
            ny = next_pos[1];

        if(isOutOfBoundary(nx, ny)) {
            return;
        }

        if(!(getWidgetAt(nx, ny) instanceof BMContainer)) {
            return;
        }

        BMContainer next = (BMContainer)getWidgetAt(nx, ny),
                cur = (BMContainer)getWidgetAt(person.getX(), person.getY());

        if(next.isPassable()) {
            next.putIn(person);
            cur.putOut();
            cur_step ++;
        } else if(next.getInner().getTypeId() == Env.BLOCK_TYPE_BOX) {
            BMBox box = (BMBox)next.getInner();
            next_pos = box.move(dir);
            int box_nx = next_pos[0],
                box_ny = next_pos[1];

            if(!isOutOfBoundary(box_nx, box_ny) && getWidgetAt(box_nx, box_ny).isPassable()) {
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

    private boolean isOutOfBoundary(int x, int y) {
        return  x < 0 || x >= height || y < 0 || y >= width;
    }

    private boolean canPushBoxToDirection(BMBox box,int dir) {
        int pos[] = box.move(dir);

        if(getWidgetAt(pos[0], pos[1]).isPassable() ||
        (pos[0] == person.getX() && pos[1] == person.getY())) {
            return true;
        }
        return false;
    }

    private boolean isBoxMovable(BMBox box) {
        if((
            canPushBoxToDirection(box, Env.DIRECTION_LEFT) &&
            canPushBoxToDirection(box, Env.DIRECTION_RIGHT)
        ) || (
            canPushBoxToDirection(box, Env.DIRECTION_UP) &&
            canPushBoxToDirection(box, Env.DIRECTION_DOWN)
        )) {
            return true;
        }

        return false;
    }

    private boolean checkFailedB() {
        for(int i = 0;i < height;i ++)
            for(int j = 0;j < width;j++)
                vis[i][j] = false;

        int head = 0, tail = 0;

        queue_x[tail] = person.getX();
        queue_y[tail ++] = person.getY();
        vis[person.getX()][person.getY()] = true;

        while(head < tail) {
            int x = queue_x[head],
                y = queue_y[head];
            head ++;

            for(int i = 0;i < Env.DIRECTIONS.length;i ++) {
                int pos[] = BMMovable.moveToDirection(x, y, Env.DIRECTIONS[i]);
                int cx = pos[0],
                    cy = pos[1];

                if(!isOutOfBoundary(cx, cy) && !vis[cx][cy]) {
                    if(getWidgetAt(cx, cy).isPassable()) {
                        vis[cx][cy] = true;
                        queue_x[tail] = cx;
                        queue_y[tail ++] = cy;
                    } else if(getWidgetAt(cx, cy) instanceof BMContainer) {
                        BMContainer con = (BMContainer)getWidgetAt(cx, cy);
                        if(con.getInner().getTypeId() == Env.BLOCK_TYPE_BOX) {
                            BMBox box = (BMBox)con.getInner();

                            if(isBoxMovable(box)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

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

    public boolean isWon() {
        for(int i = 0;i < target_blocks.length;i ++) {
            if(!target_blocks[i].isFilled()) {
                return false;
            }
        }
        return true;
    }

    public void moveBack(int step) {
        cur_step ++;
        if(step < 1) step = 1;
        if(step > n_push_stack || n_push_stack == 0) {
            try {
                int t = cur_step;
                loadLevel(cur_level);
                cur_step = t;
            } catch (LevelNotFound e) {

            }
            return;
        }

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

    public int getCurrentStep() {
        return cur_step;
    }
}

package graph;

import java.util.ArrayList;

public class PathList extends ArrayList<Integer> {
    public PathList copy() {
        PathList co = new PathList();
        for(Integer i: this)
            co.add(i);
        return co;
    }

    public void appendPath(PathList path) {
        for(Integer i: path)
            this.add(i);
    }
}

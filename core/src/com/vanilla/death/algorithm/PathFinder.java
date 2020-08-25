package com.vanilla.death.algorithm;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;

/**
 * Created by mrkotyk on 16.08.17.
 */
public class PathFinder {

    private int length;
    private Hex[][] hexes;

    private static PathFinder object;

    public static PathFinder getObject(){
        if(object==null){
            object = new PathFinder();
        }
        return object;
    }

    private int runID = 0;
    private int targetX = 0;
    private int targetY = 0;

    private BinaryHeap<PathNode> open;
    private PathNode[] nodes;
    private IntArray path;

    private PathFinder(){
        length = GameWorld.getInstance().length;
        hexes = GameWorld.getInstance().getHexes();
        open = new BinaryHeap<PathNode>(length * 4, false);
        nodes = new PathNode[length * length];
        path = new IntArray();
    }

    public IntArray pathFind(int startX, int startY, int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;

        path.clear();
        open.clear();

        runID++;
        if (runID < 0) runID = 1;


        //создаем корень пути в индексе из начальной координаты
        int root_index = startY * length + startX;
        PathNode root = nodes[root_index];
        if (root == null) {
            root = new PathNode(0.0f);
            root.setX(startX);
            root.setY(startY);
            nodes[root_index] = root;
        }


        root.setParent(null);
        root.pathCost = 0;

        open.add(root, 0.0f);

        while (open.size > 0) {
            PathNode node = open.pop();
            if (node.getX() == targetX && node.getY() == targetY) {
                while (node != root) {
                    path.add(node.getX());
                    path.add(node.getY());
                    node = node.getParent();
                }
                break;
            }
            node.closeID = runID;
            int x = node.x;
            int y = node.y;

            for(int i = x-1; i <= x+1; i++){
                for(int j = y-1; j <= y+1; j++){

                    if((i!=x || j!=y) && GameWorld.getInstance().inArray(i,j) && hexes[i][j].getGround() != null){

                        switch(x%2){
                            case 0:
                                if (!(i == x + 1 && j == y - 1) && !(i == x - 1 && j == y - 1)) {
                                    addNode(node, i,j,1);
                                }
                                break;
                            case 1:
                                if (!(i == x + 1 && j == y + 1) && !(i == x - 1 && j == y + 1)) {
                                    addNode(node, i,j,1);
                                }
                                break;
                        }
                    }

                }
            }


        }

        return path;
    }

    private void addNode(PathNode parent, int x, int y, int cost){
        int pathCost = parent.pathCost + cost;
        float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);

        int index = y * length + x;
        PathNode node = nodes[index];
        if (node != null && node.runID == runID){
            if (node.getCloseID() != runID && pathCost < node.pathCost) {
                open.setValue(node, score);
                node.parent = parent;
                node.pathCost = pathCost;
            }
        } else {
            if (node == null){
                node = new PathNode(0.0f);
                node.setX(x);
                node.setY(y);
                nodes[index] = node;
            }
            open.add(node, score);
            node.setRunID(runID);
            node.setParent(parent);
            node.setPathCost(pathCost);
        }
    }

//    private boolean isValid(int x, int y) {
//        if (){
//            //Data.map[x][y].blocked(unit as Cell)
//            return false;
//        }
//        return true;
//    }

    private class PathNode extends BinaryHeap.Node {
        public PathNode(float value) {
            super(value);
        }

        private int runID, closeID;
        private int x, y;
        private int pathCost;
        private PathNode parent;

        public int getRunID() {
            return runID;
        }
        public void setRunID(int runID) {
            this.runID = runID;
        }
        public int getCloseID() {
            return closeID;
        }
        public void setCloseID(int closeID) {
            this.closeID = closeID;
        }
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public int getPathCost() {
            return pathCost;
        }
        public void setPathCost(int pathCost) {
            this.pathCost = pathCost;
        }
        public PathNode getParent() {
            return parent;
        }
        public void setParent(PathNode parent) {
            this.parent = parent;
        }
    }

    public void dispose(){
        hexes = null;
        open = null;
        nodes = null;
        path = null;
        object = null;
    }
}

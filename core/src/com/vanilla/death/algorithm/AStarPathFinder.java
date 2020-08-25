package com.vanilla.death.algorithm;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;
import com.vanilla.death.model.GameWorld;

/**
 * Created by hedgehog on 13.08.17.
 */
public class AStarPathFinder {

    int width = 0;
    int height = 0;

    int runID = 0;
    private int targetX = 0;
    private int targetY = 0;

    BinaryHeap<PathNode> open = new BinaryHeap<PathNode>(width * 4, false);
    PathNode[] nodes = new PathNode[width * height];

    private IntArray path = new IntArray();

    //TODO UnitType?
    public IntArray pathFind(int startX, int startY, int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;

        path.clear();
        open.clear();

        runID++;

        if (runID < 0) runID = 1;

        int index = startY * width + startX;

        PathNode root = nodes[index];
        if (root == null) {
            root = new PathNode(0.0f);
            root.setX(startX);
            root.setY(startY);

            nodes[index] = root;
        }

        root.setParent(null);
        root.pathCost = 0;
        open.add(root, 0.0f);

        int lastColumn = width - 1;
        int lastRow = height - 1;

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

            if (x < lastColumn){

                addNode(node, x + 1, y, 10);
                if (y < lastRow)
                    addNode(node, x + 1, y + 1, 14);
                if (y > 0)
                    addNode(node, x + 1, y - 1, 14);
            }
            if (x > 0) {
                addNode(node, x - 1, y, 10);
                if (y < lastRow)
                    addNode(node, x - 1, y + 1, 14);
                if (y > 0)
                    addNode(node, x - 1, y - 1, 14);
            }

            if (y < lastRow)
                addNode(node, x, y + 1, 10);
            if (y > 0)
                addNode(node, x, y - 1, 10);
        }

        return path;
    }


    private void addNode(PathNode parent, int x, int y, int cost){

        if (!isValid(x, y)) return;

        int pathCost = parent.pathCost + cost;
        float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);

        int index = y * width + x;
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

    private boolean isValid(int x, int y) {
//        if (Data.map[x][y].blocked(unit as Cell)){
//            return false
//        }
        return true;
    }

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
}

package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Node {

    public int value;
    public Point coord;
    public ArrayList<Node> position;
    public Node parent;

    public Node(Point coord, int value, Node parent) {
        this.value = value;
        this.coord = coord;
        this.position = new ArrayList<>();
        this.parent = parent;
    }

    public void addPosition (Node p){
        position.add(p);
    }
}

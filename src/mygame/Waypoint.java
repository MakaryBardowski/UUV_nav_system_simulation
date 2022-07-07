/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public class Waypoint {
    private Node node;
    private float x;
    private float y;
    private float z;
    private boolean isObstacle;
    
    private Waypoint parent;

    public Waypoint(){}
    
    public Waypoint(float x, float y, float z, Waypoint parent) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    

    public boolean getIsObstacle() {
        return isObstacle;
    }

    public void setIsObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Waypoint getParent() {
        return parent;
    }

    public void setParent(Waypoint parent) {
        this.parent = parent;
    }
    
    
    
    
}

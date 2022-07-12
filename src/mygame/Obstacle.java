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
public class Obstacle {
    private Vector3 worldLocation; // location
    private float radius; // dimensions x y
    private int[][] indices;
    
    
    private Node node; // only for debugging purposes.
    
    public Obstacle(Vector3 worldLocation, float radius) {
        this.worldLocation = worldLocation;
        this.radius = radius;
        indices = new int[2][2];
    }
    
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Vector3 getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(Vector3 worldLocation) {
        this.worldLocation = worldLocation;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int[][] getIndices() {
        return indices;
    }

//    public void setIndices(float[] indices) {
//        this.indices = indices;
//    }
    
    
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author 48793
 */
public class Obstacle {
    private Vector3 worldLocation;
    private float radius;

    public Obstacle(Vector3 worldLocation, float radius) {
        this.worldLocation = worldLocation;
        this.radius = radius;
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
    
    
    
}

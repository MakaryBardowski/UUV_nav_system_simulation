/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;

/**
 *
 * @author Makary Bardowski
 */
public class Waypoint {
    private Node node;
    private Vector3 worldLocation;
    private boolean isObstacle;
    private Waypoint parent;

    public Waypoint(){}
    
    public Waypoint(float x, float y, float z, Waypoint parent) {
        worldLocation = new Vector3(x,y,z);
        this.parent = parent;
    }
    
    public Waypoint(Vector3 worldLocation, Waypoint parent){
    this.worldLocation = new Vector3(worldLocation.getX(),worldLocation.getY(),worldLocation.getZ());
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

    
    
    public void setWorldLocation(Vector3 worldLocation){
    this.worldLocation = worldLocation;
    }
    
    public Vector3 getWorldLocation(){
    return worldLocation;
    }

    public Waypoint getParent() {
        return parent;
    }

    public void setParent(Waypoint parent) {
        this.parent = parent;
    }
    
    
       public  float distance(Waypoint w2){
        
     return (float) Math.sqrt((worldLocation.getZ()-w2.getWorldLocation().getZ()) *(worldLocation.getZ()-w2.getWorldLocation().getZ())  +  (worldLocation.getX()-w2.getWorldLocation().getX())*(worldLocation.getX()-w2.getWorldLocation().getX()));
        
    }
    
}

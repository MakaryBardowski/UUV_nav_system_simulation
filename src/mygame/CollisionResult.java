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
public class CollisionResult {
    private final boolean collisionOccured;
    private final Vector3 worldLocation;
    public static final CollisionResult NO_COLLISION = new CollisionResult(false,Vector3.nonExistent);
    
    CollisionResult(boolean collisionOccured,Vector3 worldLocation){
    this.collisionOccured = collisionOccured;
    this.worldLocation = worldLocation;
    }

    public boolean getCollisionOccured() {
        return collisionOccured;
    }

    public Vector3 getWorldLocation() {
        return worldLocation;
    }
    
    
    
}

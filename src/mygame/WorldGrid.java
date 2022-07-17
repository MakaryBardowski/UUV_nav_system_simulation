/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import static com.jme3.scene.plugins.fbx.mesh.FbxLayerElement.Type.Texture;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static mygame.Main.DELTA_STEP;
import static mygame.Main.debugNode;
import static mygame.Main.publicAssetManager;

/**
 *
 * @author 48793
 */
public class WorldGrid {
 
    private int cellSize;
    private HashMap<String,ArrayList<Obstacle>> contents;
    
    
    public WorldGrid(int cellSize){
    this.cellSize = cellSize;
    contents = new HashMap();
    }
    
    public String hash(Vector3 point){
    int[] loc = new int[2];
    loc[0] = cellSize*(int)(Math.floor(point.getX()/cellSize));


    loc[1] = cellSize*(int)(Math.floor(point.getZ()/cellSize));

    return loc[0]+"."+loc[1];

    }
    
    
    public void insert(Obstacle object){
        
       Vector3 min = new Vector3(
            object.getWorldLocation().getX() - (object.getRadius()),
             object.getWorldLocation().getZ() - (object.getRadius()));   

        Vector3 max = new Vector3(
            object.getWorldLocation().getX() + (object.getRadius()),
             object.getWorldLocation().getZ() + (object.getRadius()));   
        
        
        
//        if(  contents.get( hash(object.getWorldLocation()))  == null  ){
//            contents.put( hash(object.getWorldLocation()) , new ArrayList<Obstacle>());
//        }else{
//            contents.get( hash(object.getWorldLocation())).add(object);
//
//        }

       if(  contents.get( hash(min))  == null  ){
            contents.put( hash(min) , new ArrayList<Obstacle>());
        }else{
            contents.get( hash(min)).add(object);

        }
       
       if(  contents.get( hash(max))  == null  ){
            contents.put( hash(max) , new ArrayList<Obstacle>());
        }else{
            contents.get( hash(max)).add(object);

        }
       
     Vector3 newMin = new Vector3(min.getX(),max.getZ());
       
       if(  contents.get( hash(newMin))  == null  ){
            contents.put( hash(newMin) , new ArrayList<Obstacle>());
        }else{
            contents.get( hash(newMin)).add(object);

        }
       
              newMin = new Vector3(max.getX(),min.getZ());

       
       if(  contents.get( hash(newMin))  == null  ){
            contents.put( hash(newMin) , new ArrayList<Obstacle>());
        }else{
            contents.get( hash(newMin)).add(object);

        }
       


  
    }
    
    public List<Obstacle> getNearby(Obstacle object){
        debugNode.detachAllChildren();
        SonarReader.addHitboxIndicator(debugNode, object.getRadius(),new Vector3f ( object.getWorldLocation().getX(),0 , object.getWorldLocation().getZ()  )     );
        List<Obstacle> output = new ArrayList<>();
        
        
         Vector3 min = new Vector3(
            object.getWorldLocation().getX() - (object.getRadius()),
             object.getWorldLocation().getZ() - (object.getRadius()));   

        Vector3 max = new Vector3(
            object.getWorldLocation().getX() + (object.getRadius()),
             object.getWorldLocation().getZ() + (object.getRadius()));   
        
        
        addDebugTile(max);
         addDebugTile(min);
         addDebugTile(object.getWorldLocation());

          
        
        
        if(contents.get( hash(object.getWorldLocation())) != null){
            
//            String[] parts = hash(object.getWorldLocation()).split(".");             
//            for(Obstacle detectedObstacle : contents.get( hash(object.getWorldLocation())) ){
////            System.out.println("dodaje");
//        RRTalgorithm.addBox(0.1f, 0.3f, 0.1f, ColorRGBA.Green, detectedObstacle.getNode());
//        }


    
    output.addAll( contents.get( hash(object.getWorldLocation())) );
        }
        
        
        
        if(contents.get( hash(min)) != null){
            output.addAll( contents.get( hash(min)) );
        }
        
           if(contents.get( hash(max)) != null){
            output.addAll( contents.get( hash(max)) );
        }
           
            Vector3 newMin = new Vector3(min.getX(),max.getZ());
                    addDebugTile(newMin);

            
             if(contents.get( hash(newMin)) != null){
            output.addAll( contents.get( hash(newMin)) );
                }

              newMin = new Vector3(max.getX(),min.getZ());
                    addDebugTile(newMin);

         if(contents.get( hash(newMin)) != null){
            output.addAll( contents.get( hash(newMin)) );
                }
        
        
         
         
        return output;
    }
    
    // debug methods
    
    private  void addDebugTile(Vector3 pos){
          Material allowedMaterial = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    allowedMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
    allowedMaterial.setColor("Color", new ColorRGBA(0, 1, 0, .4f));

    Node node = (Node) publicAssetManager.loadModel("Models/accessiblePlane/accessiblePlane.j3o");
    node.setMaterial(allowedMaterial);
    debugNode.attachChild(node);
    node.scale(cellSize);
    String[] parts =  hash(pos).split("\\.");
    
    node.move(Integer.parseInt(parts[0])+cellSize/2,0,Integer.parseInt(parts[1])+cellSize/2);
    }
    
    
    
    
    
}

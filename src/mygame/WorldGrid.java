/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import static com.jme3.scene.plugins.fbx.mesh.FbxLayerElement.Type.Texture;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import static mygame.Main.publicAssetManager;

/**
 *
 * @author 48793
 */
public class WorldGrid {
    private int cols;
    private int rows;
    private int cellSize;
    private HashMap<Integer, ArrayList<Obstacle>> buckets;
    private int sceneWidth;
    private int sceneHeight;
    
    
    void setup(int sceneWidth,int sceneHeight, int cellSize){

    cols= sceneWidth / cellSize;
    rows= sceneHeight / cellSize;
    buckets = new HashMap<Integer, ArrayList<Obstacle>>();

    for (int i = -sceneHeight; i < (cols*rows)-sceneHeight; i++)
    {
        buckets.put(i, new ArrayList<Obstacle>());
    }

    this.sceneWidth = sceneWidth;
    this.sceneHeight = sceneHeight;
    this.cellSize = cellSize;
//    System.out.println("setup -> buckets.size " + buckets.size() );
//        System.out.println("setup -> buckets " + buckets );

}

     void ClearBuckets(){
       buckets.clear();
       for (int i = 0; i < cols * rows; i++)
       {
           buckets.put(i, new ArrayList<Obstacle>());   
       }
   }
    
    
     void RegisterObject(Obstacle obj) // sth is wrong
    {
        ArrayList<Integer> cellIds= getIdForObj(obj);//GetIdForObj(obj);
//        System.out.println("cellid "+cellIds);
//        System.out.println("buckets "+ buckets );
       for(Integer bucket : cellIds){
//           System.out.println("bucket index "+ bucket);
           buckets.get(bucket).add(obj);
       }
    }
    
    
    
    
    private ArrayList getIdForObj(Obstacle obj)
    {
        ArrayList<Integer> bucketsObjIsIn = new ArrayList<>();
           
        Vector3 min = new Vector3(
            obj.getWorldLocation().getX() - (obj.getRadius()),
            obj.getWorldLocation().getZ() - (obj.getRadius()));   
        Vector3 max = new Vector3(
            obj.getWorldLocation().getX() + (obj.getRadius()),
            obj.getWorldLocation().getZ() + (obj.getRadius()));

        float width = sceneWidth / cellSize;   
        //TopLeft
        AddBucket(min,width,bucketsObjIsIn);
        //TopRight
        AddBucket(new Vector3(max.getX(), min.getZ()), width, bucketsObjIsIn);
        //BottomRight
        AddBucket(new Vector3(max.getX(), max.getZ()), width, bucketsObjIsIn);
        //BottomLeft
        AddBucket(new Vector3(min.getX(), max.getZ()), width, bucketsObjIsIn);

	return bucketsObjIsIn;    
    }

    
    
    
    
    private void AddBucket(Vector3 vector,float width,ArrayList<Integer> buckettoaddto)
    {  
        int cellPosition = (int)(
                   (Math.floor(vector.getX() / cellSize)) +
                   (Math.floor(vector.getZ() / cellSize))
//                *
//                   width   
        );
        if(!buckettoaddto.contains(cellPosition))
            buckettoaddto.add(cellPosition);
    }
    
    
     ArrayList<Obstacle> GetNearby(Obstacle obj)
    {   
        if(obj.getNode()!=null){
       RRTalgorithm.addBox(0.1f, 0.6f, 0.1f, ColorRGBA.Blue, obj.getNode());
        }
        
        
        ArrayList<Obstacle> objects = new ArrayList<>();
        ArrayList<Integer> bucketIds = getIdForObj(obj);
        for(Integer i : bucketIds){
            
            objects.addAll(buckets.get(i)); // wrong, to be fixed
        
        }
        System.out.println("objects"+objects.size());
        
        for(Obstacle o : objects){
            System.out.println(o.getNode());
        if(o.getNode()!=null){
       RRTalgorithm.addBox(0.1f, 0.3f, 0.1f, ColorRGBA.Green, o.getNode());
        }
        
        }
        
        return objects;   
    }
    
    

    
}

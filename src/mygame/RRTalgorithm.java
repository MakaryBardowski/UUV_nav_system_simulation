/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;
import java.util.Random;
import static mygame.Main.RRTnode;
import static mygame.Main.publicAssetManager;
import static mygame.Main.ACCEPTABLE_RANGE_TO_TARGET;
import static mygame.Main.DELTA_STEP;

/**
 *
 * @author 48793
 */

public class RRTalgorithm {
    
    public static Waypoint targetWaypoint;
    public static boolean collisionOccured;
    public static Node obstacleNode = new Node();
    private static Node debugStickEnd = new Node();
        private static Node debugStickStart = new Node();

    
    public static void randomiseTargetNode(ColorRGBA color,float range){
    Waypoint wp = new Waypoint(random(-1,1)*range,0,random(-1,1)*range,null);
    Node node = new Node();
    node.setLocalTranslation(wp.getX(),0,wp.getZ());
    wp.setNode(node);
    RRTnode.attachChild(node);
    addBox(0.15f,0.15f,0.15f,color,wp.getNode());    
    targetWaypoint = wp;
    
    }
    
    public static Vector3f checkForCollision(Vector3f start, Vector3f end){
        collisionOccured = false;
        CollisionResults collisionResults = new CollisionResults();

        Vector3f startPos = start.clone();
        Vector3f endPos = end.clone();

        
        Ray collisionRay = new Ray(startPos,endPos.subtract(startPos));
        
        collisionRay.setLimit(startPos.distance(endPos));
        obstacleNode.collideWith(collisionRay, collisionResults);
        
        


  
        
        if (collisionResults.size() > 0) {
              
        CollisionResult closest = collisionResults.getClosestCollision(); 
        if(closest.getContactPoint().distance(startPos) <= collisionRay.getLimit() && closest.getContactPoint().distance(startPos) > 0){
            collisionOccured = true;

            return closest.getContactPoint().clone();
       
            
        }
      
        }
                  //  createArrow(startPos,endPos,ColorRGBA.Cyan);

                collisionOccured = false;
                return null;
    }
    
    public static void initiateRRTspace(ColorRGBA color){
//        RRTnode.attachChild(debugStickEnd);
//        RRTnode.attachChild(debugStickStart);
//        addBox(0.01f,1f,0.01f,ColorRGBA.Green,debugStickStart);
//                addBox(0.02f,0.6f,0.02f,ColorRGBA.White,debugStickEnd);
//Node test = (Node) publicAssetManager.loadModel("Models/Poligon/Poligon.j3o");
//        obstacleNode.attachChild(test);
//test.scale(2);
            DirectionalLight sun = new DirectionalLight();
sun.setColor(ColorRGBA.White.mult(0.75f));
sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
RRTnode.addLight(sun);

AmbientLight al = new AmbientLight();
al.setColor(ColorRGBA.White.mult(1.05f)); // around 0.1?
RRTnode.addLight(al);
        
    Main.waypoints.clear();
    Waypoint wp = new Waypoint(0,0,0,null);
    Node node = new Node();
    wp.setNode(node);
    RRTnode.attachChild(node);
    addBox(0.13f,0.13f,0.13f,color,wp.getNode());

    Main.waypoints.add(wp);
    
    
    
//    addObstacleBox(0.5f, 0.13f,50f,ColorRGBA.Red,new Node(),new Vector3f(5,0,1));
//    addObstacleBox(0.5f,0.13f,30f,ColorRGBA.Red,new Node(), new Vector3f(-5,0,2));
//    addObstacleBox(30f,0.13f,0.3f,ColorRGBA.Red,new Node(), new Vector3f(1,0,-5));
    
    RRTnode.attachChild(obstacleNode);
    }
    
    public static boolean generateRRTwaypoint(float range){

        boolean success = false;
        Node distantNode = new Node();
        
        distantNode.setLocalTranslation(random(-1,1)*range,0,random(-1,1)*range);
//        System.out.println(distantNode.getWorldTranslation());
        Waypoint targetWp = new Waypoint(distantNode.getWorldTranslation().getX(),0,distantNode.getWorldTranslation().getZ(),null);
        targetWp.setNode(distantNode);
        RRTnode.attachChild(distantNode);
        
//                addBox(0.1f,0.1f,0.1f,ColorRGBA.Blue,targetWp.getNode());

        
        Waypoint closestWp = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        
 

        for(Waypoint wp : Main.waypoints){
           
        if(!wp.getIsObstacle()){
            if(targetWp.getNode().getWorldTranslation().distance(wp.getNode().getWorldTranslation()) < closestDistance ){
            
                closestWp = wp;
                closestDistance = targetWp.getNode().getWorldTranslation().distance(wp.getNode().getWorldTranslation());
            }
            
        
        }   
        }
//        long time1 = System.currentTimeMillis();
        Vector3f collisionCoords = null;
        // collision check
        Vector3f imaginaryTargetPos = new Vector3f(closestWp.getX()-(((DELTA_STEP)*(closestWp.getX()-targetWp.getX()))/closestWp.getNode().getWorldTranslation().distance(targetWp.getNode().getWorldTranslation())),0,closestWp.getZ()-(((DELTA_STEP)*(closestWp.getZ()-targetWp.getZ()))/closestWp.getNode().getWorldTranslation().distance(targetWp.getNode().getWorldTranslation())));
        collisionCoords = checkForCollision(closestWp.getNode().getWorldTranslation(),imaginaryTargetPos);
        //collision check
//        System.out.println(System.currentTimeMillis()-time1);
        
        
        Node addedWpNode = new Node(Integer.toString(Main.waypoints.size()));
        
        float addedX = 0;
        float addedZ = 0;
        RRTnode.attachChild(addedWpNode);

        if(collisionOccured){
return false;

            


        }else{
         addedX = closestWp.getX()-(((DELTA_STEP)*(closestWp.getX()-targetWp.getX()))/closestWp.getNode().getWorldTranslation().distance(targetWp.getNode().getWorldTranslation()));
         addedZ = closestWp.getZ()-(((DELTA_STEP)*(closestWp.getZ()-targetWp.getZ()))/closestWp.getNode().getWorldTranslation().distance(targetWp.getNode().getWorldTranslation()));
       
        }
        addedWpNode.setLocalTranslation(addedX, 0, addedZ);
        
              
        Waypoint addedWp = new Waypoint(addedWpNode.getWorldTranslation().getX(),0,addedWpNode.getWorldTranslation().getZ(),closestWp);
        addedWp.setNode(addedWpNode);
        addedWp.setParent(closestWp);
        Main.waypoints.add(addedWp);
        addedWp.setX(addedX);
        addedWp.setZ(addedZ);
        addedWp.setY(0);
    

        addBox(0.5f,0.5f,0.5f,ColorRGBA.Blue,addedWp.getNode());
        
                Vector3f[] lineVerticies=new Vector3f[2];
                lineVerticies[0] = addedWp.getNode().getWorldTranslation();
                lineVerticies[1] = addedWp.getParent().getNode().getWorldTranslation();
                plotLine(lineVerticies,ColorRGBA.Cyan);
                
                
        if(addedWp.getNode().getWorldTranslation().distance(targetWaypoint.getNode().getWorldTranslation())<= ACCEPTABLE_RANGE_TO_TARGET){
        
            System.out.println("znaleziono");
            success = true;
            
            while(addedWp.getParent() != null){
               
        if(addedWp.getParent().getNode().getChildren().size() != 0 ){
             Material mat1 = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Green);
//        addedWp.getParent().getNode().scale(1.25f);
            addedWp.getParent().getNode().getChild(0).setMaterial(mat1);
                addedWp = addedWp.getParent();
                
            }
            }
            
        }

        
    
        return success;
    
    }
    
    public static void addBox(float sizeX,float sizeY, float sizeZ,ColorRGBA color,Node node){
        Box b = new Box(sizeX, sizeY, sizeZ);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);

        node.attachChild(geom);
//        node.setCullHint(Spatial.CullHint.Always);
    }
    
    public static void addObstacleBox(float sizeX,float sizeY, float sizeZ,ColorRGBA color,Node node,Vector3f offset){
        Box b = new Box(sizeX, sizeY, sizeZ);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(false);
    mat.getAdditionalRenderState().setLineWidth(1);
        geom.setMaterial(mat);

        node.attachChild(geom);
        obstacleNode.attachChild(node);
        node.move(offset);
//        node.setCullHint(Spatial.CullHint.Always);
    }
    
    public static float random(int low, int high){
    
        return  (float)(low + Math.random() * (high - low));
        
    }
    
    
    
    
    public static void plotLine(Vector3f[] lineVerticies, ColorRGBA lineColor){
        Mesh m = new Mesh();
        m.setMode(Mesh.Mode.Lines);
      

        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));
        
        short[] indexes=new short[2*lineVerticies.length]; //Indexes are in pairs, from a vertex and to a vertex
        
        for(short i=0;i<lineVerticies.length-1;i++){
            indexes[2*i]=i;
            indexes[2*i+1]=(short)(i+1);
        }
        
        m.setBuffer(VertexBuffer.Type.Index, 2, indexes);
        
        m.updateBound();
        m.updateCounts();

        Geometry geo=new Geometry("line",m);
        Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", lineColor);
        geo.setMaterial(mat);
        
        RRTnode.attachChild(geo);
    }
    
    public static void createArrow(Vector3f start,Vector3f end,ColorRGBA color){
      Arrow arrow = new Arrow(end.clone().subtractLocal(start));    
    Geometry g = new Geometry("coordinate axis", arrow);
    g.move(start);
    
    Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.getAdditionalRenderState().setWireframe(true);
    mat.getAdditionalRenderState().setLineWidth(1);
    mat.setColor("Color", color);
    g.setMaterial(mat);
    RRTnode.attachChild(g);
    }
    
    
    
    
    
    
    
    
    
    
}

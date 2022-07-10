/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

//import com.jme3.collision.CollisionResult;
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
import static mygame.Utils.random;

/**
 *
 * @author Makary Bardowski
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
    node.setLocalTranslation(wp.getWorldLocation().getX(),0,wp.getWorldLocation().getZ());
    wp.setNode(node);
    RRTnode.attachChild(node);
    addBox(0.15f,0.15f,0.15f,color,wp.getNode());    
    targetWaypoint = wp;
    node.setCullHint(Spatial.CullHint.Always);
    
    }
    
//    public static Vector3f checkForCollision(Vector3f start, Vector3f end){
//        collisionOccured = false;  // 
//        CollisionResults collisionResults = new CollisionResults(); 
//        Vector3f startPos = start.clone(); 
//        Vector3f endPos = end.clone();
//        Ray collisionRay = new Ray(startPos,endPos.subtract(startPos));
//        collisionRay.setLimit(startPos.distance(endPos));
//        obstacleNode.collideWith(collisionRay, collisionResults);
//        if (collisionResults.size() > 0) {
//              
//        CollisionResult closest = collisionResults.getClosestCollision(); 
//        if(closest.getContactPoint().distance(startPos) <= collisionRay.getLimit() && closest.getContactPoint().distance(startPos) > 0){
//            collisionOccured = true;
//
//            return closest.getContactPoint().clone();
//       
//            
//        }
//        }
//                  //  createArrow(startPos,endPos,ColorRGBA.Cyan);
//
//                collisionOccured = false;
//                return null;
//    }
    
    public static void addStartWaypoint(ColorRGBA color){
            // do wizualizacji, dodaje swiatlo zeby bylo cokolwiek widac
            DirectionalLight sun = new DirectionalLight();
            sun.setColor(ColorRGBA.White.mult(0.75f));
            sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
            RRTnode.addLight(sun);

            AmbientLight al = new AmbientLight();
            al.setColor(ColorRGBA.White.mult(1.05f)); // around 0.1?
            RRTnode.addLight(al);
        
            // punkt startowy zawsze w miejscu 0,0,0
            Main.waypoints.clear();
            Waypoint wp = new Waypoint(0,0,0,null); // jesli startujemy z innego miejsca, zmieniamy tu
            Node node = new Node();
            wp.setNode(node);
            node.move(wp.getWorldLocation().getX(),0,wp.getWorldLocation().getZ());
            RRTnode.attachChild(node);
            addBox(0.13f,0.13f,0.13f,color,wp.getNode());

            Main.waypoints.add(wp);
    
    
    

    
    RRTnode.attachChild(obstacleNode);
    }
    
    public static boolean generateRRTwaypoint(float range){
        CollisionResult result = null;
        collisionOccured = false;
        boolean success = false; // czy znalazl droge?
        
        // wygeneruj punkt (ten w kierunku którego będzie dodany nowy punkt RRT) 
        Waypoint targetWp = new Waypoint(random(-1,1)*range,0,random(-1,1)*range,null);
        
        
//        targetWp.setNode(Main.targetNodeIndicator);
//        targetWp.getNode().setLocalTranslation(targetWp.getWorldLocation().getX(),0,targetWp.getWorldLocation().getZ());
//        RRTnode.attachChild(targetWp.getNode());
//        addBox(0.1f,0.1f,0.1f,ColorRGBA.Magenta,targetWp.getNode());

        // znajdź najbliższy już istniejący punkt RRT
        Waypoint closestWp = null; // ten najbliższy punkt , na początku żaden czyli null
        float closestDistance = Float.POSITIVE_INFINITY; /* dystans do najbliższego,
        na początku nieskończoność bo skoro nie mamy najbliższego jeszcze określonego to pierwszy
        sprawdzany będzie najbliższy, bo dowolny dystans < nieskończoność
        */
        for(Waypoint wp : Main.waypoints){ // waypoints to arrayLista przechowująca wszystkie punkty
            if(!wp.getIsObstacle()){
            if(targetWp.distance(wp) < closestDistance ){
            
                closestWp = wp;
                closestDistance = targetWp.distance(wp);
            }
            }
        
        }
        
        // sprawdzanie kolizji
        Vector3 collisionCoords = null; // miejsce w ktorym wystapila kolizja
        
        /* imaginaryTargetPos to współrzędne punktu oddalonego
        o DELTA_STEP (dystans ustalony między punktami) w kierunku punktu targetWp 
        (patrz na początek metody)     
        */

        Vector3f imaginaryTargetPos = new Vector3f(closestWp.getWorldLocation().getX()-(((DELTA_STEP)*(closestWp.getWorldLocation().getX()-targetWp.getWorldLocation().getX()))/closestWp.distance(targetWp)),0,closestWp.getWorldLocation().getZ()-(((DELTA_STEP)*(closestWp.getWorldLocation().getZ()-targetWp.getWorldLocation().getZ()))/closestWp.distance(targetWp)));

        //test
        for (Obstacle obstacle : Main.obstacles) {
            Vector3 point2 = new Vector3(closestWp.getWorldLocation().getX()-(((DELTA_STEP)*(closestWp.getWorldLocation().getX()-targetWp.getWorldLocation().getX()))/closestWp.distance(targetWp)),0,closestWp.getWorldLocation().getZ()-(((DELTA_STEP)*(closestWp.getWorldLocation().getZ()-targetWp.getWorldLocation().getZ()))/closestWp.distance(targetWp)));
            Vector3 point1 = closestWp.getWorldLocation();
            
            result = Utils.findClosestCollision(Utils.lineBetweenPoints(point1, point2), obstacle);
            collisionCoords = result.getWorldLocation();
//            System.out.println("dystans kolizji: "+closestWp.getWorldLocation().distance(collisionCoords));
            if(collisionCoords.distance(closestWp.getWorldLocation()) < DELTA_STEP){
                
//                            createArrow(new Vector3f(point1.getX(),point1.getY(),point1.getZ()), new Vector3f(point2.getX(),point2.getY(),point2.getZ()),ColorRGBA.Red);
                            
//                            Vector3f collisionCoord3f = new Vector3f(result.getWorldLocation().getX(),result.getWorldLocation().getY(),result.getWorldLocation().getZ() );
//                            Box box1 = new Box(0.01f,0.3f,0.01f);
//        Geometry blue = new Geometry("Box", box1);
//        Material mat1 = new Material(publicAssetManager,
//                "Common/MatDefs/Misc/Unshaded.j3md");
//        mat1.setColor("Color", ColorRGBA.Red);
//        blue.setMaterial(mat1);
//        RRTnode.attachChild(blue);
//        blue.move(collisionCoord3f);
                            collisionOccured = result.getCollisionOccured();

//                System.out.println("kolizja");
//System.out.println("obstacle"+obstacle.getWorldLocation());
                break;
            }
        }

        // to liczy sie tylko do wizualizacji, niepotrzebne do dzialania matematycznego
        Node addedWpNode = new Node(Integer.toString(Main.waypoints.size()));
        RRTnode.attachChild(addedWpNode);

        if(collisionOccured){
        return false;
        }
        addedWpNode.setLocalTranslation(imaginaryTargetPos.getX(), 0, imaginaryTargetPos.getZ());
        
        // punkt RRT ktory dodajemy
        Waypoint addedWp = new Waypoint(addedWpNode.getWorldTranslation().getX(),0,addedWpNode.getWorldTranslation().getZ(),closestWp);
        addedWp.setNode(addedWpNode);
        addedWp.setParent(closestWp);
        Main.waypoints.add(addedWp);
        addedWp.getWorldLocation().setX(imaginaryTargetPos.getX());
        addedWp.getWorldLocation().setZ(imaginaryTargetPos.getZ());
        addedWp.getWorldLocation().setY(0);
    
        //stawia niebieski kwadrat tam gdzie stoi punkt
        addBox(0.15f,0.15f,0.15f,ColorRGBA.Blue,addedWp.getNode());
        
                // rysuje linie miedzy punktami
                Vector3f[] lineVerticies=new Vector3f[2];
                lineVerticies[0] = addedWp.getNode().getWorldTranslation();
                lineVerticies[1] = addedWp.getParent().getNode().getWorldTranslation();
//                plotLine(lineVerticies,ColorRGBA.Cyan);
                
                
                //sprawdzanie czy dodany punkt jest w zasięgu do celu ( nie sprawdzając kolizji!)
        if(addedWp.distance(targetWaypoint)<= ACCEPTABLE_RANGE_TO_TARGET){
        
            System.out.println("znaleziono");
            success = true;
            
            // zaznacza punkty z ktorych sklada sie droga 
            while(addedWp.getParent() != null){
               
        if(!addedWp.getParent().getNode().getChildren().isEmpty() ){
             Material mat1 = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
             mat1.setColor("Color", ColorRGBA.Green);
             addedWp.getParent().getNode().getChild(0).setMaterial(mat1);
             addedWp = addedWp.getParent();  
            }
           }
            // zaznacza punkty z ktorych sklada sie droga
            
        }

        
    
        return success; // zwroc czy znalazl droge czy nie
    
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

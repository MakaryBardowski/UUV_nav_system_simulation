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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import static mygame.Main.RRTnode;
import static mygame.Main.publicAssetManager;
import static mygame.Main.ACCEPTABLE_RANGE_TO_TARGET;
import static mygame.Main.DELTA_STEP;
import static mygame.Main.RRTnode;
import static mygame.Main.grid;
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

        private static int debugNum = 0;
        public static HashMap<String,Vector3> debugMap = new HashMap<>(); // box name
        
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
        Vector3f checkPos = new Vector3f(closestWp.getWorldLocation().getX()-(((DELTA_STEP/2)*(closestWp.getWorldLocation().getX()-targetWp.getWorldLocation().getX()))/closestWp.distance(targetWp)),0,closestWp.getWorldLocation().getZ()-(((DELTA_STEP/2)*(closestWp.getWorldLocation().getZ()-targetWp.getWorldLocation().getZ()))/closestWp.distance(targetWp)));

        List<Obstacle> obstaclesNearby = new ArrayList<>();
        
          Node node = new Node();
           RRTnode.attachChild(node);
//           float moveX = -33.363026f;
//           float moveY = -35.52801f;
             float moveX = checkPos.getX();
             float moveZ = checkPos.getZ();
             
//              float moveX = closestWp.getWorldLocation().getX();
//             float moveZ = closestWp.getWorldLocation().getZ();
           node.move(moveX,0,moveZ);
           Obstacle o = new Obstacle(new Vector3(moveX,moveZ),DELTA_STEP-DELTA_STEP/3.05f);
           o.setNode(node);
           obstaclesNearby = grid.getNearby(o);
        
        //test
        Vector3 point1 = new Vector3(closestWp.getWorldLocation().getX()-(((DELTA_STEP)*(closestWp.getWorldLocation().getX()-targetWp.getWorldLocation().getX()))/closestWp.distance(targetWp)),0,closestWp.getWorldLocation().getZ()-(((DELTA_STEP)*(closestWp.getWorldLocation().getZ()-targetWp.getWorldLocation().getZ()))/closestWp.distance(targetWp)));
        Vector3 point2 = closestWp.getWorldLocation();
        
        checkCollision(obstaclesNearby,point1,point2);

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
                plotLine(lineVerticies,ColorRGBA.Cyan);
                
                
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
        Geometry geom = new Geometry("Box"+debugNum++, b);

        Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);

        node.attachChild(geom);
        debugMap.put(geom.getName(), new Vector3(node.getWorldTranslation().getX(),0,node.getWorldTranslation().getZ()));
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
    mat.getAdditionalRenderState().setLineWidth(18);
    mat.setColor("Color", color);
    g.setMaterial(mat);
    RRTnode.attachChild(g);
    }


    
    
    
    public static CollisionResult checkCollision(List<Obstacle> obstaclesNearby,Vector3 point1, Vector3 point2){
        CollisionResult result = null;
        Vector3 collisionCoords = null;
        for (Obstacle obstacle : obstaclesNearby) {
            

            result = Utils.findClosestCollision(Utils.lineBetweenPoints(point1, point2), obstacle);
            
            collisionCoords = result.getWorldLocation();
            if(collisionCoords.distance(point1) < DELTA_STEP){
                
//                            createArrow(new Vector3f(point1.getX(),point1.getY(),point1.getZ()), new Vector3f(point2.getX(),point2.getY(),point2.getZ()),ColorRGBA.Red);
                            
//    System.out.println("KOLIZJA");

                            collisionOccured = result.getCollisionOccured();

                break;
            }
            
        }
                    return result;

    }
    
    
    
    
    
    
}

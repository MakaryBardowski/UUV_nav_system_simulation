/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireSphere;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import static mygame.Main.RRTnode;
import static mygame.Main.publicAssetManager;
import static mygame.RRTalgorithm.obstacleNode;


/**
 *
 * @author Makary Bardowski
 */
public class SonarReader {
        public static ArrayList<Float> angles = new ArrayList<>();
    public static HashMap<Float,ArrayList<Waypoint>> waypointsByAngle = new HashMap<>();
    
    
      
    
      public static void readAndDrawNewSonarOutput(String path,int signalThreshold) throws FileNotFoundException, IOException{
         
    Float range;
     BufferedReader reader = new BufferedReader(new FileReader(path));
        String linia = reader.readLine();
                 HashSet<Float> anglesHashSet = new HashSet<>();
                 String degString = "b_deg";
                 String rangeString = "rang_scale";
                 String numberBinsString = "nBins";
                 
        while(linia != null){

             String[] parts = linia.split("  ", 2);
             range = Float.parseFloat(parts[0].substring(parts[0].indexOf(rangeString)+rangeString.length(),parts[0].indexOf(numberBinsString)));
//             System.out.println("range: "+ range);
//             System.out.println(parts[0]);
//             System.out.println("parts1 "+parts[1]);
             range= range*5; // USUNAC W WERSJI OSTETECZNEJ, po prostu zwieksza widocznosc w jme
             
             parts[0] = parts[0].substring(parts[0].indexOf(degString)+degString.length(),parts[0].indexOf(rangeString));
//             System.out.println("parts0: " + parts[0]);
        parts[0] = parts[0].replace(":","");
        int idx= parts[1].indexOf(" ", parts[1].indexOf(" ")+20);
           String replace = parts[1].substring(0,idx);
//           System.out.println(replace);
           parts[1] = parts[1].replace(replace, "");
        float angle = Float.parseFloat(parts[0]);
//        System.out.println("kat: " + angle);
        if(!anglesHashSet.contains(angle)){
        anglesHashSet.add(angle);
        ArrayList<Waypoint> waypointsOnAngle = new ArrayList<>();
        waypointsByAngle.put(angle, waypointsOnAngle);
        
     
        
              angle = (float)Math.toRadians(angle);
//              System.out.println(parts[1]);
//              parts[1] = parts[1].replace(parts[1].substring(0,1), "");
//                            System.out.println("po "+parts[1].replaceAll(" ", "^"));
StringBuilder sb = new StringBuilder();
sb.append(parts[1]);
sb.deleteCharAt(0);
parts[1] = sb.toString();
        String[] samples = parts[1].split("\\s+");

//            System.out.println(samples.length);
//System.out.println("sample "+Arrays.toString(samples));
        for(int i = 0; i<samples.length;i++){
          if(i ==0){
              continue;
          }
              Waypoint wp = new Waypoint();

                if(Integer.valueOf(samples[i]) >= signalThreshold ){ // usunac i <30

                                    Node node = new Node();
                wp.setNode(node);

                wp.setIsObstacle(true);
                

        float radius = 0.2f     * (Float.parseFloat(samples[i])/35); // 0.2 base radius

        
        float angleCopy = (float) Math.toDegrees(angle);
        
Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(angleCopy/255f-0.1f,0.5f,0,1)); // zielony = 0
        
        

//                geom.setMaterial(mat);

        Obstacle obstacle = new Obstacle(new Vector3((float)Math.cos(angle)*((range/samples.length)*i),0,(float)Math.sin(angle)*((range/samples.length)*i)),radius);
 
                
        node.move(obstacle.getWorldLocation().getX(),obstacle.getWorldLocation().getY(),obstacle.getWorldLocation().getZ());
       
        addHitboxIndicator(RRTnode,obstacle.getRadius(),new Vector3f(  obstacle.getWorldLocation().getX(),obstacle.getWorldLocation().getY(),obstacle.getWorldLocation().getZ()  ));
        obstacle.setNode(node);
        Main.obstacles.add(obstacle);
        
        obstacleNode.attachChild(node);
        Main.grid.insert(obstacle);
//        System.out.println(obstacle.getWorldLocation());
//        Main.grid.GetNearby(obstacle);

                } 
            
            
        waypointsOnAngle.add(wp);

        }
        }
//        break;
                        linia = reader.readLine();

        }
         angles = new ArrayList<>(anglesHashSet);
        Collections.sort(angles);
 

//        System.out.println(angles);
        reader.close();
        
    
    }
    
    public static void addHitboxIndicator(Node node,float radius,Vector3f offset){
    Circle circle = new Circle(radius);
        Geometry geo = new Geometry("OurMesh", circle); // using our custom mesh object
Material mat = new Material(publicAssetManager,
    "Common/MatDefs/Misc/Unshaded.j3md");
 mat.getAdditionalRenderState().setWireframe(true);
    mat.getAdditionalRenderState().setLineWidth(4);
mat.setColor("Color", ColorRGBA.Red);
geo.setMaterial(mat);
node.attachChild(geo);
geo.move(offset);
geo.move(0,0.04f,0);
    }
    
    
   
    
}

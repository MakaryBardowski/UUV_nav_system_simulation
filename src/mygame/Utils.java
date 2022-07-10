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
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import static mygame.Main.publicAssetManager;

/**
 *
 * @author Makary Bardowski
 */
public class Utils {
       public static float random(int low, int high){
    
        return  (float)(low + Math.random() * (high - low));
       }
       
       public static Ray lineBetweenPoints(Vector3 A, Vector3 B){
//       System.out.println("point1  X:  "+A.getX() +" Z:  " +A.getZ());
//       System.out.println("point2  X:  "+B.getX() +" Z:  " +B.getZ());

        float a = (A.getZ()-B.getZ())/(A.getX()-B.getX());
        float b = (A.getZ()          - (   (A.getZ() - B.getZ())/(A.getX()-B.getX()) * A.getX()       ));
        
        float[] coeffs = new float[]{a,b};
        return new Ray(A,B,coeffs);
       }
       
       public static CollisionResult findClosestCollision(Ray ray,Obstacle obstacle){ // returns closest collision
       Vector3 A = ray.getA(); // line from A
       Vector3 B = ray.getB(); // to B
       float F = ray.getLineCoeffs()[0];
       float c = ray.getLineCoeffs()[1];
       float a = obstacle.getWorldLocation().getX();
       float b = obstacle.getWorldLocation().getZ();
       float r = obstacle.getRadius();
       
       float numeratorPart1 = a - c*F + b*F;
       float numeratorUnderSqrt = (float)Math.sqrt(r*r*F*F - a*a*F*F + 2*a*b*F - 2*a*c*F + 2*c*b + r*r - c*c - b*b);
       
       float denominator = 1+F*F;
       
//              System.out.println("sr kola: "+ obstacle.getWorldLocation());
//       System.out.println("radius: " + r);
//               System.out.println("y = "+F+"x +"+c);


       float intersectionX1 = (numeratorPart1 + numeratorUnderSqrt)/denominator;
       float intersectionZ1 = intersectionX1*F+c;
       
       float intersectionX2 = (numeratorPart1 - numeratorUnderSqrt)/denominator;
       float intersectionZ2 = intersectionX2*F+c;
       
       float closestIntersectionX = Float.NaN,closestIntersectionZ = Float.NaN;

//     System.out.println(intersectionX1+", " +intersectionZ1 + " | " +intersectionX2 +", " +intersectionZ2);
       
      Vector3 intersection1 = new Vector3(intersectionX1,0,intersectionZ1);
       Vector3 intersection2 = new Vector3(intersectionX2,0,intersectionZ2);

       boolean point_1_isBetween = pointIsBetween(A,B,intersection1);
       boolean point_2_isBetween = pointIsBetween(A,B,intersection2);
      
       
       if( !point_1_isBetween && !point_2_isBetween   ){
       return CollisionResult.NO_COLLISION;
       }
       
       
       

       float distance1 = intersection1.distance(A);
       float distance2 = intersection2.distance(A);
       
       if(point_1_isBetween && distance1 < distance2 ){
           closestIntersectionX = intersectionX1;
           closestIntersectionZ = intersectionZ1;
       } else{
       closestIntersectionX = intersectionX1;
       closestIntersectionZ = intersectionZ1;
       }
       
//       System.out.println("pkt1 nalezy: " + point_1_isBetween);
//       System.out.println("pkt2 nalezy: " + point_2_isBetween);
//       System.out.println("najbliższy punkt przeciecia linii z kolem: X: "+intersectionX1+" Z: "+ intersectionZ1);
//       System.out.println("najbliższy punkt przeciecia linii z kolem: X: "+intersectionX2+" Z: "+ intersectionZ2);

       return new CollisionResult(true,new Vector3(closestIntersectionX,0,closestIntersectionZ));
       }
       
       
       
     
       
       
       
       
       private static boolean pointIsBetween(Vector3 A, Vector3 B, Vector3 C){
       
           
       float zMax = Math.max(A.getZ(), B.getZ());
       float zMin = Math.min(A.getZ(), B.getZ());
       
       if(C.getZ() > zMax || C.getZ() < zMin){
       return false;
       }
       
       float difference = Math.abs(   C.getZ()    - C.getZ()   );
       float threshold = 0.000001f;
       return difference<= threshold;
    
        
        
           
       }
       
       
       
       //jmonkey exclusive
       public static void attachCoordinateAxes(Node node,Vector3f pos) {
    Arrow arrow = new Arrow(Vector3f.UNIT_X);
    putShape(node,arrow, ColorRGBA.Red).setLocalTranslation(pos); //x - czerwony

    arrow = new Arrow(Vector3f.UNIT_Y);
    putShape(node,arrow, ColorRGBA.Green).setLocalTranslation(pos);//y - zielony

    arrow = new Arrow(Vector3f.UNIT_Z);
    putShape(node,arrow, ColorRGBA.Blue).setLocalTranslation(pos);//z - niebieski
}

public static Geometry putShape(Node node,Mesh shape, ColorRGBA color) {
    Geometry g = new Geometry("coordinate axis", shape);
    Material mat = new Material(publicAssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.getAdditionalRenderState().setWireframe(true);
    mat.getAdditionalRenderState().setLineWidth(4);
    mat.setColor("Color", color);
    g.setMaterial(mat);
    node.attachChild(g);
    return g;
}
       
}

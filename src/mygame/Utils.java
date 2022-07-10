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
public class Utils {
       public static float random(int low, int high){
    
        return  (float)(low + Math.random() * (high - low));
       }
       
       public static float[] lineBetweenPoints(Vector3 A, Vector3 B){
//       System.out.println("point1  X:  "+A.getX() +" Z:  " +A.getZ());
//       System.out.println("point2  X:  "+B.getX() +" Z:  " +B.getZ());

        float a = (A.getZ()-B.getZ())/(A.getX()-B.getX());
        float b = (A.getZ()          - (   (A.getZ() - B.getZ())/(A.getX()-B.getX()) * A.getX()       ));
        
        float[] coeffs = new float[]{a,b};
        return coeffs;
       }
       
       public static CollisionResult findCollision(float[] lineCoeffs,Obstacle obstacle){
       float F = lineCoeffs[0];
       float c = lineCoeffs[1];
       float a = obstacle.getWorldLocation().getX();
       float b = obstacle.getWorldLocation().getZ();
       float r = obstacle.getRadius();
       
       float numeratorPart1 = a - c*F + b*F;
       float numeratorUnderSqrt = r*r*F*F - a*a*F*F + 2*a*b*F - 2*a*c*F + 2*c*b + r*r - c*c - b*b;
       
       float denominator = 1+F*F;
       
       

       float intersectionX = (numeratorPart1 + (float)Math.sqrt(numeratorUnderSqrt))/denominator;
       float intersectionZ = intersectionX*F+c;
       if(Float.isNaN(intersectionX)){
       return CollisionResult.noCollision;
       }
//               System.out.println("y = "+F+"x +"+c);
//
//       System.out.println("sr kola: "+ obstacle.getWorldLocation());
//       System.out.println("punkt przeciecia linii z kolem: X: "+intersectionX+" Z: "+ intersectionZ);
//       System.out.println("radius: " + r);
       return new CollisionResult(true,new Vector3(intersectionX,0,intersectionZ));
       }
       
       
}

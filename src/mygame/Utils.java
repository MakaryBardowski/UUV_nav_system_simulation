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
       
       public static void lineBetweenPoints(Vector3 P, Vector3 Q){
       
       float a = Q.getZ() - P.getZ();
       float b = P.getX() - Q.getX();
       float c = a*(P.getX()) + b*(P.getZ());
       
       System.out.println("point1  X:  "+P.getX() +" Z:  " +P.getZ());
       System.out.println("point2  X:  "+Q.getX() +" Z:  " +Q.getZ());
       System.out.println("a: " +a/b );
       
       System.out.println(-a/b+"x "+" + "+c/b + " = "+b/b+"y");
       
       }
}

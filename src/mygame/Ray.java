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
public class Ray {
   private Vector3 A,B; /* points between which the ray is cast 
(automatically assigns limit of the distance between points */
   private  float[] lineCoeffs; // m and b coeffs of a line y = m*x + b, which goes through points A and B

    public Ray(Vector3 A, Vector3 B, float[] lineCoeffs) {
        this.A = A;
        this.B = B;
        this.lineCoeffs = lineCoeffs;
    }

    public Vector3 getA() {
        return A;
    }

//    public void setA(Vector3 A) {
//        this.A = A;
//    }

    public Vector3 getB() {
        return B;
    }

//    public void setB(Vector3 B) {
//        this.B = B;
//    }

    public float[] getLineCoeffs() {
        return lineCoeffs;
    }

//    public void setLineCoeffs(Float[] lineCoeffs) {
//        this.lineCoeffs = lineCoeffs;
//    }
    
   
    
    
}

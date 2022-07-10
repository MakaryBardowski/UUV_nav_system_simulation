/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**

Circle.

*
@author Martin Simons modified by 48793
@version $Id$

*/

public class Circle extends Mesh

{

private Vector3f center;
private float radius;
private int samples;

public Circle(float radius)

{

this(Vector3f.ZERO, radius, 16);

}



public Circle(float radius, int samples)

{

this(Vector3f.ZERO, radius, samples);

}




public Circle(Vector3f center, float radius, int samples)

{

super();

this.center = center;

this.radius = radius;

this.samples = samples;



setMode(Mode.Lines);

updateGeometry();

}



protected void updateGeometry()

{
FloatBuffer positions = BufferUtils.createFloatBuffer(samples * 3);

FloatBuffer normals = BufferUtils.createFloatBuffer(samples * 3);

short[] indices = new short[samples * 2];
int idc = 0;


float rate = FastMath.TWO_PI / (float) samples;

float angle = 0;

for (int i = 0; i < samples; i++){

float x = FastMath.cos(angle) + center.x;

float z = FastMath.sin(angle) + center.z;



positions.put(x * radius).put(center.y).put(z * radius);

normals.put(new float[] { 0, 1, 0 });

indices[idc++] = (short) i;

if (i < samples - 1){
indices[idc++] = (short) (i + 1);
}else{
indices[idc++] = 0;
}


angle += rate;

}



setBuffer(Type.Position, 3, positions);

setBuffer(Type.Normal, 3, normals);

setBuffer(Type.Index, 2, indices);



setBuffer(Type.TexCoord, 2, new float[] { 0, 0, 1, 1 });



updateBound();

}
}

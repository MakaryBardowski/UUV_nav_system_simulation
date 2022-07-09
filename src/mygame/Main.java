package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mygame.RRTalgorithm.obstacleNode;
import static mygame.Utils.random;

public class Main extends SimpleApplication {
    
    public static ArrayList<Waypoint> waypoints = new ArrayList<>();
    public static final Node RRTnode = new Node();
    public static AssetManager publicAssetManager;
    
    public static final float DELTA_STEP = 3f; // 0.5f
    public final float RRT_RANGE = 50f; // bedzie 700f - bo 700m
    private final int RRT_ITERATIONS_PER_PRESS = 30; // ilosc prob uwtorzenia punktu RRT
    public static final float ACCEPTABLE_RANGE_TO_TARGET = 3f;
    public static final float CAMERA_SPEED = 26f; // 13f
        
    private BitmapText timeText;
    private long timeSum = 0;
    private BitmapText nodeCountText;
    private BitmapText timeSumText;
    private int nodeCounter = 2;
    
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        publicAssetManager = app.getAssetManager();
     
    }

    @Override
    public void simpleInitApp() {
       

    
        
          guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    
    nodeCountText = new BitmapText(guiFont,false);
    nodeCountText.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    nodeCountText.setText("Ilosc punktow RRT: 2 "); 
    nodeCountText.setLocalTranslation(
      settings.getWidth() / 10 - nodeCountText.getLineWidth()/2,
     settings.getHeight()- settings.getHeight() / 15 + nodeCountText.getLineHeight()/2, 0);
    guiNode.attachChild(nodeCountText);
        
     timeText = new BitmapText(guiFont,false);
    timeText.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    timeText.setText("Czas ostatniego wykonania RRT: 0 ms");
    timeText.setLocalTranslation(
      settings.getWidth() / 6.3f - timeText.getLineWidth()/2,
     settings.getHeight()-settings.getHeight() / 15- settings.getHeight() / 15 + timeText.getLineHeight()/2, 0);
    guiNode.attachChild(timeText);    
    
     timeSumText = new BitmapText(guiFont,false);
    timeSumText.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    timeSumText.setText("Suma czasu tworzenia drzewa: "+ timeSum +" ms");
    timeSumText.setLocalTranslation(
      settings.getWidth() / 6.5f - timeSumText.getLineWidth()/2,
     settings.getHeight()-settings.getHeight() / 8- settings.getHeight() / 15 + timeSumText.getLineHeight()/2, 0);
    guiNode.attachChild(timeSumText);
    
    long time1 = System.currentTimeMillis();
    for(int i = 0; i < 10000; i++){
    random(-1, 1);
    }
    System.out.println(System.currentTimeMillis() - time1);
    
        initKeys();
        flyCam.setMoveSpeed(CAMERA_SPEED);
        flyCam.setZoomSpeed(30);
        publicAssetManager = assetManager;
        rootNode.attachChild(RRTnode);
        RRTalgorithm.addStartWaypoint(ColorRGBA.Green);
        RRTalgorithm.randomiseTargetNode(ColorRGBA.Yellow,RRT_RANGE);
                try
        {
            
                        System.out.println("try");
                                       SonarReader.readAndDrawNewSonarOutput("C:\\Users\\48793\\Desktop\\basen\\sonarOdczyty\\jezioro\\walec metal n 5 357.txt",35);
          
        } catch (IOException ex) {
            System.out.println("kacz");
        }
                long time = System.currentTimeMillis();
                jme3tools.optimize.GeometryBatchFactory.optimize(obstacleNode);
                                System.out.println(System.currentTimeMillis()-time);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     private void initKeys() {
        inputManager.addMapping("K",  new KeyTrigger(KeyInput.KEY_K));
  
        inputManager.addListener(actionListener, "K");

    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
           if(name.equals("K")&& !keyPressed){
           long time1 =  System.currentTimeMillis();
           
               for(int i = 0; i < RRT_ITERATIONS_PER_PRESS;i++){
                  RRTalgorithm.generateRRTwaypoint(RRT_RANGE);
               
//               nodeCounter++;
               }

                
               
//                boolean success = false;
//                while(!success){
//                success = RRTalgorithm.generateRRTwaypoint(RRT_RANGE);
//                nodeCounter++;
//                }



                
                

                nodeCountText.setText("Ilosc punktów RRT: "+waypoints.size());
                long executionTime = System.currentTimeMillis()-time1;
                timeSum += executionTime;
               timeText.setText("Czas ostatniego wykonania RRT: "+ executionTime +" ms");
               timeSumText.setText("Suma czasu tworzenia drzewa: "+ timeSum +" ms");
           }
            
        }
    };
    
    
    
}

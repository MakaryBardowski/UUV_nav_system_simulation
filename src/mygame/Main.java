package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mygame.RRTalgorithm.debugMap;
import static mygame.RRTalgorithm.obstacleNode;
import static mygame.Utils.random;

public class Main extends SimpleApplication {
    
    /* IF YOU ARE READING THIS - THIS CODE IS PENDING FOR CLEANUP. DUE TO HIGH COMPLEXITY OF THE PROGRAM,
    I DECIDED I WILL REWRITE IT (ACCORDING TO SOLID RULES) ONCE EVERYTHING IS IN PLACE */
    public static ArrayList<Waypoint> waypoints = new ArrayList<>();
    public static ArrayList<Obstacle> obstacles = new ArrayList<>();
    public static final Node RRTnode = new Node();
    public static AssetManager publicAssetManager;
    
    public static final float DELTA_STEP = 3f; // distance between RRT points
    public final float RRT_RANGE = 50f; // range in meters (square RRT_RANGE x RRT_RANGE)
    private final int RRT_ITERATIONS_PER_PRESS = 3000; 
    public static final float ACCEPTABLE_RANGE_TO_TARGET = DELTA_STEP;
    public static final float CAMERA_SPEED = 26f; 
    public static final int GRID_SIZE = 4; // RRT optimization hashing grid size
    public static final int A_STAR_GRID_SIZE = 1;
    
    private BitmapText timeText;
    private long timeSum = 0;
    private BitmapText nodeCountText;
    private BitmapText timeSumText;
        static WorldGrid grid;

        //debug variables
    static Node targetNodeIndicator;
    static Node debugNode = new Node();
    static Node hashNode = new Node();
    static int nodesChosen = 0; // simple debug var, if 0 then you choose the starting point on click
    // if 1 you choose the finish point
    // if 2 then it resets the chosen nodes
    static Obstacle startingObst = null;
    static Obstacle finishObst = null;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        publicAssetManager = app.getAssetManager();
     
    }
    
    private void initTargetIndicatior(){
   Box b = new Box(0.2f, 0.2f, 0.2f);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Magenta);
        geom.setMaterial(mat);

       targetNodeIndicator = new Node();
       targetNodeIndicator.attachChild(geom);
    }

    @Override
    public void simpleInitApp() {
        rootNode.attachChild(debugNode);
        
        Picture crosshair = new Picture("crosshair");
        crosshair.setImage(assetManager, "Textures/crosshair.png", true);
        crosshair.setWidth(settings.getHeight()*0.04f);
        crosshair.setHeight(settings.getHeight()*0.04f); //0.04f
        crosshair.setPosition((settings.getWidth()/2)-settings.getHeight()*0.04f/2, settings.getHeight()/2-settings.getHeight()*0.04f/2);
        guiNode.attachChild(crosshair);
        
        
       initTargetIndicatior();
rootNode.attachChild(hashNode);
       grid = new WorldGrid(GRID_SIZE);
        
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

               SonarReader sonarReader = new SonarReader();
               sonarReader.readFile("C:\\Users\\48793\\Desktop\\basen\\sonarOdczyty\\jezioro\\walec metal n 5 357.txt",35);
                        
                        System.out.println("obstacles.size = "+obstacles.size());
                        
                        
                         Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.getAdditionalRenderState().setWireframe(true);
       mat.getAdditionalRenderState().setLineWidth(1.6f);

    mat.setColor("Color", ColorRGBA.DarkGray);
                        
    
    //wizualizacja glownej siatki
                        for(int i = -75; i < 75;i++){
                            for(int j = -75; j<75;j++){
                         Geometry g = new Geometry("wireframe grid", new Grid(2, 2, 1f));
   
    g.setMaterial(mat);
    g.center().move(GRID_SIZE*i+0.5f,0,GRID_SIZE*j+0.5f);
    g.scale(GRID_SIZE);
    obstacleNode.attachChild(g);
                            }
                        }
                             Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.getAdditionalRenderState().setWireframe(true);
       mat1.getAdditionalRenderState().setLineWidth(1);
       mat1.setColor("Color", ColorRGBA.Brown);
                        
                  //wizualizacja siatki A*
                  for(int i = -75; i < 75;i++){
                            for(int j = -75; j<75;j++){
                         Geometry g = new Geometry("wireframe grid", new Grid(2, 2, 1f));
    
    g.setMaterial(mat1);
    g.center().move(A_STAR_GRID_SIZE*i+0.5f,0,A_STAR_GRID_SIZE*j+0.5f);
    g.scale(A_STAR_GRID_SIZE);
    obstacleNode.attachChild(g);
                            }
                        }
                  
                  
                        
                        
        } catch (IOException ex) {
            System.out.println("kacz");
        }
                
                Utils.attachCoordinateAxes(rootNode,Vector3f.ZERO);
                System.out.println(rootNode.getChild(rootNode.getChildren().size()-1));
                
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
          inputManager.addMapping("select",  new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("castRay",  new KeyTrigger(KeyInput.KEY_R));

        inputManager.addListener(actionListener, "K");
                inputManager.addListener(actionListener, "select");
                inputManager.addListener(actionListener, "castRay");


    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("castRay") && !keyPressed && finishObst != null){
                List<Obstacle> obstaclesNearby = new ArrayList<>();
                        Vector3 point1 = startingObst.getWorldLocation();

                 Vector3 point2 = finishObst.getWorldLocation();
                 obstaclesNearby = grid.getNearby(finishObst);
       RRTalgorithm.createArrow(new Vector3f(startingObst.getWorldLocation().getX(),startingObst.getWorldLocation().getY(),startingObst.getWorldLocation().getZ()), new Vector3f(finishObst.getWorldLocation().getX(),finishObst.getWorldLocation().getY(),finishObst.getWorldLocation().getZ()),ColorRGBA.Red);
       RRTalgorithm.checkCollision(obstaclesNearby, point1, point2);
            }
            
            if(name.equals("select") && !keyPressed){
            
                
                 CollisionResults results = new CollisionResults();
        com.jme3.math.Ray ray = new com.jme3.math.Ray(cam.getLocation(), cam.getDirection());
        RRTnode.collideWith(ray, results);
        for (int i = 0; i < results.size(); i++) {
          float dist = results.getCollision(i).getDistance();
          Vector3f pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
        }
        if (results.size() > 0) {
            
          com.jme3.collision.CollisionResult closest = results.getClosestCollision();
          
          System.out.println("trafiles noda: "+closest.getGeometry().getName() );

          
          
          Obstacle o = new Obstacle( debugMap.get(closest.getGeometry().getName()) , DELTA_STEP-DELTA_STEP/3.05f); //3.05 so the radius is less than gridCell size / 2
          grid.getNearby(o);
          
          if(nodesChosen == 0){
          startingObst = o;
          nodesChosen++;
          }else if(nodesChosen++ == 1){
          finishObst = o;
                    nodesChosen++;

          }else{
          nodesChosen = 0;
          finishObst = null;
          startingObst = null;
          }
        } 
                
                
                
            }
            
            
           if(name.equals("K")&& !keyPressed){
           long time1 =  System.currentTimeMillis();
           
           
           //spatial hash test
//           Node node = new Node();
//           RRTalgorithm.addBox(0.1f, 0.6f, 0.1f, ColorRGBA.Blue, node);
//           RRTnode.attachChild(node);
//           float moveX = -33.363026f;
//           float moveY = -35.52801f;
////                System.out.println("flooooor: " + Math.floor(-0.2f) );
////             float moveX = -1;
////             float moveY = 1;
//           node.move(moveX,0,moveY);
//           Obstacle o = new Obstacle(new Vector3(moveX,moveY),0.2f);
////                      Obstacle o1 = new Obstacle(new Vector3(0,0),0.2f);
//
//           o.setNode(node);
//           grid.getNearby(o);
//           System.out.println("close to o: "+grid.getNearby(o));
//                      System.out.println("close to o1: "+grid.getNearby(o1));

//           grid.GetNearby(o);
           // spatial hash test
           
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

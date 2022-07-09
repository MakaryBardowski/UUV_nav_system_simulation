/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

/**
 *
 * @author 48793
 */
public class SpatialHash {
    
        public static Multimap<GridCell, Waypoint> worldGrid = MultimapBuilder.hashKeys().hashSetValues().build();
        // zawiera wszystkie waypointy przyporzadkowane komorkom
        private float gridSize = 0.5f;
        
        public static void getContainingCell(Waypoint wp){
        
            String containingCellVector = wp.getWorldLocation().getX()+","+0+","+wp.getWorldLocation().getY();
            
            
        }
        
    
    
    
}

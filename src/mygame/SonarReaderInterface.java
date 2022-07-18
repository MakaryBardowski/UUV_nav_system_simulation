/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author 48793
 */
public interface SonarReaderInterface {
    
    public void readFile(String path, int signalThreshold)throws FileNotFoundException, IOException;
    
}

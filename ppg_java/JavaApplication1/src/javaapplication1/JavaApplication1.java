/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author mac
 */
public class JavaApplication1 {
    
    public int fSampling = 125;
    public int multiplier = fSampling/125;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        
        ArrayList<Double> ecgSignal = getArray("DATA01_1.dat");
        ArrayList<Double> ppgSignal1 = getArray("DATA01_2.dat");
        ArrayList<Double> ppgSignal2 = getArray("DATA01_3.dat");
        ArrayList<Double> accDataX = getArray("DATA01_4.dat");
        ArrayList<Double> accDataY = getArray("DATA01_5.dat");
        ArrayList<Double> accDataZ = getArray("DATA01_6.dat");
        
        ArrayList<Double> ppgSignalAverage = addTwoList(ppgSignal1,ppgSignal2);
        
        
        
        
    }
    
    public static ArrayList<Double> addTwoList(ArrayList<Double> l1,ArrayList<Double> l2){
        int len = l1.size();
        ArrayList<Double> l3 = new ArrayList<>();
        for(int i=0;i<len;i++){
            l3.add( l1.get(i) + l2.get(i) );
        }
        
        return l3;
    }
    
    /**
     * 
     * @param fileName
     * @return ArrayList of data
     * @throws java.io.FileNotFoundException
     */
    
    public static ArrayList<Double> getArray(String fileName) throws FileNotFoundException{
        
        Scanner scanner = new Scanner(new FileReader("test/"+fileName));
        
        ArrayList<Double> dataList;
        dataList = new ArrayList<>();
        //System.out.println(scanner.nextLine());
        while(scanner.hasNextDouble()){
            dataList.add(scanner.nextDouble());
        }
       
        return dataList;
        
    }
    
  
    
    
    
    
}

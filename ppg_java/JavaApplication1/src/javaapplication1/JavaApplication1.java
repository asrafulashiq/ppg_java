/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mac
 */
public class JavaApplication1 {
    
   
    

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        
        int fSampling = 125;
        int multiplier = fSampling/125;
        
        ArrayList<Double> ecgSignal = getArray("DATA01_1.dat");
        ArrayList<Double> ppgSignal1 = getArray("DATA01_2.dat");
        ArrayList<Double> ppgSignal2 = getArray("DATA01_3.dat");
        ArrayList<Double> accDataX = getArray("DATA01_4.dat");
        ArrayList<Double> accDataY = getArray("DATA01_5.dat");
        ArrayList<Double> accDataZ = getArray("DATA01_6.dat");
        
        ArrayList<Double> ppgSignalAverage = addTwoList(ppgSignal1,ppgSignal2);
        
        /**
         * RLS filtering 
         */
        int lParameterOfRLS = 40;
        
        ArrayList<Double> ex = rlsFilter(lParameterOfRLS, accDataX, ppgSignalAverage);
        
        ArrayList<Double> exy = rlsFilter(lParameterOfRLS, accDataY, ex);
        ArrayList<Double> rRaw = rlsFilter(lParameterOfRLS, accDataZ, exy);
     
        
        /**
         * filtering all data to frequency range for human HR
         */
        ArrayList<Double> rN = myBandPass(rRaw, fSampling);
        ArrayList<Double> accDataXfiltered = myBandPass(accDataX, fSampling);
        ArrayList<Double> accDataYfiltered = myBandPass(accDataY, fSampling);
        ArrayList<Double> accDataZfiltered = myBandPass(accDataZ, fSampling);
        
        //float fPrev = initialize( rN.subList(0,1000), accDataX.subList(0,1000),accDataY.subList(0,1000),
         //       accDataZ.subList(0, 1000),fSampling);
        
        
        
        
        
    }
    
    
    
    
    /**
     * TODO
     * @param sig
     * @param accX
     * @param accY
     * @param accZ
     * @param fSampling
     * @return 
     */
    
    public static float initialize(List<Double> sig, List<Double> accX, List<Double> accY,
            List<Double> accZ, float fSampling){
        
        return 0;
    }
    
    
    //TODO
    /**
     * 
     * @param rSig signal to be filtered
     * @param fSampling sampling frequency
     * @return 
     */
    public static ArrayList<Double> myBandPass(ArrayList<Double> rSig, double fSampling){
        
        return null;
        
    }
    
    
    // TODO:
    /**
     * 
     * @param lParameter parameter of rls filter
     * @param hSig helper signal
     * @param mainSig main signal
     * @return filtered signal
     */
    
    public static ArrayList<Double> rlsFilter(int lParameter, ArrayList<Double> hSig, ArrayList<Double> mainSig){
        
        return null;
    }
    
    /**
     * 
     * @param l1 arrayList 1
     * @param l2 arrayList 2
     * @return arrayList 
     */
    public static ArrayList<Double> addTwoList(ArrayList<Double> l1,ArrayList<Double> l2){
        int len = l1.size();
        ArrayList<Double> l3;
        l3 = new ArrayList<>();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        
        double[] ecgSignal = getArray("DATA01_1.dat");
        double[] ppgSignal1 = getArray("DATA01_2.dat");
        double[] ppgSignal2 = getArray("DATA01_3.dat");
        double[] accDataX = getArray("DATA01_4.dat");
        double[] accDataY = getArray("DATA01_5.dat");
        double[] accDataZ = getArray("DATA01_6.dat");
        
        double[] ppgSignalAverage = DSP.times( DSP.plus(ppgSignal1, ppgSignal2) , 0.5) ;
        
        
        /**
         * RLS filtering 
         */
        int lParameterOfRLS = 40;
        
        double[] ex = rlsFilter(lParameterOfRLS, accDataX, ppgSignalAverage);
        
        double[] exy = rlsFilter(lParameterOfRLS, accDataY, ex);
        double[] rRaw = rlsFilter(lParameterOfRLS, accDataZ, exy);
     
        
        /**
         * filtering all data to frequency range for human HR
         */
        double[] rN = myBandPass(rRaw, fSampling,1);
        double[] accDataXfiltered = myBandPass(accDataX, fSampling,1);
        double[] accDataYfiltered = myBandPass(accDataY, fSampling,1);
        double[] accDataZfiltered = myBandPass(accDataZ, fSampling,1);
        
        double fPrev = initialize( subList(rN,1000), subList(accDataX,1000),subList(accDataY,1000),
                subList(accDataZ, 1000),fSampling);
        
        
        
        // bandpassing all the data of signal
         ppgSignal1 = myBandPass(ppgSignal1,fSampling,2);
         ppgSignal2 = myBandPass(ppgSignal2,fSampling,2);

         accDataX = myBandPass(accDataX,fSampling,2);
         accDataX = myBandPass(accDataY,fSampling,2);
         accDataX = myBandPass(accDataZ,fSampling,2);
        
        // now doing the emd portion
        int iStart = 1;
        int iStep  = 250 * multiplier ;
        int iStop  = rN.length - 1000 * multiplier ;
        
        double delta_count = 0 ; // need in EEMD
    
        int iCounter = 1;
        int iSegment = iStart;
    
        while(iSegment<=iStop){
            
            int[] currentSegment = DSP.irange(iStart, iStop);
            
            double[] peaks = null; 
            
            double freqEstimates = doEEMD(
                    getValuesFromIndex(ppgSignal1, currentSegment),
                    getValuesFromIndex(ppgSignal2, currentSegment), 
                    getValuesFromIndex(accDataX, currentSegment), 
                    getValuesFromIndex(accDataY, currentSegment), 
                    getValuesFromIndex(accDataZ, currentSegment), 
                    fPrev, fSampling,peaks
            );
            
            // we construct Simf \3 Sa, 0.5, and from this set, we
            // take the peak location nearest to fprev
            
            double[] minLoc = minValLoc( DSP.abs( DSP.min(peaks, fPrev) ) );
            double minimum = minLoc[0];
            int loc = (int)minLoc[1];
            
            double freq_td = frequency_estimate(
                    getValuesFromIndex(ppgSignal1, currentSegment),
                    getValuesFromIndex(ppgSignal2, currentSegment), 
                    getValuesFromIndex(accDataX, currentSegment), 
                    getValuesFromIndex(accDataY, currentSegment), 
                    getValuesFromIndex(accDataZ, currentSegment), 
                    fPrev, multiplier);
            
            if(freqEstimates!=-1){
                //tracking from AC
                delta_count = 0;
                System.out.println("tracking from AC");
            }
            else if( minimum <= 9 ){
                //If its distance from fprev is within 7 BPM
                // tracking from emd 2
                freqEstimates = peaks[loc];
                if (delta_count>0){
                    delta_count = delta_count - 0.5;
                }
                System.out.println("('tracking from emd :");
            }
            else{
                // track from rls
                delta_count = delta_count + 1;
                
                double[] yCropped = getValuesFromIndex(rN, currentSegment);
                
                // we put its dominant peaks (80% of the maximum) in
                // Srls
                double[] S_rls = maxFindFromThreshold(yCropped,0.8,fSampling);
                
                ArrayList<Double> S_a = new ArrayList<>();
                
                double[] dominantPeaks = maxFindFromThreshold(accDataX, 0.6, fSampling);
                if(dominantPeaks.length>2){
                    extendList(S_a, subList(dominantPeaks, 2));
                }else{
                    extendList(S_a, dominantPeaks);
                }
                dominantPeaks = maxFindFromThreshold(accDataY, 0.6, fSampling);
                if(dominantPeaks.length>2){
                    extendList(S_a, subList(dominantPeaks, 2));
                }else{
                    extendList(S_a, dominantPeaks);
                }
                dominantPeaks = maxFindFromThreshold(accDataZ, 0.6, fSampling);
                if(dominantPeaks.length>2){
                    extendList(S_a, subList(dominantPeaks, 2));
                }else{
                    extendList(S_a, dominantPeaks);
                }
                
                // set Srls\3 Sa,0.6
                ArrayList<Double> fRlsSet = new ArrayList<>();
                for (double iRLS : S_rls) {
                    if( DSP.min( DSP.abs( DSP.minus(S_a, iRLS) ) ) > 3 ){
                        fRlsSet.add(iRLS);
                    }
                }
                
                double[] fRlsSet_ = filterArray(fRlsSet, 40, 60);
                
                if( fRlsSet_.length == 1 &&  Math.abs( DSP.min( DSP.minus(fRlsSet_, fPrev) ) ) < 26.5 ){
                    freqEstimates = fRlsSet_[0];
                }
                else if( freq_td != -1 && Math.abs( freq_td - fPrev ) < 12 ){
                    freqEstimates = freq_td;
                }
                else{
                    double[] minVal  =  minValLoc(DSP.abs( DSP.minus(S_rls, fPrev) ) );
                    double min = minVal[0];
                    int pos = (int)(minVal[1]);
                    if ( min < 9 ){
                        freqEstimates = S_rls[pos];
                    }
                    
                    if(freqEstimates == -1){
                        
                    }
                    
                }
                
                
                
            }
            
            
        }
         
         
    }
    
    /**
     * 
     * @param a
     * @param min
     * @param max
     * @return 
     */
    public static double[] filterArray(double[] a, double min, double max){
        
        ArrayList<Double> filteredArray = new ArrayList<>();
        for (double val : a) {
            if( val >= min && val <= max  ){
                filteredArray.add(val);
            }
        }
        
        return toDoubleArray(filteredArray);
        
    }
    
    /**
     * TODO:::::::::
     * @param sig1
     * @param sig2
     * @param accX
     * @param accY
     * @param accZ
     * @param fPrev
     * @param multiplier
     * @return 
     */
    public static double frequency_estimate(double[] sig1, double[] sig2, double[] accX,
           double[] accY, double[] accZ,double fPrev,int multiplier){
        
        return 0;
    }
    
    /**
     * 
     * @param list
     * @return 
     */
    public static double[] minValLoc(double[] list){
        int minLoc = -1;
        double minVal = Double.MAX_VALUE;
        for(int i=0;i<list.length;i++){
            if(list[i] < minVal){
                minVal = list[i];
                minLoc = i;
            }
        }
        return new double[]{minVal,(double)minLoc};
        
    }
    
    /**
     * TODO::::::::
     * @param sig1
     * @param sig2
     * @param accX
     * @param accY
     * @param accZ
     * @param fPrev
     * @param fSampling
     * @param peaks
     * @return 
     */
    public static double doEEMD( double[] sig1, double[] sig2, double[] accX,
           double[] accY, double[] accZ, double fPrev, double fSampling, double[] peaks){
        
        return 0;
    }
    
    
    /**
     * 
     * @param doubleArray
     * @param len
     * @return 
     */
    public static double[] subList(double[] doubleArray, int len){
        double[] arr = new double[len];
        System.arraycopy(doubleArray, 0, arr, 0, len);
        return arr;
    }
    
    
    /**
     * TODO
     * @param sig PPG signal data
     * @param accX
     * @param accY
     * @param accZ
     * @param fSampling 
     * @return 
     */
    
    public static double initialize(double[] sig, double[] accX, double[] accY,
            double[] accZ, float fSampling){
        
        double fPrev = -1;
        double[] w = linspace(50,150,4000);
        double[] ww = DSP.times( w, 2 * Math.PI /(fSampling * 60.0) );
        
        double[] fSig = Complex.abs(freqz(sig,ww));
        
        int[] locs = findPeakLocation(fSig, 0.8 * DSP.max(fSig));
        
        
        if(locs.length == 1){
            fPrev = w[locs[0]];
            return fPrev;
        }
        
        //  if more than one frequencies have value above 80% 
        //  then we look for their second harmonics
        
        double[] peakFreqs = getValuesFromIndex(w,locs);
        double secondHarmonicMax = -1;
        
        for(int i = 0; i < locs.length; i++ ){
            for( int j=0; j < locs.length; j++ ){
                if( i != j && Math.abs( 2 * peakFreqs[i] - peakFreqs[j] ) < 5 ){
                    if (peakFreqs[i] > secondHarmonicMax){
                        secondHarmonicMax = peakFreqs[i];
                    }
                }
            }
        }
        
        if(secondHarmonicMax != -1){  // |2x?y| < 5 BPM found
            fPrev = secondHarmonicMax;
            return fPrev;
        }
        
        //  taking all the dominant peaks (80% of the maximum peak) 
        //  from the acceleration signals 
        
        ArrayList<Double> dominantPeaksOfAcc = new ArrayList<>();
        
        double[] fAccSig = Complex.abs(freqz(accX,ww));        
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, getValuesFromIndex(w,locs) );
        
        fAccSig = Complex.abs(freqz(accY,ww));        
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, getValuesFromIndex(w,locs) );
        
        fAccSig = Complex.abs(freqz(accZ,ww));        
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, getValuesFromIndex(w,locs) );
        
        
        double accCloseFreqMax = -1;
        
        for(int i=0; i < locs.length; i++){
            for(int j=0; j<dominantPeaksOfAcc.size();j++){
                
                if( Math.abs( peakFreqs[i] - dominantPeaksOfAcc.get(j) ) > 5 ){
                    if( peakFreqs[i] > accCloseFreqMax ){
                        accCloseFreqMax = peakFreqs[i];
                    }
                }
            }
        }
        
        if( accCloseFreqMax != -1 ){
            fPrev = accCloseFreqMax;
            return fPrev;
        }
        
        // else return strongest peak location
        int index = maxIndex(fSig);
        fPrev = getValuesFromIndex(w,locs)[0];
        return fPrev;
    }
    
    /**
     * 
     * @param d
     * @return 
     */
    public static int maxIndex(double[] d){
        
        int index = -1;
        double maxVal = Double.MIN_VALUE;
        for(int i=0;i<d.length;i++){
            if( d[i] > maxVal ){
                index = i;
                maxVal = d[i];
            }
        }
        
        return index;
    }
    
    /**
     * 
     * @param list
     * @param d 
     */
    public static void extendList(ArrayList<Double> list, double[] d){
        for(int i=0;i<d.length;i++){
            list.add(d[i]);
        }
    }
    
    /**
     * 
     * @param array
     * @param indices
     * @return 
     */
    public static double[] getValuesFromIndex(double[] array,int[] indices){
        
        double[] vals = new double[indices.length];
        for(int i=0; i < indices.length; i++){
            vals[i] = array[indices[i]];
        }
        return vals;
    }
    
    /**
     * 
     * @param sig
     * @param threshold
     * @return 
     */
    public static int[] findPeakLocation(double[] sig, double threshold){
        
        ArrayList<Integer> locs = new ArrayList<>();
        for(int i=0; i<sig.length; i++){
            if( sig[i] >= threshold ){
                locs.add(i);
            }
        }
        
        int[] loc = new int[locs.size()];
        for(int i=0;i<locs.size();i++){
            loc[i] = locs.get(i);
        }
        
        return loc;
    }
    
    /**
     * TODO
     * @param signal
     * @param freqs
     * @return 
     */
    public static Complex[] freqz( double[] signal, double[] freqs ){
        
        
        
        return null;
        
    }
    
    
    public static double[] linspace(int strt,int end,int len){
        
        double step;
        if(1!=len) {
            step = 1.0 * ( end - strt  ) / (len-1);
        }
        else{
            step = 0;
        }
        double[] lin = new double[len];
        
        double val = strt;
        for(int i=0;i<len;i++){
            lin[i] = val;
            val += step;
        }
        
        return lin;
    }
    
    
    
    //TODO
    /**
     * 
     * @param rSig signal to be filtered
     * @param fSampling sampling frequency
     * @param num
     * @return 
     * @throws java.io.FileNotFoundException 
     */
    public static double[] myBandPass(double[] rSig, double fSampling,int num) throws FileNotFoundException{
        
        
        double[] b; 
        switch (num) {
            case 1:
                b = getArray("filter_coeff/mypass.dat");
                break;
            case 2:
                b = getArray("filter_coeff/mypass1.dat");
                break;
            default:
                b = null;
                break;
        }
        double[] a = {1.0};
        
        double[] output = DSP.filter(b, a, rSig);
        
        return output;
        
    }
    
   /**
    * 
    * @param l 
    *       arrayList to convert to double array
    * @return 
    */ 
    public static double[] toDoubleArray(ArrayList<Double> l){
        double [] d = new double[l.size()];
        
        for(int i=0; i < l.size(); i++){
            d[i] = l.get(i);
        }
        
        return d;
    }
    
    // TODO:
    /**
     * 
     * @param lParameter parameter of RLS filter
     * @param hSig helper signal
     * @param mainSig main signal
     * @return filtered signal
     */
    
    public static double[] rlsFilter(int lParameter,double[] hSig,double[] mainSig){
        
        return null;
    }
    
    
    
    /**
     * 
     * @param fileName
     * @return ArrayList of data
     * @throws java.io.FileNotFoundException
     */
    
    public static double[] getArray(String fileName) throws FileNotFoundException{
        
        Scanner scanner = new Scanner(new FileReader("test/"+fileName));
        
        ArrayList<Double> dataList;
        dataList = new ArrayList<>();
        //System.out.println(scanner.nextLine());
        while(scanner.hasNextDouble()){
            dataList.add(scanner.nextDouble());
        }
       
        return toDoubleArray(dataList);
        
    }

    private static double[] maxFindFromThreshold(double[] yCropped, double d, int fSampling) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static double[] filterArray(ArrayList<Double> fRlsSet, int min, int max) {
        return filterArray( toDoubleArray(fRlsSet) , min, max);
    }
    
  
    
    
    
    
}

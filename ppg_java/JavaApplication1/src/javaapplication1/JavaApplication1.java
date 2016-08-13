/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

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
        
        double deltaCount = 0;
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
                    fPrev, deltaCount ,fSampling,peaks
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
                        double S_org = findSignalPeaks( 
                                getValuesFromIndex(ppgSignal1, currentSegment),
                                getValuesFromIndex(ppgSignal2, currentSegment),
                                fPrev,10,fSampling
                        );
                        
                        double S_a0 = findSignalPeaks( 
                                getValuesFromIndex(accDataX, currentSegment),
                                getValuesFromIndex(accDataY, currentSegment),
                                getValuesFromIndex(accDataZ, currentSegment),
                                fPrev,5.0,fSampling
                        );
                        
                        
                        if( Math.abs(S_a0 - S_org) > 3 ){
                            freqEstimates = S_org;
                        }else{
                            freqEstimates = fPrev;
                        }
                        
                    }
                    
                }
                
                
                
            }
            
            // TODO:  call_nlms2
            fPrev = freqEstimates;
            
            
        }
         
         
    }
    
    /**
     * 
     * @param signal1
     * @param signal
     * @param fPrev
     * @param range
     * @param fSampling
     * @return 
     */
    public static double findSignalPeaks(double [] signal1,double [] signal2,  double fPrev,double range, double fSampling ){
        
        double[] w = linspace(fPrev-range, fPrev+range, 100);
        double[] ww = DSP.times(w, 2 * Math.PI / (fSampling * 60 ) );
        
        double[] fy1 = DSP.power(absFreqz(signal1, ww) , 2 );
        double[] fy2 = DSP.power(absFreqz(signal2, ww) , 2 );
        int[] loc1 = findPeakLocation(fy1);
        int[] loc2 = findPeakLocation(fy2);
        
        int[] pksLoc = concateArray(loc1, loc2);
        
        double[] cand = getValuesFromIndex(w, pksLoc);
        
        if( cand.length!=0 ){
            int pos = (int)(minValLoc( DSP.abs( DSP.minus(cand, fPrev) ) ) )[1];
            return cand[pos];
        }
        else 
            return fPrev;
       
    }
    
    
    public static double findSignalPeaks(double [] signal1,double [] signal2,double[] signal3, double fPrev,double range, double fSampling ){
        
        double[] w = linspace(fPrev-range, fPrev+range, 100);
        double[] ww = DSP.times(w, 2 * Math.PI / (fSampling * 60 ) );
        
        double[] fy1 = DSP.power(absFreqz(signal1, ww) , 2 );
        double[] fy2 = DSP.power(absFreqz(signal2, ww) , 2 );
        double[] fy3 = DSP.power(absFreqz(signal3, ww) , 2 );
        int[] loc1 = findPeakLocation(fy1);
        int[] loc2 = findPeakLocation(fy2);
        int[] loc3 = findPeakLocation(fy3);
        int[] pksLoc = concateArray( concateArray(loc1, loc2) , loc3);
        
        double[] cand = getValuesFromIndex(w, pksLoc);
        
        if( cand.length!=0 ){
            int pos = (int)(minValLoc( DSP.abs( DSP.minus(cand, fPrev) ) ) )[1];
            return cand[pos];
        }
        else 
            return fPrev;
       
    }
    
    /**
     * 
     * @param signal1
     * @param signal2
     * @param freq
     * @param range
     * @param fSampling
     * @return 
     */
    public static double callNLMS(double[] signal1, double[] signal2,double freq, double range,double fSampling){
        
        double[] w = linspace(freq-range, freq+range, 4000);
        double[] ww = DSP.times(w, 2 * Math.PI / (fSampling * 60));
        double[] fy1 = DSP.power(absFreqz(signal1, ww) , 2 );
        double[] fy2 = DSP.power(absFreqz(signal2, ww) , 2 );
        int[] loc1 = findPeakLocation(fy1);
        int[] loc2 = findPeakLocation(fy2);
        
        int[] pksLoc = concateArray(loc1, loc2);
        
        double[] cand = getValuesFromIndex(w, pksLoc);
        if( cand.length!=0 ){
            int pos = (int)(minValLoc( DSP.abs( DSP.minus(cand, freq) ) ) )[1];
            return cand[pos];
        }
        else 
            return freq;
        
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
        
        double freqEst1 = -1;
        double freqEst2 = -1;
        
        double[] ch1 = timeDomain(sig1,accX,accY,accZ,fPrev,multiplier);
        double[] ch2 = timeDomain(sig2,accX,accY,accZ,fPrev,multiplier);
        
        if(ch1.length!=1){
            double[] w = linspace(0, 200, 1000);
            double[] ww = DSP.times(w, 2 * Math.PI / (125 * 60 * multiplier) );
            
            double[] yy1 = DSP.power(absFreqz(ch1, ww) , 2 );
            
            int[] loc = findPeakLocation(yy1);
            
            double[] w_ = getValuesFromIndex(w, loc);
            int minloc = (int)(minValLoc( DSP.abs( DSP.minus(w, fPrev) ) ) )[1];
            
            freqEst1 = w_[minloc];
            
        }
        if(ch2.length!=1){
            double[] w = linspace(0, 200, 1000);
            double[] ww = DSP.times(w, 2 * Math.PI / (125 * 60 * multiplier) );
            
            double[] yy1 = DSP.power(absFreqz(ch2, ww) , 2 );
            
            int[] loc = findPeakLocation(yy1);
            
            double[] w_ = getValuesFromIndex(w, loc);
            int minloc = (int)(minValLoc( DSP.abs( DSP.minus(w, fPrev) ) ) )[1];
            
            freqEst2 = w_[minloc];
            
        }
        
        double freqEst = -1;
        
        double bd = 12;
        
        double d1 = Math.abs( freqEst1 - fPrev );
        double d2 = Math.abs( freqEst2 - fPrev );
        
        if( d1 <= bd && d2 <= bd ){
            if( d1 <= d2 ){
                freqEst = freqEst1;
            }else{
                freqEst = freqEst2;
            }
        }
        else if( d1 <= bd ){
            freqEst = freqEst1;
        }
        else{
            freqEst = freqEst2;
        }
        
        
        return freqEst;
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
    
    public static double[] awgn(double[] sig, double SNR){
        
        int len = sig.length;
        //double[] asig = new double[len];
        double std = Math.sqrt(  powerOfSignal(sig) / Math.pow(10, SNR/10.0)  );
        //System.out.println("STD:"+std);
        double[] noise = DSP.times( randn(len,0) , std ) ;
       // System.out.print("awgn:");
       // System.out.println(powerOfSignal(sig));
       // DSP.printArray( noise );
        return DSP.plus(sig, noise);
    }
    
    /**
     * 
     * @param len
     * @return 
     */
    public static double[] randn(int len){
        
        Random r = new Random();
        double[] rn = new double[len];
        for (int i = 0; i < len; i++) {
            rn[i] = r.nextGaussian();
        }       
        return rn;
    }
    
    /**
     * 
     * @param len
     * @param seed
     * @return 
     */
    public static double[] randn(int len,int seed){
        
        Random r = new Random(seed);
        double[] rn = new double[len];
        for (int i = 0; i < len; i++) {
            rn[i] = r.nextGaussian();
        }       
        return rn;
    }
    
    /**
     * 
     * @param sig
     * @return 
     */
    public static double powerOfSignal(double[] sig){
        int len = sig.length;
        double sum = 0;
        for (double d : sig) {
            sum += d*d;
        }
        return sum / len;
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
           double[] accY, double[] accZ, double fPrev, double deltaCount ,double fSampling, double[] peaks){
     
        double freqEstimates = -1;
        int NE = 8;
        double SNR = 30;
        int imfToChose;
        
        if(fSampling==25)
            imfToChose = 1;
        else if(fSampling==125)
            imfToChose = 2;
        else if(fSampling==250)
            imfToChose = 3;
        else if(fSampling==500)
            imfToChose = 4;
        else
            imfToChose = 0;
        
        double[] imf1 = new double[sig1.length];
        double[] imf2 = new double[sig2.length];
        
        for (int i = 0; i < imf1.length; i++) {
            imf1[i] = 0;
            imf2[i] = 0;
        }
        
        
        for (int i = 0; i < NE; i++) {
           
            double[] sigWithNoise = awgn(sig1, SNR);
            imf1 = DSP.plus( imf1 ,  nwem(sigWithNoise,imfToChose) );
            
            sigWithNoise = awgn(sig2, SNR);
            imf2 = DSP.plus( imf2 ,  nwem(sigWithNoise,imfToChose) );
            
        }
        
        imf1 = DSP.times( imf1 , 1.0 / imf1.length );
        imf2 = DSP.times( imf2 , 1.0 / imf2.length );
        
        double[] S_imf = new double[2];
        S_imf[0] = maxFind( imf1, fSampling );
        S_imf[1] = maxFind( imf2, fSampling );
        
        double threshold = 0.5;
        ArrayList<Double> S_a = new ArrayList<>();
        
        double[] pksVector = maxFindFromThreshold( accX, threshold, fSampling );
        if( pksVector.length > 3 ){
            pksVector = subList(pksVector, 3);
        }
        extendList(S_a, pksVector);
        
        ArrayList<Double> S_imf_a_3 = new ArrayList<>();
        ArrayList<Double> G2 = new ArrayList<>();
        
        for (int i = 0; i < S_imf.length; i++) {
            
            if( DSP.min( DSP.abs( DSP.minus( S_a ,  S_imf[i] ) ) ) > 3 ){
                S_imf_a_3.add( S_imf[i] );
            }
            
            if( DSP.min( DSP.abs( DSP.minus( S_a ,  S_imf[i] ) ) ) > 2 ){
                G2.add( S_imf[i] );
            }
            
        }
        
        peaks = toDoubleArray(S_imf_a_3);
        if( G2.size()==0 ){
            return freqEstimates;
        }
        
        double f_AC;
        
        if( DSP.max(G2) - DSP.min(G2) <= 2 ){
            f_AC = DSP.mean(G2);
        }else{
            int loc = (int)(minValLoc( DSP.abs( DSP.minus(G2, fPrev))))[1];
            f_AC = G2.get(loc);
        }
        
        double delta_0 = 5;
        double delta_d = 1;

        double delta_AC = delta_0 + deltaCount * delta_d;
        
        if( Math.abs(f_AC - fPrev) < delta_AC ){
            freqEstimates = f_AC;
        }
        
        return freqEstimates;
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
    
    public static double[] subList(double[] doubleArray, int strIndex,int endIndex){
        int len = endIndex - strIndex + 1;
        double[] arr = new double[len];
        
        for (int i = 0; i < len; i++) {
            arr[i] = doubleArray[strIndex+i];
        }
        
        return arr;
    }
    
    
    public static double w(double loc, int N, float fSampling){
        return loc / N * (60 * fSampling) + 50;
    }
    
    public static double[] w(double[] locs, int N, float fSampling){
        
        double[] arr = new double[locs.length];
        for (int i = 0; i < locs.length; i++) {
            arr[i] = w(locs[i],N,fSampling);
            
        }
        return arr;
        
    }
    public static double[] w(int[] locs, int N, float fSampling){
        
        double[] arr = new double[locs.length];
        for (int i = 0; i < locs.length; i++) {
            arr[i] = w(locs[i],N,fSampling);
            
        }
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
        //double[] w = linspace(50,150,4096);
        
        //double[] ww = DSP.times( w, 2 * Math.PI /(fSampling * 60.0) );
       
        int N = (int)Math.pow(2, 17);
        double[] fSig = absFreqz(sig,N);
      
        // get indices
        int strtIndex = (int)( 50 / (60*fSampling)* N );
        int endIndex = (int)( 150 / (60*fSampling)* N );
        
        fSig = subList(fSig, strtIndex, endIndex);
        
        int[] locs = findPeakLocation(fSig, 0.8 * DSP.max(fSig));
        
        if(locs.length == 1){
            fPrev = w(locs[0],N,fSampling);
            return fPrev;
        }
        
        //  if more than one frequencies have value above 80% 
        //  then we look for their second harmonics
        
        double[] peakFreqs = w(locs,N,fSampling);
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
        
        double[] fAccSig = absFreqz(accX);        
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, w(locs,N,fSampling) );
        
        fAccSig = absFreqz(accY);        
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, w(locs,N,fSampling) );
        
        fAccSig = absFreqz(accZ);      
        locs = findPeakLocation(fAccSig, 0.8 * DSP.max(fAccSig));
        extendList(dominantPeaksOfAcc, w(locs,N,fSampling) );
        
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
        fPrev = w(locs,N,fSampling)[0];
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
    
    public static double maxVal(double[] d){
        int n = maxIndex(d);
        return d[n];
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
    
    public static int[] concateArray(int[] a1,int[] a2){
        int[] a = new int[a1.length+a2.length];
        System.arraycopy(a1, 0, a, 0, a1.length);
        System.arraycopy(a2, 0, a, a1.length, a2.length);
        return a;
        
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
        
        int[] locs = findPeakLocation(sig); // get all local maximum
        
        ArrayList<Integer> locList = new ArrayList<>();
        
        double thrVal = threshold ;//* maxVal(sig);
        
        for (int loc : locs) {
            if( sig[loc] >= thrVal ){
                locList.add(loc);
            }
        }
        
        return toIntArray(locList);
        
    }
    
    /**
     * 
     * @param sig
     * @param threshold
     * @param minPeakDistance
     * @param minpeakDistance
     * @return 
     */
    public static int[] findPeakLocation( double[] sig,double threshold,double minPeakDistance ){
        
        int[] locs = findPeakLocation(sig, threshold);
        double[] arr = getValuesFromIndex(sig, locs);
        sort(arr,false);
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]==-1){
                continue;
            }
            list.add(arr[i]);
            for (int j = i+1; j < arr.length; j++) {
                if(arr[j]==-1)continue;
                if( ( arr[i] - arr[j] ) <= minPeakDistance ){
                    arr[j] = -1;
                }
            }
        }
        
        // get indices
        int[] indices = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            
            for (int j = 0; j < sig.length; j++) {
                if( sig[j] == arr[i] ){
                    indices[i] = j;
                    break;
                }
            }
            
        }
        
        return indices;
    }
    
    public static int[] sortWithIndex(double[] array,boolean ascend){
        
        return ArrayIndexComparator.sort(array, ascend);
    }
    
    public static void sort(double[] arr,boolean ascend ){
        
        Arrays.sort(arr);
        if(ascend == false){
            reverse(arr);
        }
        
    }
    
     public static void reverse(double[] arr){
        
         int len = arr.length;
         int hlen = (int)arr.length/2;
         for (int i = 0; i < hlen; i++) {
             double tmp = arr[i];
             arr[i] = arr[len - i - 1];
             arr[len - i - 1] = tmp;
             
         }
    }
    
   // TODO:    
    public static double[] absFreqz(double[] nsig,double[] w ){
        
        int N = (int) Math.pow(2, 17);
        double[] fr = absFreqz(nsig, N);
        
        
        return null;
    }
     
    
    // TODO:......
    
    public static double[] absFreqz( double[] nsig){
    
        
        return null;
    }
     
    public static double[] absFreqz( double[] nsig, int N){
        
        // if signal length less than N, then zero pad
        double[] sig;
        if( nsig.length < N ){           
            sig = zeroPad(nsig,N-nsig.length);         
        }else{
            sig = nsig;
        }
        
        FFT fft = new FFT(N);
        double[] re = new double[N];
        System.arraycopy(sig, 0, re, 0 , N); // re vector initialized with sig       
        double[] imag = DSP.zeroVec(N); // imag vector initialized by zero
        
        fft.fft(re, imag);
        
        double[] resp = new double[N]; // frequency response
        
        for (int i = 0; i < N; i++) {
            resp[i] = Math.sqrt( re[i]*re[i] + imag[i]*imag[i]);
        }
        
        return resp;
        
    }
    
    /**
     *
     * @param b
     * @param s
     * @return
     */
    public static Complex polyVal(double[] b, Complex s ){
        
        int len = b.length;
        Complex sum = new Complex(0,0);
        
        for(int i=0; i<len; i++){
            
            sum = sum.plus( s.pow(i).times( b[i] ) );
            
        }      
        return sum;
    }
    
    
    public static double[] linspace(int strt,int end,int len){
        
        return linspace((double)strt, (double)end, len);
        
    }
    
    public static double[] linspace(double strt,double end,int len){
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
    
    
    public static int[] toIntArray(ArrayList<Integer> l){
        int [] d = new int[l.size()];
        
        for(int i=0; i < l.size(); i++){
            d[i] = l.get(i);
        }
        
        return d;
    }
    
    
    
    // TODO:
    /**
     * 
     * @param lParameter parameter of RLS filter
     * @param dSig
     * @param hSig helper signal
     * @param mainSig main signal
     * @return filtered signal
     */
    
    public static double[] rlsFilter(int lParameter,double[] dSig,double[] mainSig){
        
        int iteration = 10000;
        RLSAlgo rls = new RLSAlgo(lParameter,iteration);
        rls.setXD(dSig, mainSig);
        rls.compute();
        return rls.filter();
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

   

    private static double[] filterArray(ArrayList<Double> fRlsSet, int min, int max) {
        return filterArray( toDoubleArray(fRlsSet) , min, max);
    }

    private static double[] nwem(double[] sigWithNoise, int imfToChose) {
        
        double[] imf = Emd.getImf(sigWithNoise, imfToChose);      
        return imf;
      
    }

    public static double maxFind(double[] imf1, double fSampling) {
        
        int N = 1024;
        double[] Py = absFreqz(imf1, N); // power spectrum square of imf1
        int loc = maxIndex(Py);
        
        return 200.0/(1024-1) * (loc - 1);
        
        
    }

    private static double[] maxFindFromThreshold(double[] accX, double threshold, double fSampling) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int N = 4096;
        double[] Py = absFreqz(accX, N); // power spectrum square of imf1
        int[] locs = findPeakLocation(Py, threshold, 3);
        
        double tmp = 300.0 / (4096 - 1);
        
        return DSP.times( DSP.minus(locs, 1)  , tmp);
        
    }

    private static double[] timeDomain(double[] sig1, double[] accX, double[] accY, double[] accZ, double fPrev, int multiplier) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
       double[] en = DSP.plus( DSP.plus( DSP.power(accX, 2)  , DSP.power(accY, 2)) , DSP.power(accZ, 2));
       
       double tt = 0.1 * maxVal(en);
       
       //ArrayList<Integer> bin = new ArrayList<>();
       
       int[] bin = new int[en.length];
       
        for (int i = 0; i < en.length; i++) {
            if( en[i] > tt ){
                bin[i] = 1;
            }
            else{
                bin[i] = 0;
            }
        }
        
        int cl=0;
        int mxl=0;
        int mxst=0;
        int st=1;
        
        for (int i = 0; i < 200.0*5*multiplier; i++) {
            if(bin[i]==0){
                cl++;
                if(cl>=mxl){
                    mxl = cl;
                    mxst = st;
                }else{
                    cl = 0;
                    st = i + 1;
                }
            }
        }
        
        double [] y;
        
        if(mxst+mxl-1<=200*5*multiplier){
            int[] I = DSP.irange(mxst, mxst+mxl-1);
            
            if(I.length >= 80*5*multiplier){
                int kkk = I[I.length-1];
                I = DSP.irange( (kkk-400*multiplier+1),kkk );
            }
            
            if( ( I.length>27*5*multiplier && I[I.length-1]>150*5*multiplier ) || I.length>60*5*multiplier ){
                y = new double[I.length];
                for (int i = 0; i < I.length; i++) {
                    y[i] = sig1[i];
                }
            }else{
                y = null;
            }
        }else{
            y = null;
        }
    
       return y;
    }

    /**
     * find local maximum of sig
     * @param sig
     * @return 
     */
    public static int[] findPeakLocation(double[] sig) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       ArrayList<Integer> locs = new ArrayList<>();
       int[] locs_;
       int len = sig.length;
       if(len==0)return null;
       else if(len==1 ){
           locs_ = DSP.zeroVecInt(1);
           return locs_;
       }; 
       
       for (int i = 0; i < len; i++) {
            
            if( i == 0  ){ // first location
                if( sig[i] > sig[i+1] ){
                    locs.add(i);
                }
            }
            else if(i == len-1) // last location
            {
                if( sig[i]>sig[i-1] ){
                    locs.add(i);
                }
            }
            else{
                
                if( sig[i] > sig[i-1] && sig[i] > sig[i+1] ){
                    locs.add(i);
                }
                
            }
            
       }
       
       return toIntArray(locs);
    }
    
    private static double[] maxFindFromThreshold(double[] y, double d, int fSampling) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static double[] zeroPad(double[] nsig, int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        double[] new_sig = new double[nsig.length+i];
        for (int j = 0; j < new_sig.length ; j++) {
            if(j<nsig.length){
                new_sig[j] = nsig[j];
            }
            else{
                new_sig[j] = 0;
            }
        }
        return new_sig;
    }

}

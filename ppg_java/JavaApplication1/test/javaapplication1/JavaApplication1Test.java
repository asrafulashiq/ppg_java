/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;


/**
 *
 * @author mac
 */
public class JavaApplication1Test {
    
    public JavaApplication1Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //JavaApplication1 instance = new JavaApplication1();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class JavaApplication1.
     * @throws java.lang.Exception
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        JavaApplication1.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    
    
    

    /**
     * Test of rlsFilter method, of class JavaApplication1.
     */
    @Test
    public void testRlsFilter() {
        System.out.println("rlsFilter");
        int lParameter = 0;
        double[] hSig = null;
        double[] mainSig = null;
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = null;
        double[] result = JavaApplication1.rlsFilter(lParameter, hSig, mainSig);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getArray method, of class JavaApplication1.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetArray() throws Exception {
        System.out.println("getArray");
        String fileName = "filter_coeff/mypass.dat";
        double[] expResult = null;
        double[] result = JavaApplication1.getArray(fileName);
        
        
        //DSP.printArray(result);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }


    

    /**
     * Test of linspace method, of class JavaApplication1.
     */
    @Test
    public void testLinspace() {
        System.out.println("linspace");
        int strt = 1;
        int end = 5;
        int len = 5;
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = {1,2,3,4,5};
        double[] result = instance.linspace(strt, end, len);
        assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testLinspace1() {
        System.out.println("linspace");
        int strt = 1;
        int end = 2;
        int len = 5;
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = {1.0,1.25,1.5,1.75,2.0};
        double[] result = instance.linspace(strt, end, len);
        DSP.printArray(result);
        assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testLinspace2() {
        System.out.println("linspace");
        int strt = 1;
        int end = 1;
        int len = 1;
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = {1.0};
        double[] result = instance.linspace(strt, end, len);
        DSP.printArray(result);
        assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testLinspace3() {
        System.out.println("linspace");
        int strt = 1;
        int end = 0;
        int len = 0;
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = new double[0];
        double[] result = instance.linspace(strt, end, len);
       // DSP.printArray(result);
        assertArrayEquals(result,expResult,0.001);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of toDoubleArray method, of class JavaApplication1.
     */
    @Test
    public void testToDoubleArray() {
        System.out.println("toDoubleArray");
        ArrayList<Double> l = new ArrayList<>();
        l.add(1.0);
        l.add(2.0);
        double[] expResult = {1.0,2.0};
        
        double[] result;
        result = JavaApplication1.toDoubleArray(l);
        assertArrayEquals(expResult, result,0.01);
        
    }

    /**
     * Test of subList method, of class JavaApplication1.
     */
    @Test
    public void testSubList() {
        System.out.println("subList");
        double[] doubleArray = {1,2,3,4,5,6};
        int len = 3;
        double[] expResult = {1,2,3};
        double[] result = JavaApplication1.subList(doubleArray, len);
        assertArrayEquals(expResult, result,0.01);
    }

    /**
     * Test of initialize method, of class JavaApplication1.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        double[] sig = null;
        double[] accX = null;
        double[] accY = null;
        double[] accZ = null;
        float fSampling = 0.0F;
        float expResult = 0.0F;
        double result = JavaApplication1.initialize(sig, accX, accY, accZ, fSampling);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of myBandPass method, of class JavaApplication1.
     * @throws java.lang.Exception
     */
    @Test
    public void testMyBandPass() throws Exception {
        System.out.println("myBandPass");
        double[] rSig = {1,2,3,4,5};
        double fSampling = 0.0;
        double[] expResult = null;
        double[] result = JavaApplication1.myBandPass(rSig, fSampling,1);
        //assertArrayEquals(expResult, result,0.01);
        DSP.printArray(result);
        
        double[] a = {1,2,3};
        a = JavaApplication1.myBandPass(a,fSampling,1);
        DSP.printArray(a);
        
    }

    /**
     * Test of findPeakLocation method, of class JavaApplication1.
     */
    @Test
    public void testFindPeakLocation() {
        System.out.println("findPeakLocation");
        double[] sig = {1,4,2,6,4,8,7,10,4,-3,4,7,2};
        //double threshold = 8.0;
        int[] expResult = {1,3,5,7,11};
        int[] result = JavaApplication1.findPeakLocation(sig);
        assertArrayEquals(expResult, result);
        
    }

    /**
     * Test of findPeakLocation method, of class JavaApplication1.
     */
    @Test
    public void testFindPeakLocation2() {
        System.out.println("findPeakLocation");
        double[] sig = {1,4,2,6,4,8,7,10,4,-3,4,7,2};
        double threshold = 0.5;
        int[] expResult = {3,5,7,11};
        int[] result = JavaApplication1.findPeakLocation(sig,threshold);
        assertArrayEquals(expResult, result);
        
    }
    
    /**
     * Test of findPeakLocation method, of class JavaApplication1.
     */
    @Test
    public void testFindPeakLocation3() {
        System.out.println("findPeakLocation");
        double[] sig = {1,4,2,6,4,8,7,10,4,-3,4,7,2};
        double threshold = 0.5;
        int[] expResult = {3,5,7,11};
        int[] result = JavaApplication1.findPeakLocation(sig,threshold);
        assertArrayEquals(expResult, result);
        
        DSP.printArray(result);
    }
    
    /**
     * Test of findPeakLocation method, of class JavaApplication1.
     */
    @Test
    public void testFindPeakLocation3_1() {
        System.out.println("findPeakLocation");
        double[] t = DSP.irange(0.0,10.0,0.1);
        double[] sig =  DSP.plus( DSP.sin(t) , DSP.times( DSP.sin( DSP.times(t, 5) ),0.5) );
        double threshold = 0.5;
        int[] expResult = { 16,    79,     4,    67,    90,   28 };
        int[] result = JavaApplication1.findPeakLocation(sig,threshold);
        DSP.printArray(result);
        
        for (int i = 0; i < result.length; i++) {
            System.out.println( sig[result[i]] );
        }
        
        assertArrayEquals(expResult, result);
        
        
    }
    
    @Test
    public void testSortWithIndex(){
        
        System.err.println("sortWithIndex");
        
        double[] a = {3,2,100,-4,0,25,4,19};
        int[] expResult = {2,5,7,6,0,1,4,3};
         JavaApplication1 instance = new JavaApplication1();
         int[] result = instance.sortWithIndex(a, false);
         assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getValuesFromIndex method, of class JavaApplication1.
     */
    @Test
    public void testGetValuesFromIndex() {
        System.out.println("getValuesFromIndex");
        double[] array = {3,2,6,5,100,200};
        int[] indices = {0,2,3};
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = {3,6,5};
        double[] result = instance.getValuesFromIndex(array, indices);
        assertArrayEquals(expResult, result,0.01);
        
    }

    
    @Test
    public void testSort(){
        System.out.println("Sort");
        double[] arr = {3.3,2.2,1.1,0,10};
        JavaApplication1 instance = new JavaApplication1(); 
        double[] expResult = {10,3.3,2.2,1.1,0};
        instance.sort(arr, false);
        DSP.printArray(arr);
        assertArrayEquals(expResult, arr,0.01);
        
    }
    
    @Test
    public void testReverse(){
        System.err.println("Reverse");
        double[] arr={3.3, 4, 4.1, 2.1, 1};
        double[] expResult = {1, 2.1, 4.1, 4, 3.3};
        JavaApplication1 instance = new JavaApplication1();
        instance.reverse(arr);
        assertArrayEquals(expResult, arr,0.01);
    }
    
    /**
     * Test of extendList method, of class JavaApplication1.
     */
    @Test
    public void testExtendList() {
        System.out.println("extendList");
        ArrayList<Double> list = new ArrayList<>();
        list.add(5.5);
        double[] d = {1.1,3.3};
        JavaApplication1.extendList(list, d);
        System.out.println( list.toString() );
        
        
    }

    /**
     * Test of maxIndex method, of class JavaApplication1.
     */
    @Test
    public void testMaxIndex() {
        System.out.println("maxIndex");
        double[] d = {0.0,-2000,100,400,200};
        int expResult = 3;
        int result = JavaApplication1.maxIndex(d);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of minValLoc method, of class JavaApplication1.
     */
    @Test
    public void testMinValLoc() {
        System.out.println("minValLoc");
        double[] list = {-1,0,2,3,4};
        JavaApplication1 instance = new JavaApplication1();
        double[] expResult = {-1,0};
        double[] result = instance.minValLoc(list);
        assertArrayEquals(expResult, result,0.01);
    }

    /**
     * Test of doEEMD method, of class JavaApplication1.
     */
    @Test
    public void testDoEEMD() {
        System.out.println("doEEMD");
        double[] sig1 = null;
        double[] sig2 = null;
        double[] accX = null;
        double[] accY = null;
        double[] accZ = null;
        double fPrev = 0.0;
        double fSampling = 0.0;
        double[] peaks = null;
        double expResult = 0.0;
        double result = JavaApplication1.doEEMD(sig1, sig2, accX, accY, accZ, fPrev,0 ,fSampling, peaks);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of filterArray method, of class JavaApplication1.
     */
    @Test
    public void testFilterArray() {
        System.out.println("filterArray");
        double[] a = {1.1,10,20.0,40,50};
        double min = 10;
        double max = 40;
        double[] expResult = {10,20,40};
        double[] result = JavaApplication1.filterArray(a, min, max);
        assertArrayEquals(expResult, result,0.01);
    }

    /**
     * Test of frequency_estimate method, of class JavaApplication1.
     */
    @Test
    public void testFrequency_estimate() {
        System.out.println("frequency_estimate");
        double[] sig1 = null;
        double[] sig2 = null;
        double[] accX = null;
        double[] accY = null;
        double[] accZ = null;
        double fPrev = 0.0;
        int multiplier = 0;
        double expResult = 0.0;
        double result = JavaApplication1.frequency_estimate(sig1, sig2, accX, accY, accZ, fPrev, multiplier);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    

   

    /**
     * Test of absFreqz method, of class JavaApplication1.
     */
    @Test
    public void testAbsFreqz() {
        System.out.println("absFreqz");
        double[] b = DSP.range(0,10000);
        int N = 4096;
        //double[] expResult = null;
        double[] result = JavaApplication1.absFreqz(b, N);
        //assertArrayEquals(expResult, result,0.01);
        DSP.printArray(result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of polyVal method, of class JavaApplication1.
     */
    @Test
    public void testPolyVal() {
        System.out.println("polyVal");
        double[] b = null;
        Complex s = null;
        Complex expResult = null;
        Complex result = JavaApplication1.polyVal(b, s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of awgn method, of class JavaApplication1.
     */
    @Test
    public void testAwgn() {
        System.out.println("awgn");
        double[] sig = {1,2,3,1};
        double SNR = 30;
        double[] expResult = null;
        double[] result =  JavaApplication1.awgn(sig, SNR);
        //assertArrayEquals(expResult, result,0.01);
        DSP.printArray(result,"awgn");
        System.err.println("-----");
        assertEquals(result.length,4);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of randn method, of class JavaApplication1.
     */
    @Test
    public void testRandn() {
        System.out.println("randn");
        int len = 4;
        int seed = 0;
        double[] expResult = {0.5377};
        double[] result = JavaApplication1.randn(len, seed);
        DSP.printArray(result);
        //assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of powerOfSignal method, of class JavaApplication1.
     */
    @Test
    public void testPowerOfSignal() {
        System.out.println("powerOfSignal");
        double[] sig = {2,4,3};
        double expResult = 9.667;
        double result = JavaApplication1.powerOfSignal(sig);
        assertEquals(expResult, result, 0.01);
        
    }

    

    

    /**
     * Test of concateArray method, of class JavaApplication1.
     */
    @Test
    public void testConcateArray() {
        System.out.println("concateArray");
        int[] a1 = {1,3,2};
        int[] a2 = {5,6};
        int[] expResult = {1,3,2,5,6};
        int[] result = JavaApplication1.concateArray(a1, a2);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }

    /**
     * Test of findSignalPeaks method, of class JavaApplication1.
     */
    @Test
    public void testFindSignalPeaks_5args() {
        System.out.println("findSignalPeaks");
        double[] signal1 = null;
        double[] signal2 = null;
        double fPrev = 0.0;
        double range = 0.0;
        double fSampling = 0.0;
        double expResult = 0.0;
        double result = JavaApplication1.findSignalPeaks(signal1, signal2, fPrev, range, fSampling);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findSignalPeaks method, of class JavaApplication1.
     */
    @Test
    public void testFindSignalPeaks_6args() {
        System.out.println("findSignalPeaks");
        double[] signal1 = null;
        double[] signal2 = null;
        double[] signal3 = null;
        double fPrev = 0.0;
        double range = 0.0;
        double fSampling = 0.0;
        double expResult = 0.0;
        double result = JavaApplication1.findSignalPeaks(signal1, signal2, signal3, fPrev, range, fSampling);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of callNLMS method, of class JavaApplication1.
     */
    @Test
    public void testCallNLMS() {
        System.out.println("callNLMS");
        double[] signal1 = null;
        double[] signal2 = null;
        double freq = 0.0;
        double range = 0.0;
        double fSampling = 0.0;
        double expResult = 0.0;
        double result = JavaApplication1.callNLMS(signal1, signal2, freq, range, fSampling);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of zeroPad method, of class JavaApplication1.
     */
    @Test
    public void testZeroPad() {
        System.out.println("zeroPad");
        double[] nsig = {2,5,3,1,7};
        int i = 5;
        double[] expResult = {2,5,3,1,7,0,0,0,0,0};
        double[] result = JavaApplication1.zeroPad(nsig, i);
        assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMaxFind() {
        System.out.println("maxFind");
        double[] nsig = {2,5,3,1,7};
        int i = 5;
        double[] expResult = {2,5,3,1,7,0,0,0,0,0};
        double[] result = JavaApplication1.zeroPad(nsig, i);
        assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

   
}

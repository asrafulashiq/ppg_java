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
import static org.junit.Assert.assertTrue;
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
        //fail("The test case is a prototype.");
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
        DSP.printArray(result);
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
        double[] sig = {9.5,3.0,10,8,8.8};
        double threshold = 8.0;
        int[] expResult = {0,2,3,4};
        int[] result = JavaApplication1.findPeakLocation(sig, threshold);
        assertArrayEquals(expResult, result);
        
    }

    /**
     * Test of freqz method, of class JavaApplication1.
     */
    @Test
    public void testFreqz() {
        System.out.println("freqz");
        double[] signal = null;
        double[] freqs = null;
        Complex[] expResult = null;
        Complex[] result = JavaApplication1.freqz(signal, freqs);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        double result = JavaApplication1.doEEMD(sig1, sig2, accX, accY, accZ, fPrev, fSampling, peaks);
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

   
}

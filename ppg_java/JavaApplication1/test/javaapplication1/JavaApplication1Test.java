/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
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
     * 
     */
    @Test
    public  void testAddTwoList(){
        ArrayList<Double> a1 = new ArrayList<>();
        a1.add(Double.valueOf(5));
        a1.add(Double.valueOf(3));
        
        ArrayList<Double> a2 = new ArrayList<>();
        a2.add(Double.valueOf(-2));
        a2.add(Double.valueOf(0));
        
        ArrayList<Double> a3 = JavaApplication1.addTwoList(a1, a2);
        
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(Double.valueOf(3));
        expected.add(Double.valueOf(3));
        
        assertTrue(expected.equals(a3));
        
        
    }

    /**
     * Test of rlsFilter method, of class JavaApplication1.
     */
    @Test
    public void testRlsFilter() {
        System.out.println("rlsFilter");
        int lParameter = 0;
        ArrayList<Double> hSig = null;
        ArrayList<Double> mainSig = null;
        JavaApplication1 instance = new JavaApplication1();
        ArrayList<Double> expResult = null;
        ArrayList<Double> result = instance.rlsFilter(lParameter, hSig, mainSig);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getArray method, of class JavaApplication1.
     */
    @Test
    public void testGetArray() throws Exception {
        System.out.println("getArray");
        String fileName = "";
        ArrayList<Double> expResult = null;
        ArrayList<Double> result = JavaApplication1.getArray(fileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of myBandPass method, of class JavaApplication1.
     */
    @Test
    public void testMyBandPass() {
        System.out.println("myBandPass");
        ArrayList<Double> rSig = null;
        double fSampling = 0.0;
        ArrayList<Double> expResult = null;
        ArrayList<Double> result = JavaApplication1.myBandPass(rSig, fSampling);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class JavaApplication1.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        List<Double> sig = null;
        List<Double> accX = null;
        List<Double> accY = null;
        List<Double> accZ = null;
        float fSampling = 0.0F;
        float expResult = 0.0F;
        float result = JavaApplication1.initialize(sig, accX, accY, accZ, fSampling);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    
}

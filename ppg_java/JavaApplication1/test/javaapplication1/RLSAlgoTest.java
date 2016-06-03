/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mac
 */
public class RLSAlgoTest {
    
    public RLSAlgoTest() {
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
     * Test of compute method, of class RLSAlgo.
     */
    @Test
    public void testCompute() {
        System.out.println("compute");
        RLSAlgo instance = null;
        double[] expResult = null;
        instance.compute();
        //assertArrayEquals(expResult, result,0.01);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXN method, of class RLSAlgo.
     */
    @Test
    public void testGetXN() {
        System.out.println("getXN");
        double[] x = {0,1,2,3,4,5,6};
        int n = 4;
        int p = 6;
        double[] expResult = {4,3,2,1,0,0};
        double[] result = RLSAlgo.getXN(x, n, p);
        assertArrayEquals(expResult, result,0.01);
        
    }
    
}

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.collections.AbstractTestObject;
import org.apache.commons.collections.IterableMap;

/**
 * JUnit tests.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/01/14 21:34:34 $
 * 
 * @author Stephen Colebourne
 */
public class TestIdentityMap extends AbstractTestObject {
    
    private static final Integer I1A = new Integer(1);
    private static final Integer I1B = new Integer(1);
    private static final Integer I2A = new Integer(2);
    private static final Integer I2B = new Integer(2);

    public TestIdentityMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return new TestSuite(TestIdentityMap.class);
//        return BulkTest.makeSuite(TestIdentityMap.class);  // causes race condition!
    }
    
    public Object makeObject() {
        return new IdentityMap();
    }
    
    public String getCompatibilityVersion() {
        return "3";
    }
    
    //-----------------------------------------------------------------------
    public void testBasics() {
        IterableMap map = new IdentityMap();
        assertEquals(0, map.size());
        
        map.put(I1A, I2A);
        assertEquals(1, map.size());
        assertSame(I2A, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(true, map.containsValue(I2A));
        assertEquals(false, map.containsValue(I2B));
        
        map.put(I1A, I2B);
        assertEquals(1, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
        
        map.put(I1B, I2B);
        assertEquals(2, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(I2B, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(true, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
    }
    
    //-----------------------------------------------------------------------
    public void testHashEntry() {
        IterableMap map = new IdentityMap();
        
        map.put(I1A, I2A);
        map.put(I1B, I2A);
        
        Map.Entry entry1 = (Map.Entry) map.entrySet().iterator().next();
        Iterator it = map.entrySet().iterator();
        Map.Entry entry2 = (Map.Entry) it.next();
        Map.Entry entry3 = (Map.Entry) it.next();
        
        assertEquals(true, entry1.equals(entry2));
        assertEquals(true, entry2.equals(entry1));
        assertEquals(false, entry1.equals(entry3));
    }
    
    /**
     * Compare the current serialized form of the Map
     * against the canonical version in CVS.
     */
    public void testEmptyMapCompatibility() throws IOException, ClassNotFoundException {
        // test to make sure the canonical form has been preserved
        Map map = (Map) makeObject();
        if (map instanceof Serializable && !skipSerializedCanonicalTests()) {
            Map map2 = (Map) readExternalFormFromDisk(getCanonicalEmptyCollectionName(map));
            assertEquals("Map is empty", 0, map2.size());
        }
    }

//    public void testCreate() throws Exception {
//        Map map = new IdentityMap();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/IdentityMap.emptyCollection.version3.obj");
//        map = new IdentityMap();
//        map.put(I1A, I2A);
//        map.put(I1B, I2A);
//        map.put(I2A, I2B);
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/IdentityMap.fullCollection.version3.obj");
//    }
}
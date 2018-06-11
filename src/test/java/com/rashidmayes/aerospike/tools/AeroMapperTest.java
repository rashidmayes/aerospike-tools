package com.rashidmayes.aerospike.tools;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AeroMapperTest extends TestCase
{
    public AeroMapperTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AeroMapperTest.class );
    }

    public void testApp()
    {
        assertTrue( true );
    }
}

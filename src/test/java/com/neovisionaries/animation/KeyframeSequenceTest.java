package com.neovisionaries.animation;


import org.junit.Test;
import static org.junit.Assert.*;
import static com.neovisionaries.animation.interpolator.LinearInterpolator.LINEAR_INTERPOLATOR;


public class KeyframeSequenceTest
{
    private static final KeyframeSequence KS1 = createKS1();
    private static final KeyframeSequence KS2 = createKS2();


    private static KeyframeSequence createKS1()
    {
        return new KeyframeSequenceBuilder(1)
            .interpolator(LINEAR_INTERPOLATOR)
            .keyframe(0, 0.0f)
            .keyframe(8, 64.0f)
            .duration(8)
            .build();
    }


    private static KeyframeSequence createKS2()
    {
        return new KeyframeSequenceBuilder(1)
            .interpolator(LINEAR_INTERPOLATOR)
            .keyframe(0, 0.0f)
            .keyframe(8, 64.0f)
            .duration(12)
            .repeated(true)
            .build();
    }


    private static void checkEquals(KeyframeSequence ks, int time, float expected)
    {
        float[] output = new float[1];

        ks.getValueAt(time, output);

        assertEquals(expected, output[0], 0.0001f);
    }


    @Test
    public void test1()
    {
        checkEquals(KS1, 0, 0.0f);
    }


    @Test
    public void test2()
    {
        checkEquals(KS1, 2, 16.0f);
    }


    @Test
    public void test3()
    {
        checkEquals(KS1, 4, 32.0f);
    }


    @Test
    public void test4()
    {
        checkEquals(KS1, 8, 64.0f);
    }


    @Test
    public void test5()
    {
        checkEquals(KS1, -1, 0.0f);
    }


    @Test
    public void test6()
    {
        checkEquals(KS1, 9, 64.0f);
    }


    @Test
    public void test7()
    {
        checkEquals(KS2, 0, 0.0f);
    }


    @Test
    public void test8()
    {
        checkEquals(KS2, 2, 16.0f);
    }


    @Test
    public void test9()
    {
        checkEquals(KS2, 4, 32.0f);
    }


    @Test
    public void test10()
    {
        checkEquals(KS2, 8, 64.0f);
    }


    @Test
    public void test11()
    {
        checkEquals(KS2, 9, 48.0f);
    }


    @Test
    public void test12()
    {
        checkEquals(KS2, 10, 32.0f);
    }


    @Test
    public void test13()
    {
        checkEquals(KS2, 12, 0.0f);
    }


    @Test
    public void test14()
    {
        checkEquals(KS2, 13, 8.0f);
    }


    @Test
    public void test15()
    {
        checkEquals(KS2, -1, 16.0f);
    }


    @Test
    public void test16()
    {
        checkEquals(KS2, -2, 32.0f);
    }


    @Test
    public void test17()
    {
        checkEquals(KS2, -4, 64.0f);
    }
}

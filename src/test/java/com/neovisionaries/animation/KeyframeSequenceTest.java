/*
 * Copyright (C) 2012 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            .keyframe(0, 0.0f)
            .keyframe(8, 64.0f)
            .duration(8)
            .build();
    }


    private static KeyframeSequence createKS2()
    {
        return new KeyframeSequenceBuilder(1)
            .keyframe(0, 0.0f)
            .keyframe(8, 64.0f)
            .duration(12)
            .repeated(true)
            .build();
    }


    private static void checkEquals(KeyframeSequence ks, int time, float expected)
    {
        float[] output = new float[1];

        ks.getValueAt(time, LINEAR_INTERPOLATOR, output);

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

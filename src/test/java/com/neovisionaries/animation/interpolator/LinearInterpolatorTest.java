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
package com.neovisionaries.animation.interpolator;


import org.junit.Test;
import static org.junit.Assert.*;
import static com.neovisionaries.animation.interpolator.LinearInterpolator.LINEAR_INTERPOLATOR;


public class LinearInterpolatorTest
{
    private static void checkEquals(float from, float to, float ratio, float expected)
    {
        float actual = interpolate(from, to, ratio);

        assertEquals(expected, actual, 0.0001);
    }


    private static float interpolate(float from, float to, float ratio)
    {
        float[] output = new float[1];

        LINEAR_INTERPOLATOR.interpolate(
                new float[] { from }, 0, new float[] { to }, 0, 1, ratio, output, 0);

        return output[0];
    }


    @Test
    public void test1()
    {
        checkEquals(1.0f, 9.0f, 0.0f, 1.0f);
    }


    @Test
    public void test2()
    {
        checkEquals(1.0f, 9.0f, 0.125f, 2.0f);
    }


    @Test
    public void test3()
    {
        checkEquals(1.0f, 9.0f, 0.25f, 3.0f);
    }


    @Test
    public void test4()
    {
        checkEquals(1.0f, 9.0f, 0.5f, 5.0f);
    }


    @Test
    public void test5()
    {
        checkEquals(1.0f, 9.0f, 1.0f, 9.0f);
    }


    @Test
    public void test6()
    {
        checkEquals(-1.0f, -9.0f, 0.125f, -2.0f);
    }


    @Test
    public void test7()
    {
        checkEquals(-1.0f, -9.0f, 0.25f, -3.0f);
    }


    @Test
    public void test8()
    {
        checkEquals(-1.0f, -9.0f, 0.5f, -5.0f);
    }
}

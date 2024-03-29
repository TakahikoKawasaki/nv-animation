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


/**
 * Easing bounce interpolator.
 *
 * @author Takahiko Kawasaki
 */
public class EasingBounceInterpolator extends EasingInterpolator
{
    private int bounceCount = 3;
    private float bounciness = 2.0f;


    public EasingBounceInterpolator()
    {
        super();
    }


    public EasingBounceInterpolator(EasingMode easingMode)
    {
        super(easingMode);
    }


    /**
     * Get the count of bounces. The default value is 3.
     *
     * @return
     *         The count of bounces.
     */
    public int getBounceCount()
    {
        return bounceCount;
    }


    /**
     * Set the count of bounces.
     *
     * @param bounceCount
     *
     * @throws IllegalArgumentException
     *         The given value is less than 1.
     */
    public void setBounceCount(int bounceCount)
    {
        this.bounceCount = checkBounceCount(bounceCount);
    }


    /**
     * Get the value of bounciness. The default value is 2.0F.
     *
     * @return
     *         The value of bounciness.
     */
    public float getBounciness()
    {
        return bounciness;
    }


    /**
     * Set the value of bounciness.
     *
     * @param bounciness
     *
     * @throws IllegalArgumentException
     *         The given value is less than 1.
     */
    public void setBounciness(float bounciness)
    {
        this.bounciness = checkBounciness(bounciness);
    }


    @Override
    protected final float doEasing(float ratio)
    {
        double b1 = (bounciness == 1 ? 1.001 : bounciness);
        double b2 = 1 - b1;
        double p = 1 - Math.pow(b1, bounceCount);
        double q = ((1 - p) / b2 + p * 0.5) * b2;
        double f = Math.floor(Math.log(-ratio * q + 1) / Math.log(b1));
        double s = (1 - Math.pow(b1, f)) / q;
        double e = (1 - Math.pow(b1, f + 1)) / q;
        double m = (s + e) * 0.5;
        double r = m - s;
        double d = ratio - m;
        double a = Math.pow(1 / b1, bounceCount - f);

        return (float)((-a / (r * r)) * (d - r) * (d + r));
    }


    private static int checkBounceCount(int bounceCount)
    {
        if (bounceCount < 1)
        {
            throw new IllegalArgumentException("bounceCount < 1");
        }

        return bounceCount;
    }


    private static float checkBounciness(float bounciness)
    {
        if (bounciness < 1)
        {
            throw new IllegalArgumentException("bounciness < 1");
        }

        return bounciness;
    }
}

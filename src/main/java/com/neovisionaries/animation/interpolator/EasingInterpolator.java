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
 * Base implementation of easing interpolators.
 *
 * @author Takahiko Kawasaki
 */
public abstract class EasingInterpolator extends InterpolatorBase
{
    /**
     * Easing mode.
     */
    private EasingMode easingMode = EasingMode.OUT;


    /**
     * The default constructor with the default easing mode,
     * {@link EasingMode#OUT}.
     */
    public EasingInterpolator()
    {
    }


    /**
     * A constructor with an easing mode.
     *
     * @param easingMode
     *         An easing mode.
     *
     * @throws IllegalArgumentException
     *         The argument is null.
     */
    public EasingInterpolator(EasingMode easingMode)
    {
        this.easingMode = checkEasingMode(easingMode);
    }


    /**
     * Get the easing mode. The default value is {@link EasingMode#OUT}.
     *
     * @return
     *         The easing mode.
     */
    public EasingMode getEasingMode()
    {
        return easingMode;
    }


    /**
     * Set an easing mode.
     *
     * @param easingMode
     *         An easing mode.
     *
     * @throws IllegalArgumentException
     *         The argument is null.
     */
    public void setEasingMode(EasingMode easingMode)
    {
        this.easingMode = checkEasingMode(easingMode);
    }


    /**
     * Calculate an interpolated value.
     *
     * <p>
     * The implementation of {@code EasingInterpolator.doInterpolate}
     * modifies the value of {@code ratio} using {@link #doEasing(float)
     * doEasing} method (which is implemented by sub classes) and then
     * computes output by the expression shown below.
     * </p>
     *
     * <pre>
     * output = from * (1 - ratio) + to * ratio
     * </pre>
     *
     * <p>
     * The way to modify the ratio value varies depending on the easing mode
     * as shown below.
     * </p>
     *
     * <ol>
     * <li>If the easing mode is {@link EasingMode#IN IN}, the expression
     *     below is used.<br/>
     *     <pre>ratio = {@link #doEasing(float) doEasing}(ratio)</pre>
     *     <br/>
     * <li>If the easing mode is {@link EasingMode#OUT OUT}, the expression
     *     below is used.<br/>
     *     <pre>ratio = 1 - {@link #doEasing(float) doEasing}(1 - ratio)</pre>
     *     <br/>
     * <li>If the easing mode is {@link EasingMode#IN_OUT IN_OUT} and if
     *     {@code ratio} is less than 0.5, the expression below is used.<br/>
     *     <pre>ratio = {@link #doEasing(float) doEasing}(ratio * 2) * 0.5f</pre>
     *     <br/>
     * <li>If the easing mode is {@link EasingMode#IN_OUT IN_OUT} and if
     *     {@code ratio} is 0.5 or greater, the expression below is used.<br/>
     *     <pre>ratio = 1 - {@link #doEasing(float) doEasing}((1 - ratio) * 2) * 0.5f + 0.5f</pre>
     * </ol>
     */
    @Override
    protected final void doInterpolate(
            float[] from, int fromIndex, float[] to, int toIndex,
            int count, float ratio, float[] output, int outputIndex)
    {
        // EasingMode.IN
        if (easingMode == EasingMode.IN)
        {
            ratio = doEasing(ratio);
        }
        // EasingMode.OUT
        else if(easingMode == EasingMode.OUT)
        {
            ratio = 1 - doEasing(1 - ratio);
        }
        // EasingMode.IN_OUT (ratio < 0.5)
        else if (ratio < 0.5f)
        {
            ratio = doEasing(ratio * 2) * 0.5f;
        }
        // EasingMode.IN_OUT (0.5 <= ratio)
        else
        {
            ratio = 1 - doEasing((1 - ratio) * 2) * 0.5f + 0.5f;
        }

        for (int i = 0; i < count; ++i)
        {
            output[outputIndex + i] = from[fromIndex + i] * (1 - ratio) + to[toIndex + i] * ratio;
        }
    }


    /**
     * Check if the given easing mode is not null.
     *
     * @param easingMode
     *         An easing mode to check.
     *
     * @return
     *         The given easing mode is returned as is.
     *
     * @throws IllegalArgumentException
     *         The given argument is null.
     */
    private static EasingMode checkEasingMode(EasingMode easingMode)
    {
        if (easingMode == null)
        {
            throw new IllegalArgumentException("easingMode == null");
        }

        return easingMode;
    }


    /**
     * Modify the value of time ratio.
     *
     * <p>
     * This method is called from {@link #doInterpolate(float[], int,
     * float[], int, int, float, float[], int)
     * EasingInterpolator.doInterpolate} method. See the description
     * of {@code EasingInterpolator.doInterpolate} for details.
     * </p>
     *
     * @param ratio
     *         Time ratio.
     *
     * @return
     *         Modified time ratio.
     *
     * @see #doInterpolate(float[], int, float[], int, int, float, float[], int)
     */
    protected abstract float doEasing(float ratio);
}

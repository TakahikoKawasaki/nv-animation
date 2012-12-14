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
 * Base implementation of interpolators.
 *
 * @author Takahiko Kawasaki
 */
public abstract class InterpolatorBase implements Interpolator
{
    /**
     * Calculate an interpolated value.
     *
     * <p>
     * The implementation of {@code interpolate} method of
     * {@code InterpolatorBase} checks given arguments and then does
     * the following.
     * </p>
     *
     * <ol>
     * <li>If {@code ratio} is 0.0 or if {@code from} and {@code to} are
     *     identical and {@code fromIndex} and {@code toIndex} are equal,
     *     copy the content of {@code from} to {@code output} and return.</li>
     * <li>Otherwise, if {@code ratio} is 1.0, copy the content of
     *     {@code to} to {@code output} and return.</li>
     * <li>Otherwise, call {@link #doInterpolate(float[], int, float[],
     *     int, int, float, float[], int) doInterpolate} method.
     * </ol>
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code ratio} is less than 0.0.</li>
     * <li>{@code ratio} is greater than 1.0.</li>
     * <li>{@code count} is less than 1.0.</li>
     * <li>{@code output} is null.</li>
     * <li>The length of {@code output} minus {@code outputIndex} is less
     *     than {@code count}.</li>
     * <li>{@code from} is null (this check is not done if {@code ratio} is 1.0).</li>
     * <li>The length of {@code from} minus {@code fromIndex} is less than
     *     {@code count}. (this check is not done if {@code ratio} is 1.0).</li>
     * <li>{@code to} is null (this check is not done if {@code ratio} is 0.0).</li>
     * <li>The length of {@code to} minus {@code toIndex} is less than {@code count}.
     *     (this check is not done if {@code ratio} is 0.0).</li>
     * </ul>
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code fromIndex} is less than 0.</li>
     * <li>{@code toIndex} is less than 0.</li>
     * <li>{@code outputIndex} is less than 0.</li>
     * </ul>
     */
    public final void interpolate(
            float[] from, int fromIndex, float[] to, int toIndex,
            int count, float ratio, float[] output, int outputIndex)
    {
        // Check if 'ratio' is in between 0 and 1.
        if (ratio < 0 || 1 < ratio)
        {
            throw new IllegalArgumentException("ratio < 0 || 1 < ratio");
        }

        // Check if 'count' is a positive value.
        if (count < 1)
        {
            throw new IllegalArgumentException("count < 1");
        }

        // Check if 'fromIndex' is not negative.
        if (fromIndex < 0)
        {
            throw new IndexOutOfBoundsException("fromIndex < 0");
        }

        // Check if 'toIndex' is not negative.
        if (toIndex < 0)
        {
            throw new IndexOutOfBoundsException("toIndex < 0");
        }

        // Check if 'outputIndex' is not negative.
        if (outputIndex < 0)
        {
            throw new IndexOutOfBoundsException("outputIndex < 0");
        }

        // Check if the destination array is given.
        if (output == null)
        {
            throw new IllegalArgumentException("output == null");
        }

        // Check if the length of the destination array is big enough.
        if (output.length - outputIndex < count)
        {
            throw new IllegalArgumentException("output.length - outputIndex < count");
        }

        // If 'ratio' is less than 1, 'from' must be valid.
        if (ratio < 1)
        {
            if (from == null)
            {
                throw new IllegalArgumentException("from == null");
            }
            else if (from.length - fromIndex < count)
            {
                throw new IllegalArgumentException("from.length - fromIndex < count");
            }
        }

        // If 'ratio' is greater than 0, 'to' must be valid.
        if (0 < ratio)
        {
            if (to == null)
            {
                throw new IllegalArgumentException("to == null");
            }
            else if (to.length - toIndex < count)
            {
                throw new IllegalArgumentException("to.length - toIndex < count");
            }
        }

        // If 'ratio' is 0 or if 'from' and 'to' are identical.
        if (ratio == 0 || (from == to && fromIndex == toIndex))
        {
            // The value of 'output' becomes equal to the value of 'from'.
            // So, just copy 'from' to 'output'.
            System.arraycopy(from, fromIndex, output, outputIndex, count);
        }
        // If 'ratio' is 1.
        else if (ratio == 1)
        {
            // The value of 'output' becomes equal to the value of 'to'.
            // So, just copy 'to' to 'output'.
            System.arraycopy(to, toIndex, output, outputIndex, count);
        }
        // If 'ratio' is greater than 0 and less than 1.
        else
        {
            // Let sub classes compute the value.
            doInterpolate(from, fromIndex, to, toIndex, count, ratio, output, outputIndex);
        }
    }


    /**
     * Calculate an interpolated value.
     *
     * <p>
     * This method is called from {@link InterpolatorBase#interpolate(
     * float[], int, float[], int, int, float, float[])
     * InterpolatorBase.interpolate} method. Therefore, implementations
     * of this method ({@code doInterpolate}) does not have to do argument
     * checking done by {@code InterpolatorBase.interpolator}. Especially,
     * note that when this method is called, it is assured that {@code
     * ratio} is greater than 0.0 and less than 1.0.
     * </p>
     *
     * @param from
     *         The value at the start point of a given timespan.
     *         The length of the array minus {@code fromIndex} is equal
     *         to or greater than the component count ({@code count}).
     *
     * @param fromIndex
     *         The index in the {@code from} array from which
     *         the start value starts.
     *
     * @param to
     *         The value at the end point of a given timespan.
     *         The length of the array minus {@code toIndex} is equal
     *         to or greater than the component count ({@code count}).
     *
     * @param toIndex
     *         The index in the {@code to} array from which
     *         the end value starts.
     *
     * @param count
     *         The component count of {@code from[]} and {@code to[]}.
     *
     * @param ratio
     *         A time ratio between 0.0 (exclusively) and 1.0
     *         (exclusively).
     *
     * @param output
     *         A place into which the calculated value is put.
     *         The length of the array is equal to or greater than
     *         the component count ({@code count}).
     *
     * @param outputIndex
     *         The index in the {@code output} array at which
     *         the calculated value should be written.
     */
    protected abstract void doInterpolate(
            float[] from, int fromIndex, float[] to, int toIndex,
            int count, float ratio, float[] output, int outIndex);
}

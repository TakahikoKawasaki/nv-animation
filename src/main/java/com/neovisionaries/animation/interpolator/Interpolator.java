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
 * Interpolator.
 *
 * @author Takahiko Kawasaki
 */
public interface Interpolator
{
    /**
     * Calculate an interpolated value.
     *
     * <p>
     * An interpolator calculates an interpolated value in its own way.
     * The initial value ({@code from[]}), the final value ({@code to[]})
     * and a time ratio ({@code ratio}) are involved in the calculation.
     * </p>
     *
     * <p>
     * The range of time ratio is from 0.0 to 1.0. The time ratio 0.0
     * means the start point of a given timespan and the time ratio 1.0
     * means the end point of a given timespan. If 0.0 is given, the
     * value of {@code from[]} will be copied to {@code output[]}.
     * Likewise, if 1.0 is given, the value of {@code to[]} will be
     * copied to {@code output[]}. In other cases, each interpolator
     * implementation performs calculation in its own way.
     * </p>
     *
     * <p>
     * For example, an interpolator implementation that does linear
     * interpolation ({@link LinearInterpolator}) will generate [3.0]
     * based on the equation shown below when [2.0], [6.0] and 0.25 are
     * given as {@code from[]}, {@code to[]} and {@code ratio}.
     * </p>
     *
     * <pre style="margin: 1em;">
     * output[i] = (from[i] * (1.0 - ratio)) + (to[i] * ratio)
     * </pre>
     *
     * @param from
     *         The value at the start point of a given timespan.
     *         The length of the array must be equal to or greater than
     *         the component count ({@code count}).
     *
     * @param to
     *         The value at the end point of a given timespan.
     *         The length of the array must be equal to or greater than
     *         the component count ({@code count}).
     *
     * @param count
     *         The component count of {@code from[]} and {@code to[]}.
     *         The value must be equal to or greater than 1.
     *
     * @param ratio
     *         A time ratio. It must be in between 0.0 and 1.0.
     *
     * @param output
     *         A place into which the calculated value is put.
     *         The length of the array must be equal to or greater than
     *         the component count ({@code count}).
     */
    void interpolate(float[] from, float[] to, int count, float ratio, float[] output);
}

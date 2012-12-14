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
 * Linear interpolator.
 *
 * <p>
 * Output is computed by the expression shown below.
 * </p>
 *
 * <pre>
 * output = from * (1 - ratio) + to * ratio
 * </pre>
 *
 * @author Takahiko Kawasaki
 */
public class LinearInterpolator extends InterpolatorBase
{
    /**
     * {@code LinearInterpolator} instance. This instance can be shared
     * because {@code LinearInerpolator} is stateless.
     */
    public static final LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();


    @Override
    protected final void doInterpolate(
            float[] from, int fromIndex, float[] to, int toIndex,
            int count, float ratio, float[] output, int outputIndex)
    {
        for (int i = 0; i < count; ++i)
        {
            output[outputIndex + i] = from[fromIndex + i] * (1 - ratio) + to[toIndex + i] * ratio;
        }
    }
}

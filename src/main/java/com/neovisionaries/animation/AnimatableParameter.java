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


import com.neovisionaries.animation.interpolator.Interpolator;


/**
 * Animatable parameter. Basic implementation of {@link Animatable}.
 *
 * @author Takahiko Kawasaki
 */
public class AnimatableParameter implements Animatable
{
    /**
     * The value of this animatable parameter.
     */
    private final float[] value;


    /**
     * KeyframeSequence used to animate this parameter.
     */
    private KeyframeSequence keyframeSequence;


    /**
     * Interpolator to interpolate keyframe values.
     */
    private Interpolator interpolator;


    /**
     * Constructor with component count. This constructor just calls
     * {@link #AnimatableParameter(int, KeyframeSequence) this}{@code
     * (componentCount, null)}.
     *
     * @param componentCount
     *         Component count of the value of this parameter.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public AnimatableParameter(int componentCount)
    {
        this(componentCount, null);
    }


    /**
     * Constructor with component count and keyframe sequence.
     * This constructor just calls {@link #AnimatableParameter(int,
     * KeyframeSequence, Interpolator) this}{@code componentCount,
     * keyframeSequence, null)}.
     *
     * @param componentCount
     *         Component count of the value of this parameter.
     *
     * @param keyframeSequence
     *         Keyframe sequence used to animate this parameter.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public AnimatableParameter(int componentCount, KeyframeSequence keyframeSequence)
    {
        this(componentCount, keyframeSequence, null);
    }


    /**
     * Constructor with component count, keyframe sequence and interpolator.
     *
     * @param componentCount
     *         Component count of the value of this parameter.
     *
     * @param keyframeSequence
     *         Keyframe sequence used to animate this parameter.
     *
     * @param interpolator
     *         Interpolator to interpolate keyframe values.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public AnimatableParameter(int componentCount, KeyframeSequence keyframeSequence, Interpolator interpolator)
    {
        if (componentCount < 1)
        {
            throw new IllegalArgumentException("componentCount < 1");
        }

        this.value = new float[componentCount];
        this.keyframeSequence = keyframeSequence;
        this.interpolator = interpolator;
    }


    /**
     * Get the value of this parameter. The reference to the internal array
     * is returned, so changing the components in the returned array means
     * changing the value of this parameter directly.
     *
     * <p>
     * For example, you can get the value of the first component by {@code
     * getValue()[0]}, which is equivalent to {@link #getComponentValue(int)
     * getComponentValue}{@code (0)}.
     * </p>
     *
     * @return
     *         The value of this parameter.
     */
    public final float[] getValue()
    {
        return value;
    }


    /**
     * Get the value of the component at the specified index.
     *
     * @param componentIndex
     *         The index of the target component.
     *
     * @return
     *         The value of the component at the specified index.
     *
     * @throws ArrayIndexOutOfBoundsException
     *         The given index is out of the valid range.
     */
    public final float getComponentValue(int componentIndex)
    {
        return value[componentIndex];
    }


    /**
     * Set a value to the component at the specified index.
     *
     * @param componentIndex
     *         The index of the target component.
     *
     * @param componentValue
     *         A new value of the target component.
     *
     * @throws ArrayIndexOutOfBoundsException
     *         The given index is out of the valid range.
     */
    public final void setComponentValue(int componentIndex, float componentValue)
    {
        value[componentIndex] = componentValue;
    }


    /**
     * Get the keyframe sequence used to animate this parameter.
     *
     * @return
     *         Keyframe sequence.
     */
    public final KeyframeSequence getKeyframeSequence()
    {
        return keyframeSequence;
    }


    /**
     * Set the keyframe sequence used to animate this parameter.
     *
     * @param keyframeSequence
     *         Keyframe sequence.
     */
    public final void setKeyframeSequence(KeyframeSequence keyframeSequence)
    {
        this.keyframeSequence = keyframeSequence;
    }


    /**
     * Get the interpolator to interpolate keyframe values.
     *
     * @return
     *         Interpolator.
     */
    public final Interpolator getInterpolator()
    {
        return interpolator;
    }


    /**
     * Set the interpolator to interpolate keyframe values.
     *
     * @param interpolator
     *         Interpolator.
     */
    public final void setInterpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;
    }


    /**
     * Animate this parameter. If keyframe sequence is not set or
     * {@link KeyframeSequence#verify() verify()} method of the
     * {@link KeyframeSequence} instance returns false, of no
     * interpolator is set, nothing is performed and this method
     * returns false.
     */
    public final boolean animate(int time)
    {
        if (isAnimatable() == false)
        {
            return false;
        }

        return keyframeSequence.getValueAt(time, interpolator, value);
    }


    /**
     * Check if this object can be animatable.
     *
     * @return
     *         If keyframe sequence is not set or {@link
     *         KeyframeSequence#verify() verify()} method of
     *         the {@link KeyframeSequence} instance returns
     *         false, or no interpolator is set, this method
     *         returns false. Otherwise, this method returns true.
     */
    private boolean isAnimatable()
    {
        if (keyframeSequence == null)
        {
            return false;
        }

        if (keyframeSequence.verify() == false)
        {
            return false;
        }

        if (interpolator == null)
        {
            return false;
        }

        return true;
    }
}

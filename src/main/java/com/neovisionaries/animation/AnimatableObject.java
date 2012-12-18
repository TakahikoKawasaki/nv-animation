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


/**
 * Animatable object. Basic implementation of {@link Animatable}.
 *
 * @author Takahiko Kawasaki
 */
public class AnimatableObject implements Animatable
{
    /**
     * The value of this animatable object.
     */
    private final float[] value;


    /**
     * KeyframeSequence used to animate this object.
     */
    private KeyframeSequence keyframeSequence;


    /**
     * Constructor with component count. This constructor just calls
     * {@link #AnimatableObject(int, KeyframeSequence) this}{@code
     * (componentCount, null)}.
     *
     * @param componentCount
     *         Component count of the value of this object.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public AnimatableObject(int componentCount)
    {
        this(componentCount, null);
    }


    /**
     * Constructor with component count and keyframe sequence.
     *
     * @param componentCount
     *         Component count of the value of this object.
     *
     * @param keyframeSequence
     *         Keyframe sequence used to animate this object.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public AnimatableObject(int componentCount, KeyframeSequence keyframeSequence)
    {
        if (componentCount < 1)
        {
            throw new IllegalArgumentException("componentCount < 1");
        }

        this.value = new float[componentCount];
        this.keyframeSequence = keyframeSequence;
    }


    /**
     * Get the value of this object. The reference to the internal array
     * is returned, so changing the elements in the returned array means
     * changing the value of this object.
     *
     * <p>
     * If the component count of this object is 1, you can get the single
     * value by {@code getValue()[0]}.
     * </p>
     *
     * @return
     *         The value of this object.
     */
    public final float[] getValue()
    {
        return value;
    }


    /**
     * Get the keyframe sequence used to animate this object.
     *
     * @return
     *         Keyframe sequence.
     */
    public final KeyframeSequence getKeyframeSequence()
    {
        return keyframeSequence;
    }


    /**
     * Set the keyframe sequence used to animate this object.
     *
     * @param keyframeSequence
     *         Keyframe sequence.
     */
    public final void setKeyframeSequence(KeyframeSequence keyframeSequence)
    {
        this.keyframeSequence = keyframeSequence;
    }


    /**
     * Animate this object. If keyframe sequence is not set or
     * {@link KeyframeSequence#verify() verify()} method of the
     * {@link KeyframeSequence} instance returns false, nothing
     * is performed and this method returns false.
     */
    public final boolean animate(int time)
    {
        if (isAnimatable() == false)
        {
            return false;
        }

        return keyframeSequence.getValueAt(time, value);
    }


    /**
     * Check if this object can be animatable.
     *
     * @return
     *         If keyframe sequence is not set or {@link
     *         KeyframeSequence#verify() verify()} method of
     *         the {@link KeyframeSequence} instance returns
     *         false, this method returns false. Otherwise,
     *         this method returns true.
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

        return true;
    }
}

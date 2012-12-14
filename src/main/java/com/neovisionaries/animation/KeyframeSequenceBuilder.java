/*
 * Copyright (C) 2012 Neo Visionaries Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.neovisionaries.animation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import com.neovisionaries.animation.interpolator.Interpolator;


/**
 * Builder for {@link KeyframeSequence}.
 *
 * @see KeyframeSequence
 *
 * @author Takahiko Kawasaki
 */
public class KeyframeSequenceBuilder
{
    /**
     * Pairs of time and list of keyframe values. The value type
     * of this map is not {@code float[]} but {@code List<float[]>}
     * in order to accept keyframes whose times are equal.
     */
    private final SortedMap<Integer, List<float[]>> keyframeMap = new TreeMap<Integer, List<float[]>>();
    private int keyframeCount;
    private int componentCount;
    private Interpolator interpolator;
    private int duration;
    private boolean repeated;


    /**
     * The default constructor.
     * {@link #componentCount(int)} should be called later.
     */
    public KeyframeSequenceBuilder()
    {
    }


    /**
     * A constructor with component count.
     *
     * @param componentCount
     *         Component count.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     */
    public KeyframeSequenceBuilder(int componentCount)
    {
        checkComponentCount(componentCount);

        this.componentCount = componentCount;
    }


    /**
     * Set component count.
     *
     * <p>
     * If at least one keyframe has already been added,
     * component count cannot be changed.
     * </p>
     *
     * @param componentCount
     *         Component count.
     *
     * @return
     *         {@code this} object.
     *
     * @throws IllegalArgumentException
     *         {@code componentCount} is less than 1.
     *
     * @throws IllegalStateException
     *         Component count has already been set and at least one
     *         keyframe has been added.
     */
    public final KeyframeSequenceBuilder componentCount(int componentCount)
    {
        checkComponentCount(componentCount);

        if (this.componentCount != 0 && keyframeMap.size() != 0)
        {
            throw new IllegalStateException("Component count has already been set");
        }

        this.componentCount = componentCount;

        return this;
    }


    /**
     * Add a keyframe.
     *
     * <p>
     * This method is an alias of {@link #keyframe(int, float[], int)
     * keyframe}<{@code (time, value, (int)0)}.
     * </p>
     *
     * @param time
     *         Keyframe time.
     *
     * @param value
     *         Components of the keyframe.
     *
     * @return
     *         {@code this} object.
     *
     * @see #keyframe(int, float[], int)
     */
    public final KeyframeSequenceBuilder keyframe(int time, float... value)
    {
        return keyframe(time, value, (int)0);
    }


    /**
     * Add a keyframe.
     *
     * <p>
     * Before this method is called, component count needs to
     * be set by {@link #KeyframeSequenceBuilder(int)} or {@link
     * #componentCount(int)}. If component count is not set,
     * this method throws {@code IllegalStateException}.
     * </p>
     *
     * <p>
     * Note that this method must be called at least once so that
     * {@link #build()} method can create a new {@link KeyframeSequence}
     * instance.
     * </p>
     *
     * @param time
     *         Keyframe time.
     *
     * @param value
     *         Value of the keyframe. The length of the array minus
     *         {@code valueIndex} must be equal to or greater than
     *         the component count.
     *
     * @param valueIndex
     *         The index in the {@code value} array from which
     *         the keyframe value starts.
     *
     * @return
     *         {@code this} object.
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code time} is less than 0.</li>
     * <li>{@code value} is null.</li>
     * <li>{@code value.length} minus {@code valueIndex} is less than the
     *     component count.</li>
     * </ul>
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code valueIndex} is less than 0.</li>
     * </ul>
     *
     * @throws IllegalStateException
     * <ul>
     * <li>Component count is not set.</li>
     * </ul>
     *
     * @see KeyframeSequence#setKeyframe(int, int, float[], int)
     */
    public final KeyframeSequenceBuilder keyframe(int time, float[] value, int valueIndex)
    {
        // Check if time is not negative.
        if (time < 0)
        {
            throw new IllegalArgumentException("time < 0");
        }

        // Check if value is given.
        if (value == null)
        {
            throw new IllegalArgumentException("value is null");
        }

        // Check if valueIndex is not negative.
        if (valueIndex < 0)
        {
            throw new IndexOutOfBoundsException("valueIndex < 0");
        }

        // Check if component count has already been set.
        if (componentCount == 0)
        {
            throw new IllegalStateException("Component count is not set");
        }

        // Check if the length of value array is big enough.
        if (value.length - valueIndex < componentCount)
        {
            throw new IllegalArgumentException("value.length - valueIndex < componentCount");
        }

        List<float[]> list = keyframeMap.get(time);

        if (list == null)
        {
            list = new ArrayList<float[]>();
            keyframeMap.put(time, list);
        }

        // Clone the given value array to 'copied'.
        float[] copied = Arrays.copyOfRange(value, valueIndex, valueIndex + componentCount);

        list.add(copied);

        ++keyframeCount;

        return this;
    }


    /**
     * Set an interpolator.
     *
     * @param interpolator
     *
     * @return
     *         {@code this} object.
     *
     * @see KeyframeSequence#KeyframeSequence(int, int, Interpolator)
     * @see KeyframeSequence#setInterpolator(Interpolator)
     */
    public final KeyframeSequenceBuilder interpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;

        return this;
    }


    /**
     * Set duration.
     *
     * @param duration
     *         Duration.
     *
     * @return
     *         {@code this} object.
     *
     * @throws IllegalArgumentException
     *         {@code duration} is equal to or less than 0.
     *
     * @see KeyframeSequence#setDuration(int)
     */
    public final KeyframeSequenceBuilder duration(int duration)
    {
        if (duration <= 0)
        {
            throw new IllegalArgumentException("duration <= 0");
        }

        this.duration = duration;

        return this;
    }


    /**
     * Set 'repeated' flag. The default value is false.
     *
     * @param repeated
     *         True to enable 'repeated' mode.
     *
     * @return
     *         {@code this} object.
     *
     * @see KeyframeSequence#setRepeated(boolean)
     */
    public final KeyframeSequenceBuilder repeated(boolean repeated)
    {
        this.repeated = repeated;

        return this;
    }


    /**
     * Build a {@link KeyframeSequence} instance.
     *
     * @return
     *         A new {@code KeyframeSequence} instance.
     *
     * @throws IllegalStateException
     *         No keyframe has been added to this builder.
     */
    public final KeyframeSequence build()
    {
        // If no keyframe has been added to this builder.
        if (keyframeCount == 0)
        {
            // KeyframeSequence instance having no keyframe cannot be created.
            throw new IllegalStateException("No keyframe");
        }

        KeyframeSequence ks = new KeyframeSequence(keyframeCount, componentCount, interpolator);

        // If valid duration has been given to this builder.
        if (0 < duration)
        {
            // Set duration.
            ks.setDuration(duration);
        }

        // Set 'repeated' flag.
        ks.setRepeated(repeated);

        int keyframeIndex = 0;

        for (Map.Entry<Integer, List<float[]>> entry : keyframeMap.entrySet())
        {
            // Keyframe time.
            int time = entry.getKey();

            for (float[] value : entry.getValue())
            {
                // Set one keyframe.
                ks.setKeyframe(keyframeIndex, time, value);

                ++keyframeIndex;
            }
        }

        return ks;
    }


    private void checkComponentCount(int componentCount)
    {
        if (componentCount < 1)
        {
            throw new IllegalArgumentException("componentCount < 1");
        }
    }
}

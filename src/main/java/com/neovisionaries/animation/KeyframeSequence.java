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


import java.util.Arrays;
import com.neovisionaries.animation.interpolator.Interpolator;


/**
 * Keyframe sequence.
 *
 * @see KeyframeSequenceBuilder
 *
 * @author Takahiko Kawasaki
 */
public class KeyframeSequence
{
    private final int keyframeCount;
    private final int componentCount;
    private final float[] values;
    private final int[] times;
    private Interpolator interpolator;
    private int duration;
    private boolean repeated;


    /**
     * A constructor.
     *
     * @param keyframeCount
     *         The number of keyframes.
     *
     * @param componentCount
     *         The number of components of each keyframe.
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code keyframeCount} is less than 1.</li>
     * <li>{@code componentCount} is less than 1.</li>
     * </ul>
     */
    public KeyframeSequence(int keyframeCount, int componentCount)
    {
        this(keyframeCount, componentCount, null);
    }


    /**
     * A constructor.
     *
     * @param keyframeCount
     *         The number of keyframes.
     *
     * @param componentCount
     *         The number of components of each keyframe.
     *
     * @param interpolator
     *         An interpolator used to interpolate keyframe values.
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code keyframeCount} is less than 1.</li>
     * <li>{@code componentCount} is less than 1.</li>
     * </ul>
     */
    public KeyframeSequence(int keyframeCount, int componentCount, Interpolator interpolator)
    {
        if (keyframeCount < 1)
        {
            throw new IllegalArgumentException("keyframeCount < 1");
        }

        if (componentCount < 1)
        {
            throw new IllegalArgumentException("componentCount < 1");
        }

        this.keyframeCount = keyframeCount;
        this.componentCount = componentCount;
        this.interpolator = interpolator;

        values = new float[keyframeCount * componentCount];
        times = new int[keyframeCount];
    }


    /**
     * Get the number of keyframes.
     *
     * @return
     *         The number of keyframes.
     */
    public final int getKeyframeCount()
    {
        return keyframeCount;
    }


    /**
     * Get the number of components of each keyframe.
     *
     * @return
     *         The number of components of each keyframe.
     */
    public final int getComponentCount()
    {
        return componentCount;
    }


    /**
     * Get the keyframe at the specified index.
     *
     * <p>
     * This method is an alias of {@link #getKeyframe(int, float[], int)
     * getKeyframe}{@code (index, value, 0)}.
     * </p>
     *
     * @see #getKeyframe(int, float[], int)
     */
    public final int getKeyframe(int index, float[] value)
    {
        return getKeyframe(index, value, 0);
    }


    /**
     * Get the keyframe at the specified index.
     *
     * @param index
     *         A keyframe index.
     *
     * @param value
     *         A place to store the keyframe value.
     *
     * @param valueIndex
     *         The index in the {@code value} array at which
     *         the keyframe value should be written.
     *
     * @return
     *         The keyframe time of the keyframe.
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code index} is less than 0.</li>
     * <li>{@code index} is equal to or greater than the number of keyframes.</li>
     * <li>{@code valueIndex} is less than 0.</li>
     * </ul>
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code value} is null</li>
     * <li>The length of {@code value} minus {@code valueIndex} is less than
     *     the number of components of each keyframe.</li>
     * </ul>
     */
    public final int getKeyframe(int index, float[] value, int valueIndex)
    {
        checkIndex(index);
        checkArray("value", value, valueIndex);

        getValue(index, value, valueIndex);

        return getTime(index);
    }


    /**
     * Set a keyframe at the specified index.
     *
     * <p>
     * This method is an alias of {@link #setKeyframe(int, int, float[], int)
     * setKeyframe}{@code (index, time, value, 0)}.
     * </p>
     *
     * @see #setKeyframe(int, int, float[], int)
     */
    public final KeyframeSequence setKeyframe(int index, int time, float[] value)
    {
        return setKeyframe(index, time, value, 0);
    }


    /**
     * Set a keyframe at the specified index.
     *
     * <p>
     * A keyframe time should be equal to or greater than that of
     * the preceding keyframe. For example, if the keyframe time of the
     * keyframe at the index 5 is 1000, the keyframe time of the keyframe
     * at the index 6 must be equal to or greater than 1000, for instance,
     * 1500.
     * </p>
     *
     * @param index
     *         A keyframe index.
     *
     * @param time
     *         A keyframe time. The unit is application-dependent.
     *         In a typical case, the value of {@code time} for the first
     *         keyframe is 0. Values of keyframe times should be less than
     *         or equal to duration although it is not a must requirement
     *         (keyframe times greater than duration will be just ignored).
     *
     * @param value
     *         A keyframe value.
     *
     * @param valueIndex
     *         The index in the {@code value} array at which
     *         the keyframe value starts.
     *
     * @return
     *         {@code this} object.
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code index} is less than 0.</li>
     * <li>{@code index} is equal to or greater than the number of keyframes.</li>
     * <li>{@code valueIndex} is less than 0.</li>
     * </ul>
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code time} is less than 0.</li>
     * <li>{@code value} is null.</li>
     * <li>The size of {@code value} is less than the number of components of
     *     each keyframe.</li>
     * </ul>
     */
    public final KeyframeSequence setKeyframe(int index, int time, float[] value, int valueIndex)
    {
        checkIndex(index);
        checkTime(time);
        checkArray("value", value, valueIndex);

        setTime(index, time);
        setValue(index, value, valueIndex);

        return this;
    }


    /**
     * Get the keyframe time of the keyframe at the specified index.
     *
     * @param index
     *         A keyframe index.
     *
     * @return
     *         The keyframe time of the keyframe at the specified index.
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code index} is less than 0.</li>
     * <li>{@code index} is equal to or greater than the number of keyframes.</li>
     * </ul>
     */
    public final int getKeyframeTime(int index)
    {
        checkIndex(index);

        return getTime(index);
    }


    /**
     * Set a keyframe time of the keyframe at the specified index.
     *
     * <p>
     * A keyframe time should be equal to or greater than that of
     * the preceding keyframe. For example, if the keyframe time of the
     * keyframe at the index 5 is 1000, the keyframe time of the keyframe
     * at the index 6 must be equal to or greater than 1000, for instance,
     * 1500.
     * </p>
     *
     * @param index
     *         A keyframe index.
     *
     * @param time
     *         A keyframe time. The unit is application-dependent.
     *         In a typical case, the value of {@code time} for the first
     *         keyframe is 0. Values of keyframe times should be less than
     *         or equal to duration although it is not a must requirement
     *         (keyframe time greater than duration will be just ignored).
     *
     * @return
     *         {@code this} object.
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code index} is less than 0.</li>
     * <li>{@code index} is equal to or greater than the number of keyframes.</li>
     * </ul>
     *
     * @throws IllegalArgumentException
     *         {@code time} is less than 0.
     */
    public final KeyframeSequence setKeyframeTime(int index, int time)
    {
        checkIndex(index);
        checkTime(time);

        setTime(index, time);

        return this;
    }


    /**
     * Get the interpolator.
     *
     * @return
     *         The interpolator.
     */
    public final Interpolator getInterpolator()
    {
        return interpolator;
    }


    /**
     * Set the interpolator used to interpolate keyframe values.
     *
     * @param interpolator
     *         An interpolator.
     *
     * @return
     *         {@code this} object.
     */
    public final KeyframeSequence setInterpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;

        return this;
    }


    /**
     * Get the duration of this keyframe sequence.
     *
     * @return
     *         The duration.
     */
    public final int getDuration()
    {
        return duration;
    }


    /**
     * Set duration.
     *
     * @param duration
     *         Duration to set. The unit is application-dependent.
     *
     * @return
     *         {@code this} object.
     */
    public final KeyframeSequence setDuration(int duration)
    {
        checkDuration(duration);

        this.duration = duration;

        return this;
    }


    /**
     * Check if the repeat mode is on.
     *
     * @return
     *         True if the repeat mode is on.
     */
    public final boolean isRepeated()
    {
        return repeated;
    }


    /**
     * Turn on or off the repeat mode.
     *
     * @param repeated
     *         True to turn on the repeat mode.
     *
     * @return
     *         {@code this} object.
     */
    public final KeyframeSequence setRepeated(boolean repeated)
    {
        this.repeated = repeated;

        return this;
    }


    /**
     * Get an interpolated value at the specified time.
     * The calculation involves the keyframe values and the
     * interpolator.
     *
     * <p>
     * Set interpolator and duration before calling this method.
     * Otherwise, {@code IllegalStateException} is thrown.
     * </p>
     *
     * @param time
     *         A time. The unit is application-dependent.
     *
     * @param output
     *         A place to store the interpolated value.
     *         The length of the array minus {@code outputIndex}
     *         must be equal to or greater than the component count.
     *
     * @param outputIndex
     *         The index in the {@code output} array at which
     *         the interpolated value should be written.
     *
     * @return
     *         False if the given time exceeds the duration and
     *         the repeat mode is off. If the repeat mode is on,
     *         true is always returned.
     *
     * @throws IllegalArgumentException
     * <ul>
     * <li>{@code output} is null.</li>
     * <li>The length of {@code output} minus {@code outputIndex}
     *     is less than the number of components of each keyframe.</li>
     * </ul>
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code outputIndex} is less than 0.</li>
     * </ul>
     *
     * @throws IllegalStateException
     * <ul>
     * <li>Duration is not set.</li>
     * <li>No keyframe within the duration.</li>
     * <li>Interpolator is not set.</li>
     * </ul>
     */
    public final boolean getValueAt(int time, float[] output, int outputIndex)
    {
        checkArray("output", output, outputIndex);
        validateDuration();
        validateInterpolator();

        if (isRepeated())
        {
            getValueInRepeatMode(time, output, outputIndex);

            return true;
        }
        else
        {
            getValueInConstantMode(time, output, outputIndex);

            return (duration < time);
        }
    }


    private void getValueInRepeatMode(int time, float[] output, int outputIndex)
    {
        int timeToLookUp = time % duration;

        if (timeToLookUp == 0 && time != 0)
        {
            timeToLookUp = duration;
        }

        // Search times array for the time.
        int index = Arrays.binarySearch(times, timeToLookUp);

        // If there is at least one entry whose time value is
        // exactly the same as the time to look up.
        if (0 <= index)
        {
            // Find the first one among the entries whose time value
            // matches the given time.
            index = findFirstAtSameTime(index);

            // Copy the value of the keyframe.
            getValue(index, output, outputIndex);

            return;
        }

        // The value of 'index' is (-(insertion point)-1). This means
        // there is no keyframe whose time exactly matches timeToLookUp.
        //
        // The time at the insertion point is greater than timeToLookUp
        // and the time at (insertion point - 1) is less than timeToLookUp.

        // Convert the value of 'index' to the value of insertion index.
        index = Math.abs(index + 1);

        int startIndex = determineStartIndexInRepeatMode(index);
        int endIndex = determineEndIndexInRepeatMode(index);

        // Let the interpolator compute the interpolated value.
        getInterpolatedValue(startIndex, endIndex, timeToLookUp, output, outputIndex);
    }


    private int determineStartIndexInRepeatMode(int index)
    {
        // If there is at least one keyframe before the keyframe.
        if (index != 0)
        {
            // Use the preceding keyframe.
            return index - 1;
        }

        // Find the last keyframe within the duration.
        index = Arrays.binarySearch(times, duration);

        // If there is at least one entry whose time value is exactly
        // the same as the duration.
        if (0 <= index)
        {
            // Find the last one among the entries whose time value
            // matches the duration.
            return findLastAtSameTime(index);
        }

        // The value of 'index' is (-(insertion point)-1). This means
        // there is no keyframe whose time exactly matches the duration.
        //
        // The time at the insertion point is greater than the duration
        // and the time at (insertion point - 1) is less than the duration.

        // Convert the value of 'index' to the value of insertion index.
        index = Math.abs(index + 1);

        return (index - 1);
    }


    private int determineEndIndexInRepeatMode(int index)
    {
        // If the time of the next keyframe does not exceed the duration.
        if (getTime(index) <= duration)
        {
            // Use the next keyframe.
            return index;
        }

        // Use the first keyframe.
        return 0;
    }


    private void getValueInConstantMode(int time, float[] output, int outputIndex)
    {
        // If the time is less than or equal to the time
        // of the first keyframe.
        if (time <= getTime(0))
        {
            // Copy the value of the first keyframe.
            getValue(0, output, outputIndex);

            return;
        }

        // Search times array for the time.
        int timeToLookUp = Math.min(time, duration);
        int index = Arrays.binarySearch(times, timeToLookUp);

        // If there is at least one entry whose time value is
        // exactly the same as the time to look up.
        if (0 <= index)
        {
            if (timeToLookUp < time)
            {
                // If the index is not negative and the given time is
                // greater than timeToLookUp, the time of the keyframe
                // at the index is exactly the same as the duration.
                // Find the last one among the entries whose time value
                // matches the duration.
                index = findLastAtSameTime(index);
            }
            else
            {
                // Find the first one among the entries whose time value
                // matches the given time.
                index = findFirstAtSameTime(index);
            }

            // Copy the value of the keyframe.
            getValue(index, output, outputIndex);

            return;
        }

        // The value of 'index' is (-(insertion point)-1). This means
        // there is no keyframe whose time exactly matches timeToLookUp.
        //
        // The time at the insertion point is greater than timeToLookUp
        // and the time at (insertion point - 1) is less than timeToLookUp.

        // Convert the value of 'index' to the value of insertion index.
        index = Math.abs(index + 1);

        // If there is no keyframe after the time.
        if (index == keyframeCount)
        {
            // If 'repeat' mode is off, the keyframe values of the last
            // keyframe lasts after the keyframe time of the last keyframe.
            // Copy the value of the last keyframe.
            getValue(keyframeCount - 1, output, outputIndex);

            return;
        }

        // Let the interpolator compute the interpolated value.
        getInterpolatedValue(index - 1, index, timeToLookUp, output, outputIndex);
    }


    private void getInterpolatedValue(int startIndex, int endIndex, int time, float[] output, int outputIndex)
    {
        // Time of the start keyframe.
        int startTime = getTime(startIndex);

        // Time of the end keyframe.
        int endTime = getTime(endIndex);

        // Time ratio.
        float ratio;

        if (startTime < endTime)
        {
            ratio = (float)(time - startTime) / (float)(endTime - startTime);
        }
        else
        {
            ratio = (float)(time - endTime) / (float)(duration - endTime + startTime);
        }

        // The index in 'values' array from which keyframe values of
        // the start keyframe starts.
        int fromIndex = startIndex * componentCount;

        // The index in 'values' array from which keyframe values of
        // the end keyframe starts.
        int toIndex = endIndex * componentCount;

        // Let the interpolator compute the interpolated value.
        interpolator.interpolate(values, fromIndex, values, toIndex, componentCount, ratio, output, outputIndex);
    }


    private void checkIndex(int index)
    {
        if (index < 0 || keyframeCount <= index)
        {
            throw new IndexOutOfBoundsException("index is out of range");
        }
    }


    private void checkTime(int time)
    {
        if (time < 0)
        {
            throw new IllegalArgumentException("time < 0");
        }
    }


    private void checkArray(String name, float[] array, int index)
    {
        if (index < 0)
        {
            throw new IndexOutOfBoundsException(name + "Index < 0");
        }

        if (array == null)
        {
            throw new IllegalArgumentException(name + " is null");
        }

        if (array.length - index < componentCount)
        {
            throw new IllegalArgumentException(name + ".length - " + name + "Index < componentCount");
        }
    }


    private void checkDuration(int duration)
    {
        if (duration <= 0)
        {
            throw new IllegalArgumentException("duration <= 0");
        }
    }


    private void validateDuration()
    {
        if (duration <= 0)
        {
            throw new IllegalStateException("Duration is not set");
        }

        if (duration < getTime(0))
        {
            throw new IllegalStateException("No keyframe within the duration (" + duration + ")");
        }
    }


    private void validateInterpolator()
    {
        if (interpolator == null)
        {
            throw new IllegalStateException("Interpolator is not set");
        }
    }


    private int getTime(int index)
    {
        return times[index];
    }


    private void setTime(int index, int time)
    {
        times[index] = time;
    }


    private void getValue(int index, float[] value, int valueIndex)
    {
        System.arraycopy(values, index * componentCount, value, valueIndex, componentCount);
    }


    private void setValue(int index, float[] value, int valueIndex)
    {
        System.arraycopy(value, valueIndex, values, index * componentCount, componentCount);
    }


    private int findFirstAtSameTime(int index)
    {
        int time = getTime(index);

        while (true)
        {
            if (index == 0)
            {
                // The index points to the first keyframe.
                break;
            }

            if (getTime(index - 1) != time)
            {
                // The time of the previous keyframe is different.
                break;
            }

            --index;
        }

        return index;
    }


    private int findLastAtSameTime(int index)
    {
        int time = getTime(index);

        while (true)
        {
            if (index + 1 == keyframeCount)
            {
                // The index points to the last keyframe.
                break;
            }

            if (getTime(index + 1) != time)
            {
                // The time of the next keyframe is different.
                break;
            }

            ++index;
        }

        return index;
    }
}

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
public class KeyframeSequence implements Cloneable
{
    private final int keyframeCount;
    private final int componentCount;
    private float[] values;
    private int[] times;
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

        values = new float[keyframeCount * componentCount];
        times = new int[keyframeCount];
    }


    @Override
    public KeyframeSequence clone()
    {
        try
        {
            KeyframeSequence cloned = (KeyframeSequence)super.clone();

            cloned.values = this.values.clone();
            cloned.times = this.times.clone();

            return cloned;
        }
        catch (CloneNotSupportedException e)
        {
            throw new Error("CloneNotSupportedException was detected.", e);
        }
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
     * getKeyframe}{@code (keyframeIndex, value, 0)}.
     * </p>
     *
     * @see #getKeyframe(int, float[], int)
     */
    public final int getKeyframe(int keyframeIndex, float[] value)
    {
        return getKeyframe(keyframeIndex, value, 0);
    }


    /**
     * Get the keyframe at the specified index.
     *
     * @param keyframeIndex
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
     * <li>{@code keyframeIndex} is less than 0.</li>
     * <li>{@code keyframeIndex} is equal to or greater than the number of keyframes.</li>
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
    public final int getKeyframe(int keyframeIndex, float[] value, int valueIndex)
    {
        checkKeyframeIndex(keyframeIndex);
        checkArray("value", value, valueIndex);

        getValue(keyframeIndex, value, valueIndex);

        return getTime(keyframeIndex);
    }


    /**
     * Set a keyframe at the specified index.
     *
     * <p>
     * This method is an alias of {@link #setKeyframe(int, int, float[], int)
     * setKeyframe}{@code (keyframeIndex, time, value, 0)}.
     * </p>
     *
     * @see #setKeyframe(int, int, float[], int)
     */
    public final KeyframeSequence setKeyframe(int keyframeIndex, int time, float[] value)
    {
        return setKeyframe(keyframeIndex, time, value, 0);
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
     * @param keyframeIndex
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
     * <li>{@code keyframeIndex} is less than 0.</li>
     * <li>{@code keyframeIndex} is equal to or greater than the number of keyframes.</li>
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
    public final KeyframeSequence setKeyframe(int keyframeIndex, int time, float[] value, int valueIndex)
    {
        checkKeyframeIndex(keyframeIndex);
        checkTime(time);
        checkArray("value", value, valueIndex);

        setTime(keyframeIndex, time);
        setValue(keyframeIndex, value, valueIndex);

        return this;
    }


    /**
     * Get the keyframe time of the keyframe at the specified index.
     *
     * @param keyframeIndex
     *         A keyframe index.
     *
     * @return
     *         The keyframe time of the keyframe at the specified index.
     *
     * @throws IndexOutOfBoundsException
     * <ul>
     * <li>{@code keyframeIndex} is less than 0.</li>
     * <li>{@code keyframeIndex} is equal to or greater than the number of keyframes.</li>
     * </ul>
     */
    public final int getKeyframeTime(int keyframeIndex)
    {
        checkKeyframeIndex(keyframeIndex);

        return getTime(keyframeIndex);
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
     * @param keyframeIndex
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
     * <li>{@code keyframeIndex} is less than 0.</li>
     * <li>{@code keyframeIndex} is equal to or greater than the number of keyframes.</li>
     * </ul>
     *
     * @throws IllegalArgumentException
     *         {@code time} is less than 0.
     */
    public final KeyframeSequence setKeyframeTime(int keyframeIndex, int time)
    {
        checkKeyframeIndex(keyframeIndex);
        checkTime(time);

        setTime(keyframeIndex, time);

        return this;
    }


    public final KeyframeSequence setKeyframeValue(int keyframeIndex, float[] value)
    {
        return setKeyframeValue(keyframeIndex, value, 0);
    }


    public final KeyframeSequence setKeyframeValue(int keyframeIndex, float[] value, int valueIndex)
    {
        checkKeyframeIndex(keyframeIndex);
        checkArray("value", value, valueIndex);
        setValue(keyframeIndex, value, valueIndex);

        return this;
    }


    public final float getKeyframeComponentValue(int keyframeIndex, int componentIndex)
    {
        checkKeyframeIndex(keyframeIndex);
        checkComonentIndex(componentIndex);

        return getComponentValue(keyframeIndex, componentIndex);
    }


    public final KeyframeSequence setKeyframeComponentValue(int keyframeIndex, int componentIndex, float componentValue)
    {
        checkKeyframeIndex(keyframeIndex);
        checkComonentIndex(componentIndex);
        setComponentValue(keyframeIndex, componentIndex, componentValue);

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
     *
     * <p>
     * This method is an alias of {@link #getValueAt(int, Interpolator,
     * float[], int) getValueAt}{@code (time, interpolator, output, 0)}.
     * </p>
     * 
     * @param time
     *         Time.
     *
     * @param interpolator
     *         An interpolator to interpolate keyframe values.
     *
     * @param output
     *         A place to store the interpolated value.
     *
     * @return
     *         False if the given time exceeds the duration and
     *         the repeat mode is off. If the repeat mode is on,
     *         true is always returned.
     *
     * @see #getValueAt(int, Interpolator, float[], int)
     */
    public final boolean getValueAt(int time, Interpolator interpolator, float[] output)
    {
        return getValueAt(time, interpolator, output, 0);
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
     * @param interpolator
     *         An interpolator to interpolate keyframe values.
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
     * <li>{@code interpolator} is null.</li>
     * <li>{@code outputIndex} is less than 0.</li>
     * </ul>
     *
     * @throws IllegalStateException
     * <ul>
     * <li>Duration is not set.</li>
     * <li>No keyframe within the duration.</li>
     * </ul>
     */
    public final boolean getValueAt(int time, Interpolator interpolator, float[] output, int outputIndex)
    {
        checkInterpolator(interpolator);
        checkArray("output", output, outputIndex);
        verify(true);

        if (isRepeated())
        {
            getValueInRepeatMode(time, interpolator, output, outputIndex);

            return true;
        }
        else
        {
            getValueInConstantMode(time, interpolator, output, outputIndex);

            return (duration < time);
        }
    }


    private void getValueInRepeatMode(int time, Interpolator interpolator, float[] output, int outputIndex)
    {
        int timeToLookUp = time % duration;

        if (timeToLookUp < 0)
        {
            timeToLookUp += duration;
        }

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
        getInterpolatedValue(startIndex, endIndex, timeToLookUp, interpolator, output, outputIndex);
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
        // If the insertion point is beyond the last keyframe.
        if (index == keyframeCount)
        {
            // Use the first keyframe.
            return 0;
        }

        // If the time of the next keyframe does not exceed the duration.
        if (getTime(index) <= duration)
        {
            // Use the next keyframe.
            return index;
        }

        // Use the first keyframe.
        return 0;
    }


    private void getValueInConstantMode(int time, Interpolator interpolator, float[] output, int outputIndex)
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
        getInterpolatedValue(index - 1, index, timeToLookUp, interpolator, output, outputIndex);
    }


    private void getInterpolatedValue(int startIndex, int endIndex, int time, Interpolator interpolator, float[] output, int outputIndex)
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
            int numerator;

            if (time < startTime)
            {
                // 'time' is in between 0 and 'endTime'.
                numerator = time;
            }
            else
            {
                // 'time' is in between 'startTime' and 'duration'.
                numerator = time - startTime;
            }

            int denominator = endTime - startTime + duration;

            ratio = (float)numerator / (float)denominator;
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


    private void checkKeyframeIndex(int keyframeIndex)
    {
        if (keyframeIndex < 0 || keyframeCount <= keyframeIndex)
        {
            throw new IndexOutOfBoundsException("keyframeIndex is out of range");
        }
    }


    private void checkComonentIndex(int componentIndex)
    {
        if (componentIndex < 0 || componentCount <= componentIndex)
        {
            throw new IndexOutOfBoundsException("componentIndex is out of range");
        }
    }


    private void checkTime(int time)
    {
        if (time < 0)
        {
            throw new IllegalArgumentException("time < 0");
        }
    }


    private void checkInterpolator(Interpolator interpolator)
    {
        if (interpolator == null)
        {
            throw new IllegalArgumentException("interpolator is null");
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


    /**
     * Verify if this keyframe sequence is ready for performing interpolation.
     * If this method returns false, {@link #getValueAt(int, Interpolator,
     * float[], int)} will throws an exception.
     *
     * @return
     *         If duration is not set, false is returned.
     *         Otherwise, true is returned.
     */
    public final boolean verify()
    {
        return verify(false);
    }


    private boolean verify(boolean throwsException)
    {
        if (verifyDuration(throwsException) == false)
        {
            return false;
        }

        return true;
    }


    private boolean verifyDuration(boolean throwsException)
    {
        if (duration <= 0)
        {
            if (throwsException)
            {
                throw new IllegalStateException("Duration is not set");
            }
            else
            {
                return false;
            }
        }

        if (duration < getTime(0))
        {
            if (throwsException)
            {
                throw new IllegalStateException("No keyframe within the duration (" + duration + ")");
            }
            else
            {
                return false;
            }
        }

        return true;
    }


    private int getTime(int keyframeIndex)
    {
        return times[keyframeIndex];
    }


    private void setTime(int keyframeIndex, int time)
    {
        times[keyframeIndex] = time;
    }


    private void getValue(int keyframeIndex, float[] value, int valueIndex)
    {
        System.arraycopy(values, keyframeIndex * componentCount, value, valueIndex, componentCount);
    }


    private void setValue(int keyframeIndex, float[] value, int valueIndex)
    {
        System.arraycopy(value, valueIndex, values, keyframeIndex * componentCount, componentCount);
    }


    private float getComponentValue(int keyframeIndex, int componentIndex)
    {
        return values[keyframeIndex * componentCount + componentIndex];
    }


    private void setComponentValue(int keyframeIndex, int componentIndex, float componentValue)
    {
        values[keyframeIndex * componentCount + componentIndex] = componentValue;
    }


    private int findFirstAtSameTime(int keyframeIndex)
    {
        int time = getTime(keyframeIndex);

        while (true)
        {
            if (keyframeIndex == 0)
            {
                // The index points to the first keyframe.
                break;
            }

            if (getTime(keyframeIndex - 1) != time)
            {
                // The time of the previous keyframe is different.
                break;
            }

            --keyframeIndex;
        }

        return keyframeIndex;
    }


    private int findLastAtSameTime(int keyframeIndex)
    {
        int time = getTime(keyframeIndex);

        while (true)
        {
            if (keyframeIndex + 1 == keyframeCount)
            {
                // The index points to the last keyframe.
                break;
            }

            if (getTime(keyframeIndex + 1) != time)
            {
                // The time of the next keyframe is different.
                break;
            }

            ++keyframeIndex;
        }

        return keyframeIndex;
    }
}

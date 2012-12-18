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
 * Animator to animate {@link Animatable} objects.
 *
 * @author Takahiko Kawasaki
 */
public class Animator
{
    private int referenceSequenceTime;
    private int referenceWorldTime;
    private float speed = 1.0f;


    public Animator()
    {
    }


    public Animator(int worldTime)
    {
        this(worldTime, 0, 1.0f);
    }


    public Animator(int worldTime, int sequenceTime)
    {
        this(worldTime, sequenceTime, 1.0f);
    }


    public Animator(int worldTime, int sequenceTime, float speed)
    {
        this.referenceWorldTime = worldTime;
        this.referenceSequenceTime = sequenceTime;
        this.speed = speed;
    }


    /**
     * Compute the sequence time that corresponds to the given world time. The
     * sequence time is computed as follows.
     * 
     * <pre>
     * Math.round(referenceSequenceTime - speed * (worldTime - referenceWorldTime))
     * </pre>
     * 
     * @param worldTime
     *            World time.
     * 
     * @return Sequence time.
     */
    public final int getSequenceTime(int worldTime)
    {
        return Math.round(referenceSequenceTime + speed * (worldTime - referenceWorldTime));
    }


    public final void setReferenceTime(int worldTime, int sequenceTime)
    {
        this.referenceWorldTime = worldTime;
        this.referenceSequenceTime = sequenceTime;
    }


    /**
     * Get the playback speed. The default value is 1.0.
     *
     * @return
     *         Playback speed. 1.0 is the nominal speed.
     *         2.0 is double speed.
     */
    public final float getSpeed()
    {
        return speed;
    }


    /**
     * Set a new playback speed.
     *
     * @param speed
     *
     * @param worldTime
     *         New reference world time. The value of sequence time that
     *         corresponds to this world time remain unchanged.
     */
    public final void setSpeed(float speed, int worldTime)
    {
        this.referenceWorldTime = worldTime;
        this.referenceSequenceTime = getSequenceTime(worldTime);
        this.speed = speed;
    }


    public final boolean animate(int worldTime, Animatable... animatables)
    {
        // Convert the world time to a sequence time.
        int sequenceTime = getSequenceTime(worldTime);

        boolean changed = false;

        for (Animatable animatable : animatables)
        {
            if (animatable == null)
            {
                continue;
            }

            changed |= animatable.animate(sequenceTime);
        }

        return changed;
    }
}
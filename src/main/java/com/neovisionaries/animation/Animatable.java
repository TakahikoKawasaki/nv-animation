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
 * Animatable.
 *
 * <p>
 * Objects implementing {@code Animatable} interface can be animated
 * by {@link Animator#animate(int, Animatable...)}.
 * </p>
 *
 * @see Animator
 *
 * @author Takahiko Kawasaki
 */
public interface Animatable
{
    /**
     * Animate this object.
     *
     * @param time
     *         Time relative to the starting point of the associated
     *         animation. In a typical case, {@code time} is
     *         sequence time that {@link KeyframeSequence#getValueAt(int,
     *         float[])} can accept.
     *
     * @return
     *         True if any change has been made.
     */
    boolean animate(int time);
}

/*
 * MIT License
 *
 * Copyright (c) 2016. Agmo Studio
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agmostudio.pasarmalamlocator;

import android.location.Location;


public class PasarMalam {

    public String name;
    public String imageUrl;
    public String operationHour;
    public String desc;
    public double latitude;
    public double longitude;

    private float[] result = new float[1];

    /**
     * @param location The start Location
     * @return the distance in meters to this {@link PasarMalam}
     */
    public float calculateDistance(Location location) {
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                latitude, longitude, result);
        return result[0];
    }
}

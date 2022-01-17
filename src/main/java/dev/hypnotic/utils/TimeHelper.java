/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.utils;

public class TimeHelper {

    private long nanoTime = -1L;
    private static float deltaTime = 0;
    public static float startTime = System.nanoTime();

    public void reset() {
        nanoTime = System.nanoTime();
    }

    // All setters
    public void setTicks(long ticks) { nanoTime = System.nanoTime() - convertTicksToNano(ticks); }

    public void setNano(long time) { nanoTime = System.nanoTime() - time; }

    public void setMicro(long time) { nanoTime = System.nanoTime() - convertMicroToNano(time); }

    public void setMillis(long time) { nanoTime = System.nanoTime() - convertMillisToNano(time); }

    public void setSec(long time) { nanoTime = System.nanoTime() - convertSecToNano(time); }

    // All getters
    public long getTicks() { return convertNanoToTicks(nanoTime); }

    public long getNano() { return nanoTime; }

    public long getMicro() { return convertNanoToMicro(nanoTime); }

    public long getMillis() { return convertNanoToMillis(nanoTime); }

    public long getSec() { return convertNanoToSec(nanoTime); }

    // All passed
    public boolean passedTicks(long ticks) { return passedNano(convertTicksToNano(ticks)); }

    public boolean passedNano(long time) { return System.nanoTime() - nanoTime >= time; }

    public boolean passedMicro(long time) { return passedNano(convertMicroToNano(time)); }

    public boolean passedMillis(long time) { return passedNano(convertMillisToNano(time)); }

    public boolean passedSec(long time) { return passedNano(convertSecToNano(time)); }

    // Tick Conversions
    public long convertMillisToTicks(long time) { return time / 50; }
    public long convertTicksToMillis(long ticks) { return ticks * 50; }
    public long convertNanoToTicks(long time) { return convertMillisToTicks(convertNanoToMillis(time)); }
    public long convertTicksToNano(long ticks) { return convertMillisToNano(convertTicksToMillis(ticks)); }

    // All Conversions To Smaller
    public long convertSecToMillis(long time) { return time * 1000L; }
    public long convertSecToMicro(long time) { return convertMillisToMicro(convertSecToMillis(time)); }
    public long convertSecToNano(long time) { return convertMicroToNano(convertMillisToMicro(convertSecToMillis(time))); }

    public long convertMillisToMicro(long time) { return time * 1000L; }
    public long convertMillisToNano(long time) { return convertMicroToNano(convertMillisToMicro(time)); }

    public long convertMicroToNano(long time) { return time * 1000L; }

    // All Conversions To Larger
    public long convertNanoToMicro(long time) { return time / 1000L; }
    public long convertNanoToMillis(long time) { return convertMicroToMillis(convertNanoToMicro(time)); }
    public long convertNanoToSec(long time) { return convertMillisToSec(convertMicroToMillis(convertNanoToMicro(time))); }

    public long convertMicroToMillis(long time) { return time / 1000L; }
    public long convertMicroToSec(long time) { return convertMillisToSec(convertMicroToMillis(time)); }

    public long convertMillisToSec(long time) { return time / 1000L; }
    
    public static float getDeltaTime() {
		return deltaTime;
	}
    
    public static float getTime() {
    	return (float)((System.nanoTime() - startTime) * 1E-9);
    }
    
    public static void setDeltaTime(float deltaTime) {
		TimeHelper.deltaTime = deltaTime;
	}
}

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
package dev.hypnotic.ui;


public class TimeHelper {

    private long currentMS = 0L;
    private long lastMS = -1L;

    public void update() {
        currentMS = System.currentTimeMillis();
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasPassed(long MS) {
        update();
        return currentMS >= lastMS + MS;
    }

    public long getPassed() {
        update();
        return currentMS - lastMS;
    }

    public long getCurrentMS() {
        return currentMS;
    }

    public long getLastMS() {
        return lastMS;
    }
}
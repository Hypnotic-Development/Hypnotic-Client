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
package dev.hypnotic.waypoint;

import java.awt.Color;

import dev.hypnotic.module.Mod;
import net.minecraft.util.math.BlockPos;

public class Waypoint extends Mod {

	private String name;
	private double x, y, z;
	private BlockPos pos;
	private Color color;
	
	public Waypoint(String name, double x, double y, double z, Color color) {
		super(name, "", null);
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.pos = new BlockPos(x, y, z);
	}
	
	public Waypoint(String name, BlockPos pos) {
		super(name, "", null);
		this.name = name;
		this.pos = pos;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
}

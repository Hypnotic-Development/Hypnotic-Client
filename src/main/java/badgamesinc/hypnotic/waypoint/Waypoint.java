package badgamesinc.hypnotic.waypoint;

import badgamesinc.hypnotic.module.Mod;
import net.minecraft.util.math.BlockPos;

public class Waypoint extends Mod {

	private String name;
	private double x, y, z;
	private BlockPos pos;
	
	public Waypoint(String name, double x, double y, double z) {
		super(name, "", null);
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
}

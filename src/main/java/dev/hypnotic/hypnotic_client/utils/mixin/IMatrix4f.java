package dev.hypnotic.hypnotic_client.utils.mixin;

import dev.hypnotic.hypnotic_client.utils.math.Vec4;
import net.minecraft.util.math.Vec3d;

public interface IMatrix4f {
    void multiplyMatrix(Vec4 v, Vec4 out);

    Vec3d mul(Vec3d vec);
}

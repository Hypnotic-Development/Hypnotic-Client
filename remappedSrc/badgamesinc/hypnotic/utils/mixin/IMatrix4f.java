package badgamesinc.hypnotic.utils.mixin;

import badgamesinc.hypnotic.utils.math.Vec4;
import net.minecraft.util.math.Vec3d;

public interface IMatrix4f {
    void multiplyMatrix(Vec4 v, Vec4 out);

    Vec3d mul(Vec3d vec);
}

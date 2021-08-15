package badgamesinc.hypnotic.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

	public static double round(double num, double increment) {
        if (increment < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int) increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

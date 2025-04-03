package com.example.onlineStore.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtil {
    public static double roundToTwoDecimal(double value){
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

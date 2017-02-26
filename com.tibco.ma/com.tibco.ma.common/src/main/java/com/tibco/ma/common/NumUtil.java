package com.tibco.ma.common;

import java.math.BigDecimal;

/**
 * 
 * 
 * @author qiankun
 * @Functional description: Calculate the percentage
 */

public class NumUtil {

	public static BigDecimal toBig(Object o) {
		if (o == null || o.toString().equals("") || o.toString().equals("NaN")) {
			return new BigDecimal(0);
		}
		return new BigDecimal(o.toString());
	}

	/**
	 * Calculate percentage
	 * 
	 * @param divisor
	 * @param dividend
	 * @return String
	 * @author qiankun
	 * @date 2015-05-14
	 */
	public static String getPercent(Object divisor, Object dividend) {
		if (divisor == null || dividend == null) {
			return "";
		}
		BigDecimal a = toBig(divisor);
		BigDecimal b = toBig(dividend);
		if (a.equals(toBig(0)) || b.equals(toBig(0))) {
			return "0";
		}
		BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);

		return c.toString();
	}

	public static String divideNumber(Object divisor, Object dividend) {
		if (divisor == null || dividend == null) {
			return "";
		}
		BigDecimal a = toBig(divisor);
		BigDecimal b = toBig(dividend);
		if (a.equals(toBig(0)) || b.equals(toBig(0))) {
			return "0";
		}
		BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);
		return c.toString();
	}

	/**
	 * 
	 * @param divisor
	 * @param dividend
	 * @return int
	 */
	public static int averageNumber(Object divisor, Object dividend) {
		if (divisor == null || dividend == null) {
			return 0;
		}
		BigDecimal a = toBig(divisor);
		BigDecimal b = toBig(dividend);
		if (a.equals(toBig(0)) || b.equals(toBig(0))) {
			return 0;
		}
		BigDecimal c = a.divide(b, 0, BigDecimal.ROUND_HALF_UP);
		return c.intValue();
	}

}

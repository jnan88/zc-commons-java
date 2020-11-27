package xyz.nesting.core.coder;

public class KitBase62 {
	private static String	chars		= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static int		scale		= 62;
	// 最小5位
	private static int		minLength	= 5;

	/**
	 * 数字转62进制，默认长度为5
	 * 
	 * @param num
	 * @return
	 */
	public static String encode(long num) {
		return encode(num, minLength);
	}

	/**
	 * 数字转62进制
	 * 
	 * @param num
	 * @param minLength
	 *            最小长度
	 * @return
	 */
	public static String encode(long num, int minLength) {
		StringBuilder sb = new StringBuilder();
		int remainder;
		while (num > scale - 1) {
			// 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转字符串
			remainder = Long.valueOf(num % scale).intValue();
			sb.append(chars.charAt(remainder));
			// 除以进制数，获取下一个末尾数
			num = num / scale;
		}
		sb.append(chars.charAt(Long.valueOf(num).intValue()));
		int hasLeft = minLength - sb.length();
		while (hasLeft > 0) {
			hasLeft--;
			sb.append("0");
		}
		String value = sb.reverse().toString();
		return value;
	}

	/**
	 * 62进制转为数字
	 * 
	 * @param str
	 * @return
	 */
	public static long decode(String str) {
		// 将 0 开头的字符串进行替换
		str = str.replace("^0*", "");
		long value = 0;
		char tempChar;
		int tempCharValue;
		for (int i = 0, j = str.length(); i < j; i++) {
			// 获取字符
			tempChar = str.charAt(i);
			// 单字符值
			tempCharValue = chars.indexOf(tempChar);
			// 单字符值在进制规则下表示的值
			value += (long) (tempCharValue * Math.pow(scale, str.length() - i - 1));
		}
		return value;
	}
}

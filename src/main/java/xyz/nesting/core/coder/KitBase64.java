package xyz.nesting.core.coder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 
 * 描述：
 * 
 * <pre>
 * Base64编码
 * </pre>
 * 
 * @author qizai
 * @version: 0.0.1 Nov 27, 2020-6:40:29 PM
 *
 */
public final class KitBase64 {

	public static byte[] utf8Bytes(String data) {
		return data.getBytes(StandardCharsets.UTF_8);
	}

	public static String urlEncodeToString(final String src) {
		return Base64.getUrlEncoder().encodeToString(utf8Bytes(src));
	}

	public static String urlDecode(final String src) {
		return new String(Base64.getUrlDecoder().decode(src));
	}

	// decode
	public static byte[] decode(final byte[] src) {
		return Base64.getDecoder().decode(src);
	}

	public static byte[] decode(final String src) {
		return Base64.getDecoder().decode(src);
	}

	public static String decodeToString(final String src) {
		return new String(decode(src));
	}

	public static String decodeToString(final byte[] src) {
		return new String(decode(src));
	}

	// encode
	public static byte[] encode(final byte[] src) {
		return Base64.getEncoder().encode(src);
	}

	public static byte[] encode(final String src) {
		return encode(utf8Bytes(src));
	}

	public static String encodeToString(final byte[] src) {
		return new String(encode(src));
	}

	public static String encodeToString(final String src) {
		return new String(encode(src));
	}
}

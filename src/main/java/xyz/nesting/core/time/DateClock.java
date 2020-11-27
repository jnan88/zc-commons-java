package xyz.nesting.core.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.Getter;

/**
 * 
 * 描述：
 * 
 * <pre>
 * 日期工具类，用于函数中传递快速获取各种格式.统一使用时区ZONE_OFFSET_8
 * 启动类加:TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
 * </pre>
 * 
 * @author qizai
 * @version: 0.0.1 Oct 23, 2020-9:36:11 AM
 *
 */
public class DateClock {
	private final static ZoneOffset	ZONE_OFFSET_8	= ZoneOffset.ofHours(8);
	@Getter
	private LocalDateTime			localDateTime;
	@Getter
	private LocalDate				localDate;
	@Getter
	private LocalTime				localTime;

	public static ZoneOffset getZoneOffset8() {
		return ZONE_OFFSET_8;
	}

	private DateClock() {
	}

	public static DateClock getInstance() {
		DateClock dateClock = new DateClock();
		LocalDateTime ldt = LocalDateTime.now(ZONE_OFFSET_8.normalized());
		dateClock.init(ldt);
		return dateClock;
	}

	public static Date now() {
		return DateClock.getInstance().date();
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZONE_OFFSET_8.normalized()).toInstant());
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZONE_OFFSET_8.normalized()).toLocalDateTime();
	}

	public static DateClock of(Date date) {
		DateClock dateClock = new DateClock();
		dateClock.init(LocalDateTime.ofInstant(date.toInstant(), ZONE_OFFSET_8.normalized()));
		return dateClock;
	}

	/**
	 * @param text
	 *            日期转化，兼容格式yyyy-MM-dd、yyyy年MM月dd日、yyyyMMdd
	 * @return
	 */
	public static DateClock ofDate(String text) {
		DateClock dateClock = new DateClock();
		LocalDate ldt = null;
		if (text.indexOf("-") > -1) {
			ldt = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} else if (text.indexOf("年") > -1) {
			ldt = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
		} else {
			ldt = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyyMMdd"));
		}
		dateClock.init(ldt.atStartOfDay());
		return dateClock;
	}

	/**
	 * 日期
	 * 
	 * @return
	 */
	public Date date() {
		return asDate(localDateTime);
	}

	public Date atStartOfDay() {
		return asDate(localDate.atStartOfDay());
	}

	public Date atNow() {
		return asDate(localDate.atTime(LocalTime.now(ZONE_OFFSET_8)));
	}

	public Date atEndOfDay() {
		return asDate(localDate.atTime(LocalTime.of(23, 59, 59)));
	}

	/**
	 * 时间戳
	 * 
	 * @return
	 */
	public long timestmp() {
		return localDateTime.toEpochSecond(ZONE_OFFSET_8);
	}

	/**
	 * 
	 * @return 精确到毫秒
	 */
	public long timeMillis() {
		return localDateTime.toInstant(ZONE_OFFSET_8).toEpochMilli();
	}

	/**
	 * 
	 * @param pattern
	 *            LocalDateTime
	 * @return
	 */
	public String format(String pattern) {
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 
	 * @return yyyyMMdd
	 */
	public String formatYmd() {
		return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	/**
	 * 
	 * @return yyyy-MM-dd
	 */
	public String formatYmdUnderline() {
		return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * 
	 * @return yyyy年MM月dd日
	 */
	public String formatYmdChinese() {
		return localDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
	}

	/**
	 * 
	 * @return yyyyMMddHHmmss
	 */
	public String format() {
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String formatUnderline() {
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 
	 * @return yyyy年MM月dd日 HH时mm分ss秒
	 */
	public String formatChinese() {
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
	}

	private DateClock init(LocalDateTime ldt) {
		this.localDateTime = ldt;
		this.localDate = ldt.toLocalDate();
		this.localTime = ldt.toLocalTime();
		return this;
	}

	public DateClock plusYears(long years) {
		LocalDateTime ldt = this.localDateTime.plusYears(years);
		this.init(ldt);
		return this;
	}

	public DateClock plusMonths(long months) {
		LocalDateTime ldt = this.localDateTime.plusMonths(months);
		this.init(ldt);
		return this;
	}

	public DateClock plusDays(long days) {
		LocalDateTime ldt = this.localDateTime.plusDays(days);
		this.init(ldt);
		return this;
	}

	public DateClock plusHours(long hours) {
		LocalDateTime ldt = this.localDateTime.plusHours(hours);
		this.init(ldt);
		return this;
	}

	public DateClock plusMinutes(long minutes) {
		LocalDateTime ldt = this.localDateTime.plusMinutes(minutes);
		this.init(ldt);
		return this;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	@Override
	public String toString() {
		return formatUnderline();
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss.SSS
	 */
	public String iso() {
		return format("yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss Z
	 */
	public String isoOffset() {
		return ZonedDateTime.of(localDateTime, ZONE_OFFSET_8)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
	}

}

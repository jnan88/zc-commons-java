package xyz.nesting.core.idgen;

/**
 * 
 * 描述：使用雪花算法生成ID工具类
 * 
 * <pre>
    * 1位，不用。二进制中最高位为1的都是负数，但是我们生成的id一般都使用整数，所以这个最高位固定是0
    * 41位，用来记录时间戳（毫秒）。41位可以表示2^41−1个数字，约69年
    * 10位，用来记录工作机器id。可以部署在2^10=1024个节点，包括5位datacenterId和5位workerId
      5位（bit）可以表示的最大正整数是2^5-1 =31，即可以用0、1、2、3、....31这32个数字，来表示不同的datecenterId或workerId
    * 12位，序列号，用来记录同毫秒内产生的不同id。
      12位（bit）可以表示的最大正整数是2^12-1 = 4095，即可以用0、1、2、3、....4094这4095个数字，来表示同一机器同一时间截（毫秒)内产生的4095个ID序号
 * </pre>
 * 
 * @author qizai
 * @version: 0.0.1 Nov 26, 2020-5:30:48 PM
 *
 */
public class IdBySnowFlake {
	// 下面两个每个5位，加起来就是10位的工作机器id
	private long	workerId;		// 工作id
	private long	datacenterId;	// 数据id
	// 12位的序列号
	private long	sequence;

	/**
	 * 
	 * @param workerId
	 *            工作ID-5位，0-31
	 * @param datacenterId
	 *            数据ID-5位，0-31
	 * @param sequence
	 *            序列号-12位，0-4094
	 */
	public IdBySnowFlake(long workerId, long datacenterId, long sequence) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		if (sequence > sequenceMask || sequence < 0) {
			throw new IllegalArgumentException(
					String.format("sequence Id can't be greater than %d or less than 0", sequenceMask));
		}
		System.out.printf(
				"worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
				timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

		this.workerId = workerId;
		this.datacenterId = datacenterId;
		this.sequence = sequence;
	}

	// 初始时间戳
	private long	twepoch				= 1606383541749L;
	// 长度为5位
	private long	workerIdBits		= 5L;
	private long	datacenterIdBits	= 5L;
	// 最大值
	private long	maxWorkerId			= -1L ^ (-1L << workerIdBits);
	private long	maxDatacenterId		= -1L ^ (-1L << datacenterIdBits);
	// 序列号id长度
	private long	sequenceBits		= 12L;
	// 序列号最大值
	private long	sequenceMask		= -1L ^ (-1L << sequenceBits);
	// 工作id需要左移的位数，12位
	private long	workerIdShift		= sequenceBits;
	// 数据id需要左移位数 12+5=17位
	private long	datacenterIdShift	= sequenceBits + workerIdBits;
	// 时间戳需要左移位数 12+5+5=22位
	private long	timestampLeftShift	= sequenceBits + workerIdBits + datacenterIdBits;
	// 上次时间戳，初始值为负数
	private long	lastTimestamp		= -1L;

	public long getWorkerId() {
		return workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public long getTimestamp() {
		return System.currentTimeMillis();
	}

	// 下一个ID生成算法
	public synchronized long nextId() {
		long timestamp = getTimestamp();

		// 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
		if (timestamp < lastTimestamp) {
			System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		// 将上次时间戳值刷新
		lastTimestamp = timestamp;

		/**
		 * 返回结果： (timestamp - twepoch) << timestampLeftShift)
		 * 表示将时间戳减去初始时间戳，再左移相应位数 (datacenterId << datacenterIdShift)
		 * 表示将数据id左移相应位数 (workerId << workerIdShift) 表示将工作id左移相应位数 |
		 * 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
		 * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
		 */
		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	// 获取时间戳，并与上次时间戳比较
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = getTimestamp();
		while (timestamp <= lastTimestamp) {
			timestamp = getTimestamp();
		}
		return timestamp;
	}
}

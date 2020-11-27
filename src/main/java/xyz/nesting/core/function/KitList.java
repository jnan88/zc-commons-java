package xyz.nesting.core.function;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import com.google.common.collect.ObjectArrays;

/**
 * 
 * 描述：
 * 
 * <pre>
 * java8 list Stream 常用操作
 * </pre>
 * 
 * @author qizai
 * @version: 0.0.1 Sep 1, 2020-11:25:05 PM
 *
 */
public class KitList {
	/**
	 * Swaps the two specified elements in the specified array.
	 */
	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 将传入的数组乱序
	 */
	public static <T> T[] shuffle(T[] array) {
		if (array != null && array.length > 1) {
			Random rand = new Random();
			return shuffle(array, rand);
		} else {
			return array;
		}
	}

	/**
	 * 将传入的数组乱序
	 */
	public static <T> T[] shuffle(T[] array, Random random) {
		if (array != null && array.length > 1 && random != null) {
			for (int i = array.length; i > 1; i--) {
				swap(array, i - 1, random.nextInt(i));
			}
		}
		return array;
	}

	/**
	 * 添加元素到数组头.
	 */
	public static <T> T[] concat(T element, T[] array) {
		return ObjectArrays.concat(element, array);
	}

	/**
	 * 添加元素到数组末尾.
	 */
	public static <T> T[] concat(T[] array, T element) {
		return ObjectArrays.concat(array, element);
	}

	/**
	 * 附加序号的迭代拓展
	 * 
	 * @param <T>
	 * @param elements
	 * @param action
	 */
	public static <T> void forEach(Iterable<T> elements, BiConsumer<Integer, T> action) {
		Iterator<T> els = elements.iterator();
		int index = 0;
		while (els.hasNext()) {
			action.accept(index, els.next());
			index++;
		}
	}

	/**
	 * 过滤elements
	 * 
	 * @param <T>
	 * @param elements
	 * @param predicate
	 */
	public static <T> List<T> filter(Iterable<T> elements, Predicate<T> predicate) {
		List<T> list = new ArrayList<T>();
		elements.forEach(t -> {
			if (predicate.test(t)) {
				list.add(t);
			}
		});
		return list;
	}

	/**
	 * 取list某个属性返回list
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F, R> List<R> toList(List<F> fromList, Function<F, R> function) {
		if (null == fromList || fromList.isEmpty()) {
			return Collections.emptyList();
		}
		return fromList.stream().map(function).collect(Collectors.toList());
	}

	public static <F> List<F> filter(List<F> fromList, Predicate<F> predicate) {
		if (null == fromList || fromList.isEmpty()) {
			return fromList;
		}
		return fromList.stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * 取list某个属性累加(计算的属性不能为null)
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F, R> int sumInt(List<F> fromList, ToIntFunction<? super F> function) {
		if (null == fromList || fromList.isEmpty()) {
			return 0;
		}
		return fromList.stream().mapToInt(function).sum();
	}

	/**
	 * 取list某个属性累加(计算的属性不能为null)
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F, R> long sumLong(List<F> fromList, ToLongFunction<? super F> function) {
		if (null == fromList || fromList.isEmpty()) {
			return 0L;
		}
		return fromList.stream().mapToLong(function).sum();
	}

	/**
	 * 计算出来的平均值，总数，总和，最值(计算的属性不能为null)
	 * 
	 * @param <F>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F> IntSummaryStatistics summarizingInt(List<F> fromList, ToIntFunction<? super F> function) {
		return fromList.stream().collect(Collectors.summarizingInt(function));
	}

	/**
	 * 计算出来的平均值，总数，总和，最值(计算的属性不能为null)
	 * 
	 * @param <F>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F> LongSummaryStatistics summarizingLong(List<F> fromList, ToLongFunction<? super F> function) {
		return fromList.stream().collect(Collectors.summarizingLong(function));
	}

	/**
	 * 计算总和(计算属性可为null)
	 * 
	 * @param <F>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F> BigDecimal sumBigDecimal(List<F> fromList, Function<? super F, BigDecimal> function) {
		return fromList.stream().map(function).filter(x -> null != x).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 取list某个属性返回set
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F, R> Set<R> toSet(List<F> fromList, Function<F, R> function) {
		if (null == fromList || fromList.isEmpty()) {
			return Collections.emptySet();
		}
		return fromList.stream().map(function).collect(Collectors.toSet());
	}

	/**
	 * 取list某个集合属性返回set
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <F, R> Set<R> toListSet(List<F> fromList, Function<F, List<R>> function) {
		if (null == fromList || fromList.isEmpty()) {
			return Collections.emptySet();
		}
		List<R> list = fromList.stream().map(function).reduce((item, next) -> addAll(item, next))
				.orElse(Collections.emptyList());
		return list.isEmpty() ? Collections.emptySet() : new HashSet<>(list);
	}

	static <R> List<R> addAll(List<R> list1, List<R> list2) {
		if (null == list1) {
			return null == list2 ? new LinkedList<>() : list2;
		}
		if (null == list2 || list2.isEmpty()) {
			return list1;
		}
		list1.addAll(list2);
		return list1;
	}

	/**
	 * 直接对list进行去重处理
	 * 
	 * @param <T>
	 * @param fromList
	 * @return
	 */
	public static <T> List<T> distinct(List<T> fromList) {
		if (null == fromList || fromList.isEmpty()) {
			return fromList;
		}
		return fromList.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * 提取最大值，属性可为null，则返回可能为null
	 * 
	 * @param <T>
	 * @param <U>
	 * @param fromList
	 * @param keyExtractor
	 * @return
	 */
	public static <T, U extends Comparable<? super U>> Optional<T> maxBy(List<T> fromList,
			Function<? super T, ? extends U> keyExtractor) {
		return fromList.stream().collect(Collectors.maxBy(
				Comparator.comparing(keyExtractor, (o1, o2) -> o1 == null ? 1 : (o2 == null ? -1 : o2.compareTo(o1)))));
	}

	/**
	 * 提取最小值，属性可为null，则返回可能为null
	 * 
	 * @param <T>
	 * @param <U>
	 * @param fromList
	 * @param keyExtractor
	 * @return
	 */
	public static <T, U extends Comparable<? super U>> Optional<T> minBy(List<T> fromList,
			Function<? super T, ? extends U> keyExtractor) {
		return fromList.stream().collect(Collectors.minBy(
				Comparator.comparing(keyExtractor, (o1, o2) -> o1 == null ? 1 : (o2 == null ? -1 : o2.compareTo(o1)))));
	}

	/**
	 * 通过Set对list进行去重处理
	 * 
	 * @param <T>
	 * @param fromList
	 * @param keyExtractor
	 * @return
	 */
	public static <T> List<T> distinct(List<T> fromList, Function<? super T, ?> keyExtractor) {
		if (null == fromList || fromList.isEmpty()) {
			return fromList;
		}
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return fromList.stream().filter(t -> seen.add(keyExtractor.apply(t))).collect(Collectors.toList());
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	// /**
	// * 通过TreeSet对list进行去重处理
	// *
	// * @param <T>
	// * @param fromList
	// * @param keyExtractor
	// * @return
	// */
	// public static <T, U extends Comparable<? super U>> List<T>
	// distinct2(List<T> fromList,
	// Function<? super T, ? extends U> keyExtractor) {
	// return fromList.stream().collect(Collectors.collectingAndThen(
	// Collectors.toCollection(() -> new
	// TreeSet<>(Comparator.comparing(keyExtractor))), ArrayList::new));
	// }

	/**
	 * 对list进行正序排序
	 * 
	 * @param <T>
	 * @param <U>
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <T, U extends Comparable<? super U>> List<T> sort(List<T> fromList,
			Function<? super T, ? extends U> function) {
		return sort(fromList, function, false);
	}

	/**
	 * 对list进行正序排序
	 * 
	 * @param <T>
	 * @param <U>
	 * @param fromList
	 * @param function
	 * @param reversed
	 *            是否反向排序
	 * @return
	 */
	public static <T, U extends Comparable<? super U>> List<T> sort(List<T> fromList,
			Function<? super T, ? extends U> function, boolean reversed) {
		if (null == fromList || fromList.isEmpty()) {
			return fromList;
		}
		Comparator<T> comparator = Comparator.comparing(function,
				(o1, o2) -> o1 == null ? 1 : (o2 == null ? -1 : o2.compareTo(o1)));
		fromList.sort(reversed ? comparator.reversed() : comparator);
		return fromList;
	}

	/**
	 * 对list进行正序排序,多项排序
	 * 
	 * @param <T>
	 * @param <U>
	 * @param fromList
	 * @param function
	 * @param function2
	 * @param reversed
	 *            是否反向排序
	 * @return
	 */
	public static <T, U extends Comparable<? super U>> List<T> sort(List<T> fromList,
			Function<? super T, ? extends U> function, Function<? super T, ? extends U> function2, boolean reversed) {
		if (null == fromList || fromList.isEmpty()) {
			return fromList;
		}
		Comparator<T> comparator = Comparator.comparing(function,
				(o1, o2) -> o1 == null ? 1 : (o2 == null ? -1 : o2.compareTo(o1)));
		Comparator<T> comparator2 = Comparator.comparing(function2,
				(o1, o2) -> o1 == null ? 1 : (o2 == null ? -1 : o2.compareTo(o1)));
		if (reversed) {
			comparator = comparator.reversed().thenComparing(comparator2.reversed());
		} else {
			comparator = comparator.thenComparing(comparator2);
		}
		fromList.sort(comparator);
		return fromList;
	}

	/**
	 * 取list某个属性返回map
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param classifier
	 * @return
	 */
	public static <F, R> Map<R, List<F>> groupingBy(List<F> fromList, Function<F, R> classifier) {
		if (null == fromList || fromList.isEmpty()) {
			return Collections.emptyMap();
		}
		return fromList.stream().collect(Collectors.groupingBy(classifier));
	}

	/**
	 * 取list某个属性返回map（存在key冲突时以最后的key为准）
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param classifier
	 * @return
	 */
	public static <F, R> Map<R, F> toMap(List<F> fromList, Function<F, R> classifier) {
		if (null == fromList || fromList.isEmpty()) {
			return Collections.emptyMap();
		}
		return fromList.stream().collect(Collectors.toMap(classifier, Function.identity(), (v1, v2) -> v2));
	}

	/**
	 * 取list某个属性返回map（存在key冲突时以最后的key为准）
	 * 
	 * @param <F>
	 * @param <R>
	 * @param fromList
	 * @param classifier
	 * @return
	 */
	public static <F, R> ConcurrentMap<R, F> toConcurrentMap(List<F> fromList, Function<F, R> classifier) {
		return fromList.stream().collect(Collectors.toConcurrentMap(classifier, Function.identity(), (v1, v2) -> v2));
	}
}

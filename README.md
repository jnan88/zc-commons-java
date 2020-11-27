
## IO
### 在Reader与Writer间复制内容
> CharStreams.copy(input, output)

### 在InputStream与OutputStream间复制内容
> ByteStreams.copy(input, output)

## Collections

### Set

#### Sets.union(set1, set2) 并集
> set1, set2的并集（在set1或set2的对象）的只读view

#### Sets.intersection(set1, set2) 交集
> set1, set2的交集（同时在set1和set2的对象）的只读view

#### Sets.difference(set1, set2) 差集
> set1, set2的差集（在set1，不在set2中的对象）的只读view

#### Sets.symmetricDifference(set1, set2) 补集
> set1, set2的补集（在set1或set2中，但不在交集中的对象，又叫反交集）的只读view

### 随机乱序
> Collections.shuffle(list)

### 倒转顺序
> Lists.reverse(list)

### 返回无序集合中的最小值
> Collections.min(coll)

### 两个集合中的所有元素按顺序相等
> Iterables.elementsEqual(iterable1, iterable2)

### 返回无序集合中的最大值
> Collections.max(coll)

### 返回Iterable中最小的N个对象
> Ordering.natural().leastOf(coll, n)

### 返回Iterable中最大的N个对象
> Ordering.natural().greatestOf(coll, n)


## Queue
### 并发队列 ConcurrentLinkedQueue
### 并发双端队列 ConcurrentLinkedDeque
### 后进先出的栈
> Collections.asLifoQueue(new ArrayDeque<E>(initSize))

### 后进先出的无阻塞的并发栈
> Collections.asLifoQueue(new ConcurrentLinkedDeque<E>())

### 固定长度LRU栈（如果Queue已满，则删除最旧的元素）
> EvictingQueue.create(maxSize)# zc-commons-java

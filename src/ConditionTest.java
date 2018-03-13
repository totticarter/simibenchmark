import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
	public static void main(String[] args) {
		final Business business = new Business();
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadExecute(business, "sub");
			}
		}).start();
		threadExecute(business, "main");
	}

	public static void threadExecute(Business business, String threadType) {
		for (int i = 0; i < 10; i++) {
			try {
				if ("main".equals(threadType)) {
					business.main(i);
				} else {
					business.sub(i);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Business {
	private boolean bool = true;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public /* synchronized */ void main(int loop) throws InterruptedException {
		lock.lock();
		try {
			while (bool) {
				System.out.println("bool is: " + bool + ", main begin to wait");
				condition.await();// this.wait();
				System.out.println("notifition got for main , try to read bool, and bool now is: " + bool);
			}
			System.out.println("bool is: " + bool + " now, try to loop in main");
			for (int i = 0; i < 10; i++) {
				System.out.println("main thread seq of " + i + ", loop of " + loop);
			}
			bool = true;
			condition.signal();// this.notify();
		} finally {
			lock.unlock();
		}
	}

	public /* synchronized */ void sub(int loop) throws InterruptedException {
		lock.lock();
		try {
			while (!bool) {
				condition.await();// this.wait();
			}
			for (int i = 0; i < 10; i++) {
				System.out.println("sub thread seq of " + i + ", loop of " + loop);
			}
			bool = false;
			condition.signal();// this.notify();
		} finally {
			lock.unlock();
		}
	}
}

// 在Condition中，用await()替换wait()，用signal()替换notify()，用signalAll()替换notifyAll()，传统线程的通信方式，Condition都可以实现，这里注意，Condition是被绑定到Lock上的，要创建一个Lock的Condition必须用newCondition()方法。
// 这样看来，Condition和传统的线程通信没什么区别，Condition的强大之处在于它可以为多个线程间建立不同的Condition，下面引入API中的一段代码，加以说明。
class BoundedBuffer {
	final Lock lock = new ReentrantLock();// 锁对象
	final Condition notFull = lock.newCondition();// 写线程条件
	final Condition notEmpty = lock.newCondition();// 读线程条件

	final Object[] items = new Object[100];// 缓存队列
	int putptr/* 写索引 */, takeptr/* 读索引 */, count/* 队列中存在的数据个数 */;

	public void put(Object x) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length)// 如果队列满了
				notFull.await();// 阻塞写线程
			items[putptr] = x;// 赋值
			if (++putptr == items.length)
				putptr = 0;// 如果写索引写到队列的最后一个位置了，那么置为0
			++count;// 个数++
			notEmpty.signal();// 唤醒读线程
		} finally {
			lock.unlock();
		}
	}

	public Object take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0)// 如果队列为空
				notEmpty.await();// 阻塞读线程
			Object x = items[takeptr];// 取值
			if (++takeptr == items.length)
				takeptr = 0;// 如果读索引读到队列的最后一个位置了，那么置为0
			--count;// 个数--
			notFull.signal();// 唤醒写线程
			return x;
		} finally {
			lock.unlock();
		}
	}
}
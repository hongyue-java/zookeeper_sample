import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: BeatificWang
 * @Date: 2018/12/1 18:36
 */
public class ConnectionApp {
	public static void main(String[] args) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		//String s = "192.168.74.253:2181,192.168.74.253:2182,192.168.74.253:2183";
		String s = "127.0.0.1:2181";
		int sessionTimeOut = 3000;
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(s, sessionTimeOut, new org.apache.zookeeper.Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (Event.KeeperState.SyncConnected.equals(event.getState())
					) {
						countDownLatch.countDown();
					}
				}

			});
			// 强制等待至计数器归0
			countDownLatch.await();
			//CONNECTED
			System.out.println(zooKeeper.getState());
			String path = "/wang-test";
			// 新增节点,返回实际节点路径值
			String s1 = zooKeeper.create(path, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			Thread.sleep(2000);
			System.out.println(s1);

			// 新建Stat对象,记录节点状态
			Stat stat = new Stat();

			// 获取节点值
			byte[] bytes = zooKeeper.getData(path, null, stat);
			System.out.println(new String(bytes));
			System.out.println(stat);

			// 修改节点值
			Stat stat1 = zooKeeper.setData(path, "1".getBytes(), stat.getVersion());
			System.out.println(stat1);

			// 获取节点值
			byte[] uBytes = zooKeeper.getData(path, null, stat1);
			System.out.println(new String(uBytes));
			System.out.println(stat);

			// 删除节点值
			//zooKeeper.delete(path, stat1.getVersion());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

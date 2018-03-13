package zktest;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class MyWatcher implements Watcher,StatCallback {

	public ZooKeeper zkClient;
	public static Logger LOG = Logger.getLogger(MyWatcher.class);
	public MyWatcher() throws IOException{
		
		zkClient = new ZooKeeper("localhost:2181", 30000, this);
		LOG.info("zk connected successufully!");
	}
	@Override
	public void process(WatchedEvent event) {

		if(event.getType().equals(EventType.None)){
			
			System.out.println("1.Event type is: " + event.getType() + ", and event path is: "  + event.getPath());
		}else if(event.getType().equals(EventType.NodeCreated)){
			
			try {
				zkClient.exists(event.getPath(), true);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("2.Event type is: " + event.getType() + ", and event path is: "  + event.getPath());
		}else if(event.getType().equals(EventType.NodeDataChanged)){
			
			System.out.println("3.Event type is: " + event.getType() + ", and event path is: "  + event.getPath());
			try {
				byte[] b = this.zkClient.getData(event.getPath(), this, null);
				System.out.println("Changed data is: " + (new String(b)));
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(event.getType().equals(EventType.NodeChildrenChanged)){
			
			System.out.println("4.Event type is: " + event.getType() + ", and event path is: "  + event.getPath());
			try {
				
				List<String> children = this.zkClient.getChildren(event.getPath(), this);
				System.out.println("After changed, children are: " + children.toString());
				
				for(String child: children){
					
					System.out.println("Reregisit " + event.getPath() + "/" + child);
					this.zkClient.exists(event.getPath() + "/" + child, this);
				}
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		else if(event.getType().equals(EventType.NodeDeleted)){
			
			try {
				List<String> children = this.zkClient.getChildren(event.getPath(), this);
				System.out.println("After deleted, children are: " + children.toString());
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("5.Event type is: " + event.getType() + ", and event path is: "  + event.getPath());
		}
	}
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		
		String log4jConfPath = "/Users/waixingren/bigdata-java/test/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		
		MyWatcher mw = new MyWatcher();
		
		
		//1.测试节点创建
		//注册/root节点创建监听
		mw.zkClient.exists("/root", true);
		//创建/root触发process
		mw.zkClient.create("/root", "mydata1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		//2.测试数据变更
		//注册root节点数据变更监听
		mw.zkClient.getData("/root", mw, null);
		//修改数据，触发process，在process中通过获取数据继续监听root节点的数据变更
		mw.zkClient.setData("/root", "mydata2".getBytes(), -1);
		//测试永久监听数据变更是否生效
		mw.zkClient.setData("/root", "mydata3".getBytes(), -1);
		
		
		System.out.println("测试child变更==========");
		//注册root节点的child变更监听
		mw.zkClient.getChildren("/root", mw);
		//创建root的child，触发process，在process中通过调用getchildren继续监听root的children变更
		mw.zkClient.create("/root/child1", "child1data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		mw.zkClient.create("/root/child2", "child2data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		//测试child的child变化是否会被检测到
		List<String> children = mw.zkClient.getChildren("/root", mw);
		System.out.println("Regist watcher for children changer for /root, before changed, children are: " + children.toString());
		mw.zkClient.create("/root/child1/child1-child11", "child1data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		System.out.println("测试child删除===========");
		//4.测试child删除，在3中检测到child变更(即生成)时，使用exist对child进行监听注册
		mw.zkClient.delete("/root/child1", -1);
		mw.zkClient.delete("/root/child2", -1);
		mw.zkClient.delete("/root", -1);
	}
	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		// TODO Auto-generated method stub
		
	}
}

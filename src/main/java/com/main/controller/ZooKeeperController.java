package com.main.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ZooKeeperController {
	
	@Autowired 
	CuratorFramework client;
	private Boolean flag=false;
	
	
	/**
	 * http://localhost:9091/createZKNode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createZKNode/{root}/{root_content}")
	@ResponseBody
	public String createZKNode(@PathVariable String root,@PathVariable String root_content) throws Exception {
		System.out.println("进入添加根节点方法");
		client.create().forPath("/"+root,root_content.getBytes());
		return "创建"+root+"节点成功";
	}
	
	/**
	 * http://localhost:9091/isZKNodeExist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/isZKNodeExist/{path}")
	@ResponseBody
	public String isZKNodeExist(@PathVariable String path) throws Exception {
		System.out.println("判断"+path+"节点是否存在");
		Stat stat = client.checkExists().watched().forPath("/"+path);
		if(stat!=null) {
			flag=true;
			return path+"节点存在";
		}else {
			return path+"节点不存在";
		}
	}

	/**
	 *
	 *http://localhost:9091/updateZKNode
	 * 	 * @return
	 * 	 * @throws Exception
	 * 	 修改根节点数据
	 */
	@RequestMapping("/updateZKNode/{root}/{root_content}")
	@ResponseBody
	public String updateZKNode(@PathVariable String root,@PathVariable String root_content) throws Exception {
		System.out.println("进入修改节点方法");
		flag=false;
		this.isZKNodeExist(root);
		System.out.println(flag);
		if(flag){
			System.out.println("节点存在，可以修改");
			client.setData().forPath("/"+root,root_content.getBytes());
			return "修改成功";
		}else{
			System.out.println("节点不存在");
			return root+"节点不存在，无法修改";
		}
	}

	/**
	 *
	 *http://localhost:9091/getZKNode
	 * 	 * @return
	 * 	 * @throws Exception
	 * 	 查找根节点数据
	 */
	@RequestMapping("/getZKNode/{root}")
	@ResponseBody
	public String getZKNode(@PathVariable String root) throws Exception {
		System.out.println("进入查找节点方法");
		flag=false;
		this.isZKNodeExist(root);
		System.out.println(flag);
		if(flag){
			System.out.println("节点存在");
			byte[] bytes = client.getData().forPath("/" + root);
			return root+"节点的数据是"+new String(bytes);
		}else{
			System.out.println("节点不存在");
			return root+"节点不存在，无法修改";
		}
	}
	
}

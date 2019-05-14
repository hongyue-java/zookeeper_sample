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
	
	
	/**
	 * http://localhost:8080/createHello
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createHelloNode/{root}/{root_content}")
	@ResponseBody
	public String createHelloNode(@PathVariable String root,@PathVariable String root_content) throws Exception {
		System.out.println("进入添加根节点方法");
		client.create().forPath("/"+root,root_content.getBytes());
		return "创建Hello节点成功";
	}
	
	/**
	 * http://localhost:8080/isHelloNodeExist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/isHelloNodeExist")
	@ResponseBody
	public String isHelloNodeExist() throws Exception {
		System.out.println("判断Hello节点是否存在");
		Stat stat = client.checkExists().watched().forPath("/hello");
		if(stat!=null) {
			return "Hello节点存在";
		}else {
			return "Hello节点不存在";
		}
		
	}
	
	
}

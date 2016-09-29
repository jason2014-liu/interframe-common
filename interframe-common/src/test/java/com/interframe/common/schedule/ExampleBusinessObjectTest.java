/**
* TODO
* @Project: interframe-common
* @Title: ExampleBusinessObjectTest.java
* @Package org.interframe.common
* @author jason
* @Date 2016年9月20日 上午10:46:30
* @Copyright
* @Version 
*/
package com.interframe.common.schedule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.interframe.common.schedule.ExampleBusinessObject;

/**
* TODO
* @ClassName: ExampleBusinessObjectTest
* @author jason
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext-schedule.xml"})
public class ExampleBusinessObjectTest {

	@Autowired
	ApplicationContext ctx;
	
	@Test
	public void testEmployee(){
		ExampleBusinessObject obj =(ExampleBusinessObject) ctx.getBean("exampleBusinessObject");
		obj.doInit();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

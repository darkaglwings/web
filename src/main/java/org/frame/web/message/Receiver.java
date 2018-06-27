package org.frame.web.message;

import javax.jms.Message;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	
	private JmsTemplate jmsTemplate;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public Message reveive() {
		System.out.println("开始接受信息。。。");
		return jmsTemplate.receive();  
	}
	
	/*public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "org/frame/business/message/applicationContext-jms.xml" });
		Receiver receiver = (Receiver) context.getBean("receiver");
		
		Message message = receiver.reveive();
		try {
			System.out.println(((MapMessage) message).getString("session"));
			System.out.println(((MapMessage) message).getString("account"));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}*/
	
}

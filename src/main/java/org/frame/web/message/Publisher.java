package org.frame.web.message;

import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
	
	private JmsTemplate jmsTemplate;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send (final Map<String, Object> parameter){
		jmsTemplate.send(
				new MessageCreator() {

					@Override
					public Message createMessage(Session session)  {
						MapMessage message;
						try {
							message = session.createMapMessage();
							String key;
							for (Iterator<String> iterator = parameter.keySet().iterator(); iterator.hasNext();) {
								key = iterator.next();
								message.setObject(key, parameter.get(key));
							}
						} catch (JMSException e) {
							message = null;
							e.printStackTrace();
						}

						return message;
					}
				});
	}
	
	/*public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "org/frame/business/message/applicationContext-jms.xml" });
		Publisher publisher = (Publisher) context.getBean("publisher");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("info", "spring");
        publisher.send(map);
	}*/

}

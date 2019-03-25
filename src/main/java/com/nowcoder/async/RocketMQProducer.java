package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * RocketMQ producer
 *
 * @author wangleifu
 * @created 2019-02-23 17:17
 */
@Service
public class RocketMQProducer {
    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducer.class);

    public boolean fireEvent(EventModel model) {

        //声明并初始化一个producer
        //需要一个producer group名字作为构造方法的参数，这里为producer1
        DefaultMQProducer producer = new DefaultMQProducer("StationLetterProducer");

        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr("127.0.0.1:9876");

        try {
            //调用start()方法启动一个producer实例
            producer.start();

            //StationLetter，tag为Producer，消息内容为model对象的JSON字符串
            String json = JSONObject.toJSONString(model);
            Message msg = new Message("StationLetter",// topic
                    "Producer",// tag
                    json.getBytes(RemotingHelper.DEFAULT_CHARSET)// body
            );

            //调用producer的send()方法发送消息
            //这里调用的是同步的方式，所以会有返回结果
            SendResult sendResult = producer.send(msg);
        } catch (Exception e) {
            // MQClientException | InterruptedException | RemotingException | MQBrokerException | UnsupportedEncodingException
            logger.info("RocketMQProducer Exception: " + e.getMessage());
            return false;
        }
        finally {
            //发送完消息之后，调用shutdown()方法关闭producer
            producer.shutdown();
        }
        return true;
    }
}

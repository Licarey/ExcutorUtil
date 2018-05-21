package com.example.liming.pool;

import android.util.Log;

import com.example.liming.APP;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.common4j.xml.NodeParser;

/**
 * 从配置文件（/config/threadpool4j.xml）读取配置信息并存储在内存中。
 * 
 */
public class ThreadPoolConfig implements ILifeCycle {

    public final static String DEFAULT_CONFIG_FILE = "threadpool4j.xml";
    protected String _configFile =DEFAULT_CONFIG_FILE;
    
    /**
     * key为线程池名称，value为{@link ThreadPoolInfo}实例。
     */
    protected Map<String, ThreadPoolInfo> _multiThreadPoolInfo = new HashMap<String, ThreadPoolInfo>();
    
    /** 线程池状态收集开关 */
    protected boolean _threadPoolStateSwitch = false;
    protected int _threadPoolStateInterval = 60;   // 单位：秒
    
    /** 线程状态收集开关 */
    protected boolean _threadStateSwitch = false;
    protected int _threadStateInterval = 60;   // 单位：秒
    
    /** 线程堆栈收集开关 */
    protected boolean _threadStackSwitch = false;
    protected int _threadStackInterval = 60;   // 单位：秒
    
    @Override
    public void init() {
        initConfig();
    }

    private void initConfig() {
        try {
            InputStream inputStream = APP.getInstance().getAssets().open("threadpool4j.xml");
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            //获取：DocumentBuilder对象
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            //将数据源转换成：document 对象
            Document document = dBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            NodeParser rootParser = new NodeParser(root);
            List<Node> nodeList = rootParser.getChildNodes();
            for (Node node : nodeList) {
                NodeParser nodeParser = new NodeParser(node);
                if ( "pool".equals(node.getNodeName()) ) {
                    ThreadPoolInfo info = new ThreadPoolInfo();
                    info.setName(nodeParser.getAttributeValue("name"));
                    info.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
                    info.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
                    info.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
                    info.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
                    Log.e("LM" , "解析线程池数据  " + info.toString());
                    _multiThreadPoolInfo.put(info.getName(), info);
                } else if ( "threadpoolstate".equals(node.getNodeName()) ) {
                    _threadPoolStateSwitch = computeSwitchValue(nodeParser);
                    _threadPoolStateInterval = computeIntervalValue(nodeParser);
                } else if ( "threadstate".equals(node.getNodeName()) ) {
                    _threadStateSwitch = computeSwitchValue(nodeParser);
                    _threadStateInterval = computeIntervalValue(nodeParser);
                } else if ( "threadstack".equals(node.getNodeName()) ) {
                    _threadStackSwitch = computeSwitchValue(nodeParser);
                    _threadStackInterval = computeIntervalValue(nodeParser);
                }
            } // end of for
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    
    private boolean computeSwitchValue(NodeParser nodeParser) {
        return "on".equalsIgnoreCase(
                nodeParser.getAttributeValue("switch"));
    }
    
    private int computeIntervalValue(NodeParser nodeParser) {
        return Integer.parseInt(nodeParser.getAttributeValue("interval"));
    }
    
    /**
     * 指定名称的线程池的配置是否存在。
     * 
     * @return 如果指定名称的线程池的配置存在返回true，如果不存在返回false；如果传入的线程池名称为null也返回false。
     */
    public boolean containsPool(String poolName) {
        if (null == poolName || null == _multiThreadPoolInfo || _multiThreadPoolInfo.isEmpty()) {
            return false;
        }
        
        return _multiThreadPoolInfo.containsKey(poolName);
    }
    
    /**
     * 获取指定线程池的配置信息。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池配置信息（{@link ThreadPoolInfo}）
     */
    public ThreadPoolInfo getThreadPoolConfig(String threadpoolName) {
        return _multiThreadPoolInfo.get(threadpoolName);
    }
    
    /**
     * 获取所有线程池的配置信息。
     * 
     * @return 线程池配置信息（{@link ThreadPoolInfo}）集合
     */
    public Collection<ThreadPoolInfo> getThreadPoolConfig() {
        return _multiThreadPoolInfo.values();
    }
    
    /**
     * @return 输出各个线程池状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadPoolStateSwitch() {
        return _threadPoolStateSwitch;
    }
    
    /**
     * @return 线程池状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadPoolStateInterval() {
        return _threadPoolStateInterval;
    }
    
    /**
     * @return 输出各个线程组中线程状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadStateSwitch() {
        return _threadStateSwitch;
    }
    
    /**
     * @return 线程状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadStateInterval() {
        return _threadStateInterval;
    }
    
    /**
     * @return 输出所有线程堆栈的开关，true表示开，false表示关
     */
    public boolean getThreadStackSwitch() {
        return _threadStackSwitch;
    }
    
    /**
     * @return 线程堆栈信息输出的间隔时间（单位：秒）
     */
    public int getThreadStackInterval() {
        return _threadStackInterval;
    }
    
    @Override
    public void destroy() {
        _threadPoolStateSwitch = false;
        _threadStateSwitch = false;
        _multiThreadPoolInfo.clear();
    }

}

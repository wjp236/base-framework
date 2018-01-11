package com.platform.base.framework.trunk.core.idworker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * ID组成 31(秒数)+5(业务类型)+10(节点数量)+17(序列)
 * ID设计参数情况：
 * 设计使用时长68年，支持32(0-31)个业务类型，1024(0-2013)个节点,每秒(并发)序列最大131071
 * nextId:ID 生产方法
 * decodeId: ID反解
 */
public class IdWorker {

    //开始该类生成ID的时间截，精确到秒，占 31 位
    private static final long startTime = 1499337971L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    //时间位数
    private static final long maxTimeBits = 31L;

    //业务类型buzid所占的位数 从0计数
    private static final long buzIdBits = 5L;

    //机器nodeid所占的位数 从0计数
    private static final long nodeIdBits = 10L;

    //序列在id中占的位数从1计数
    private static final long sequenceBits = 17L;

    //支持最大的时间秒数
    private static final long maxSencond = -1L ^ (-1L << maxTimeBits);

    //支持的最大数据标识id
    private static final long maxBuzId = -1L ^ (-1L << buzIdBits);

    //支持的最大机器id数
    private static final long maxNodeId = -1L ^ (-1L << nodeIdBits);

    //生成序列的最大值
    private static final long maxSequence = -1L ^ (-1L << sequenceBits);

    //机器nodeid向左移位数
    private static final long nodeIdLeftShift = sequenceBits;

    //业务类型buzid左移位数
    private static final long buzIdLeftShift = nodeIdBits + nodeIdLeftShift;

    //时间截向左移位数
    private static final long timestampLeftShift = buzIdBits + buzIdLeftShift;

    //最小ID
    private static final long minId = 1L << timestampLeftShift;

    //最大ID
    private static final long maxId = (maxSencond << timestampLeftShift)
            | (maxBuzId << buzIdLeftShift)
            | (maxNodeId << nodeIdLeftShift)
            | maxSequence;

    private final long nodeId;

    private final long buzId;

    //同一个时间截内生成的序列数，初始值是0
    private long sequence = 0L;

    //上次生成id的时间截，精确到秒
    private long lastTime = -1L;

    public IdWorker(long buzId , long nodeId){
        if(buzId < 0 || buzId > maxBuzId){
            throw new IllegalArgumentException(
                    String.format("buzId[%d] is less than 0 or greater than maxBuzId[%d].",
                            buzId, maxBuzId));
        }
        if(nodeId < 0 || nodeId > maxNodeId){
            throw new IllegalArgumentException(
                    String.format("nodeID[%d] is less than 0 or greater than maxNodeId[%d].", nodeId, maxNodeId));
        }
        this.nodeId = nodeId;
        this.buzId = buzId;
    }

    //生成id
    public synchronized long nextId(){
        long curTime = timeGen();
        if(curTime < lastTime){
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTime - curTime));
        }
        //如果是同一时间生成的，则自增
        if(curTime == lastTime){
            sequence = (sequence + 1) & maxSequence;
            if(sequence == 0){
                //表示已经达到本秒的序列值，需要生成下一个秒级的序列
                curTime = tilNextTime();
                //序列从0开始
                sequence = 0L;
            }
        }else{
            //如果发现是下一个时间单位，则自增序列回0，重新自增
            sequence = 0L;
        }

        lastTime = curTime;

        //移位并通过或运算拼到一起组成64位的ID
        return ((curTime - startTime) << timestampLeftShift)
                | (buzId << buzIdLeftShift)
                | (nodeId << nodeIdLeftShift)
                | sequence;
    }

    private long tilNextTime(){
        long timestamp = timeGen();
        if(timestamp <= lastTime){
            timestamp = tilNextTime();
        }
        return timestamp;
    }

    private long timeGen(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * decodeId: Id反解
     * @param encodeId 在id 范围内
     * @return 输出时间 业务类型 节点ID 及 序列号
     */
    public Map<String,String> decodeId(long encodeId){
        //ID超出范围
        if(encodeId<minId || encodeId > maxId){
            throw new IllegalArgumentException(
                    String.format("encodeId[%d] is out of  range[%d--%d].", encodeId, minId,maxId));
        }
        Map resMap = new HashMap<String, String>();

        //反解过程
        long timeStamp = encodeId >> timestampLeftShift;
        long buzId = ((timeStamp << timestampLeftShift) ^ encodeId ) >> buzIdLeftShift;
        long nodeId = ((timeStamp << timestampLeftShift | buzId<< buzIdLeftShift) ^ encodeId )>> nodeIdLeftShift;
        long sequence = (timeStamp << timestampLeftShift | buzId<< buzIdLeftShift| nodeId << nodeIdLeftShift)^encodeId;

        //日期还原
        Instant ins = Instant.ofEpochSecond(timeStamp+this.startTime);
        LocalDateTime ldt = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
        String sldt = ldt.format(formatter);

        resMap.put("seconds",timeStamp+this.startTime);
        resMap.put("dateTime", sldt);
        resMap.put("buzId" ,buzId);
        resMap.put("nodeId", nodeId);
        resMap.put("sequence", sequence);

        return resMap;
    }
}
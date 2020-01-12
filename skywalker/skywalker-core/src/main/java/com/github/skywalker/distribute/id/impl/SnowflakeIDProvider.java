package com.github.skywalker.distribute.id.impl;

import com.github.skywalker.distribute.id.IDProvider;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 雪花算法，使用zookeeper临时顺序节点获取workerId
 * </p>
 *
 * @author CHAO
 * @since 2019/7/10
 **/
@Slf4j
public class SnowflakeIDProvider implements IDProvider<Long> {
    public static final long EPOCH = 0L;
    /**
     * 序列号位数
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * workId位数
     */
    private static final long WORKER_ID_BITS = 10L;
    /**
     * 0-4095
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    /**
     * 0-1023
     */
    private static final long WORKER_ID_MAX_VALUE = ~(-1L << WORKER_ID_BITS);

    private static final long WORKER_ID = 0L;

    private byte sequenceOffset;

    private long sequence;

    private long lastMilliseconds;

    @Override
    public Long generateKey() {
        return getId();
    }

    private long getWorkerId() {
        return WORKER_ID;
    }

    @Synchronized
    private synchronized long getId() {
        long currentMillis = System.currentTimeMillis();
        //时钟回退
        if (clockBack(currentMillis)) {
            currentMillis = System.currentTimeMillis();
        }
        if (currentMillis == lastMilliseconds) {
            //sequence越界溢出
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                currentMillis = waitNextMillis(currentMillis);
            }
        } else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMillis;
        return currentMillis << TIMESTAMP_LEFT_SHIFT_BITS | (getWorkerId() << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    private long waitNextMillis(long lastMillis) {
        long currentMillis = System.currentTimeMillis();
        while (currentMillis <= lastMillis) {
            log.info("尝试获取时间戳，currentMillis={},lastMillis={}", currentMillis, lastMillis);
            currentMillis = System.currentTimeMillis();
        }
        return currentMillis;
    }

    @SneakyThrows
    private boolean clockBack(final long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
        //可以容忍10ms时钟回退，否则抛异常
        Preconditions.checkState(timeDifferenceMilliseconds < 10,
                "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastMilliseconds, currentMilliseconds);
        Thread.sleep(timeDifferenceMilliseconds);
        return true;
    }
}
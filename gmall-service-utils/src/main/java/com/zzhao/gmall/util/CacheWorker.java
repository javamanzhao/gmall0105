package com.zzhao.gmall.util;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @date 2019/11/15 0015下午 14:39
 */
public abstract class CacheWorker<T, Z> {

    public T find(Z z) {
        if (null == read(z)) {
            return write(z);
        }
        return read(z);
    }

    protected abstract T read(Z z);

    protected abstract T write(Z z);

}

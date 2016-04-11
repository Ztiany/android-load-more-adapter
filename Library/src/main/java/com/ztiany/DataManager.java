package com.ztiany;

import java.util.List;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 12:39                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public interface DataManager<T> {

    void addItem(T t);

    void removeItem(int position);

    void addList(List<T> dataList);

    void addItem(int position, T t);

}

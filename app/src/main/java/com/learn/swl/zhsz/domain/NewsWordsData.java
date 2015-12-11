package com.learn.swl.zhsz.domain;

import java.util.ArrayList;

/**
 * Created by ADM on 2015/12/11.
 */
public class NewsWordsData {
    public String reason;
    public int error_code;
    public ArrayList<String> result;

    @Override
    public String toString() {
        return "NewsWordsData{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", result=" + result +
                '}';
    }
}

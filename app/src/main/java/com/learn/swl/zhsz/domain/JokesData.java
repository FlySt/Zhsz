package com.learn.swl.zhsz.domain;

import java.util.ArrayList;

/**
 * Created by ADM on 2015/12/2.
 */
public class JokesData {

    public int error_code;
    public String reason;
    public JokesDataList result;

    public class JokesDataList {
        public ArrayList<TabJokesDataList> data;

        @Override
        public String toString() {
            return "JokesDataList{" +
                    "data=" + data +
                    '}';
        }
    }

    /**
     * 新闻列表对象
     *
     * @author Kevin
     *
     */
    public class TabJokesDataList {
        public String content;
        public String hashId;
        public String unixtime;
        public String updatetime;

        @Override
        public String toString() {
            return "TabJokesDataList{" +
                    "content='" + content + '\'' +
                    ", hashId='" + hashId + '\'' +
                    ", unixtime='" + unixtime + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JokesData{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
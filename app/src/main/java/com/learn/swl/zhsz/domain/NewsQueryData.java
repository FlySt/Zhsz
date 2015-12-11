package com.learn.swl.zhsz.domain;

import java.util.ArrayList;

/**
 * Created by ADM on 2015/12/11.
 */
public class NewsQueryData {
    public String reason;
    public int error_code;
    public ArrayList<TabNewsQueryData > result;

    @Override
    public String toString() {
        return "NewsWordsData{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", result=" + result +
                '}';
    }
    public class TabNewsQueryData{
        public String title;
        public String content;
        public String src;
        public String img;
        public String url;
        public String pdate_src;

        @Override
        public String toString() {
            return "TabNewsQueryData{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", src='" + src + '\'' +
                    ", img='" + img + '\'' +
                    ", url='" + url + '\'' +
                    ", pdate_src='" + pdate_src + '\'' +
                    '}';
        }
    }
}

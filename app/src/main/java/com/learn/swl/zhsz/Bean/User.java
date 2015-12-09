package com.learn.swl.zhsz.Bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;


/**
 * Created by SWan on 2015/10/12.
 */
public class User extends BmobUser{

/**
 *
 */
private static final long serialVersionUID = 1L;

        /**
         * 发布的博客列表
         */
        private BmobRelation blogs;

        /**
         * //显示数据拼音的首字母
         */
        private String sortLetters;

        /**
         * //性别-true-男
         */
        private Boolean sex;

        //private Blog blog;

        /**
         * 地理坐标
         */
        private BmobGeoPoint location;//

        private Integer hight;
        private String nick;
        private BmobFile avatar;

        public BmobFile getAvatar() {
            return avatar;
        }

        public void setAvatar(BmobFile avatar) {
            this.avatar = avatar;
        }

         public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }


      /*  public Blog getBlog() {
            return blog;
        }
        public void setBlog(Blog blog) {
            this.blog = blog;
        } */
        public Integer getHight() {
            return hight;
        }
        public void setHight(Integer hight) {
            this.hight = hight;
        }
        public BmobRelation getBlogs() {
            return blogs;
        }
        public void setBlogs(BmobRelation blogs) {
            this.blogs = blogs;
        }
        public BmobGeoPoint getLocation() {
            return location;
        }
        public void setLocation(BmobGeoPoint location) {
            this.location = location;
        }
        public Boolean getSex() {
            return sex;
        }
        public void setSex(Boolean sex) {
            this.sex = sex;
        }
        public String getSortLetters() {
            return sortLetters;
        }
        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

}

package com.learning.util.basic;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by taylor on 9/2/16.
 * twitter: @taylorwang789
 */
public class PageResult {

    @Expose
    private Integer totalPage;
    @Expose
    private Integer page;
    @Expose
    private Integer size;
    @Expose
    private List  content;
    @Expose
    private boolean hasNext;
    @Expose
    private boolean hasPrevious;


    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}

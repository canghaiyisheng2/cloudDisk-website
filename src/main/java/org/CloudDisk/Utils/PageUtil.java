package org.CloudDisk.Utils;

import org.CloudDisk.pojo.upFile;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageUtil {
    List<upFile> content;
    int curPage;
    int totalPage;

    public PageUtil(List<upFile> content, int curPage, int totalPage) {
        this.content = content;
        this.curPage = curPage;
        this.totalPage = totalPage;
    }

    public PageUtil(Page<upFile> p){
        if (p == null) return;
        this.content = p.getContent();
        this.totalPage = p.getTotalPages();
        this.curPage = p.getNumber();
    }

    public List<upFile> getContent() {
        return content;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setContent(List<upFile> content) {
        this.content = content;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}

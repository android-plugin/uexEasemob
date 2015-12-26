package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;

public class PageVO implements Serializable{

    private static final long serialVersionUID = 5561255063399857576L;

    private String pageSize;
    private String cursor;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}

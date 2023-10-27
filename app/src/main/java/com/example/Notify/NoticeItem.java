package com.example.Notify;

public class NoticeItem {
    private String noticeHead;
    private String noticebody;
    private String Url;

    public  NoticeItem(){}

    public NoticeItem(String noticeHead, String noticebody) {
        this.noticeHead = noticeHead;
        this.noticebody = noticebody;
    }

    public NoticeItem(String noticeHead, String noticebody, String url) {
        this.noticeHead = noticeHead;
        this.noticebody = noticebody;
        this.Url = url;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getNoticeHead() {
        return noticeHead;
    }

    public void setNoticeHead(String noticeHead) {
        this.noticeHead = noticeHead;
    }

    public String getNoticebody() {
        return noticebody;
    }

    public void setNoticebody(String noticebody) {
        this.noticebody = noticebody;
    }
}

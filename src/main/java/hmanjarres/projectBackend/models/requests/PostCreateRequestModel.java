package hmanjarres.projectBackend.models.requests;

public class PostCreateRequestModel {
    private String title;
    private String content;
    private Long exposureId;
    private int expitarionTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getExposureId() {
        return exposureId;
    }

    public void setExposureId(Long exposureId) {
        this.exposureId = exposureId;
    }

    public int getExpitarionTime() {
        return expitarionTime;
    }

    public void setExpitarionTime(int expitarionTime) {
        this.expitarionTime = expitarionTime;
    }
}

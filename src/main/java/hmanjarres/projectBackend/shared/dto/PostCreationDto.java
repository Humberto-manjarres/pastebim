package hmanjarres.projectBackend.shared.dto;

import java.io.Serializable;

public class PostCreationDto implements Serializable {
    private String title;
    private String content;
    private Long exposureId;
    private int expitarionTime;
    private String userEmail;

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

package hmanjarres.projectBackend.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class PostDto implements Serializable {
    private Long id;
    private String postId;
    private String title;
    private String Content;
    private Date expiresAt;
    private Date createdAt;
    private UserDto user;
    private ExposureDto exposure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ExposureDto getExposure() {
        return exposure;
    }

    public void setExposure(ExposureDto exposure) {
        this.exposure = exposure;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

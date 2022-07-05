package hmanjarres.projectBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String Content;

    @Column(nullable = false)
    private Date expiresAt;

    @CreatedDate
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"posts","hibernateLazyInitializer","handler"},allowSetters = true)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"posts","hibernateLazyInitializer","handler"},allowSetters = true)
    @JoinColumn(name = "exposure_id")
    private ExposureEntity exposure;

    public ExposureEntity getExposure() {
        return exposure;
    }

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

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setExposure(ExposureEntity exposure) {
        this.exposure = exposure;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

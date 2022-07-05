package hmanjarres.projectBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exposures")
public class ExposureEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL ,mappedBy = "exposure")
    @JsonIgnoreProperties(value = {"exposure","hibernateLazyInitializer","handler"},allowSetters = true)
    private List<PostEntity> posts;

    public ExposureEntity() {
        this.posts = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }
}

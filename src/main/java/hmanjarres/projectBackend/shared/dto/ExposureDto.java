package hmanjarres.projectBackend.shared.dto;

import java.io.Serializable;
import java.util.List;

public class ExposureDto implements Serializable {
    private Long id;
    private String type;
    private List<PostDto> postDtos;

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

    public List<PostDto> getPostDtos() {
        return postDtos;
    }

    public void setPostDtos(List<PostDto> postDtos) {
        this.postDtos = postDtos;
    }
}

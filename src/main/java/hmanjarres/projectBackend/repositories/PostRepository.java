package hmanjarres.projectBackend.repositories;

import hmanjarres.projectBackend.entities.PostEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<PostEntity, Long> {
    public List<PostEntity> getByUserIdOrderByCreatedAtDesc(Long userId);

    @Query(value = "SELECT * FROM posts p WHERE p.exposure_id = :exposure and p.expires_at > :now ORDER BY created_at DESC LIMIT 20", nativeQuery = true)
    public List<PostEntity> getLastPublicPosts(@Param("exposure")Long exposureId, @Param("now") String now);

    @Query(value = "SELECT * FROM posts p WHERE p.exposure_id = :exposure", nativeQuery = true)
    public List<PostEntity> getLastPublicPosts2(@Param("exposure")Long exposureId);

    public PostEntity findByPostId(String postId);

    @Query(value = "select * from posts p where p.post_id = :postId", nativeQuery = true)
    public PostEntity buscarPost(String postId);

}

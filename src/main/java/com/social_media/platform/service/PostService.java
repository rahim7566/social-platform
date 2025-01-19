package com.social_media.platform.service;

import com.social_media.platform.entity.Post;
import com.social_media.platform.entity.User;
import com.social_media.platform.repository.PostRepository;
import com.social_media.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(int page, int size) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, size));
        return postPage.getContent();
    }

    @Transactional
    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }

        return postRepository.save(post);
    }

    public Post getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.orElse(null);
    }

    public Object updatePost(Post post) {
        return postRepository.findById(post.getId())
                .map(existingPost -> {
                    existingPost.setComments(post.getComments());
                    existingPost.setUser(post.getUser());
                    existingPost.setLikes(post.getLikes());
                    existingPost.setContent(post.getContent());
                    existingPost.setTimestamp(post.getTimestamp());
                    return postRepository.save(existingPost);
                }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Object deletePost(Long postId) {
        postRepository.deleteById(postId);
        return "Post Deleted successfully....!";
    }
}


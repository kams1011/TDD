package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {

    private final List<Post> data = new ArrayList<>();

    private final AtomicLong autoIncrement = new AtomicLong(0);


    @Override
    public Optional<Post> findById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            Post newPost = Post.builder()
                    .id(autoIncrement.incrementAndGet())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .writer(post.getWriter())
                    .build();
            data.add(newPost);
            return newPost;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), post.getId()));
            return post;
        }
    }
}
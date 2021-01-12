package dev.golony.springbook.service.posts;

import dev.golony.springbook.domain.posts.Post;
import dev.golony.springbook.domain.posts.PostRepository;
import dev.golony.springbook.web.dto.PostListResponseDto;
import dev.golony.springbook.web.dto.PostsResponseDto;
import dev.golony.springbook.web.dto.PostsSaveRequestDto;
import dev.golony.springbook.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostRepository postRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당 게시글이 없습니다. id:{}", id)));

        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당 게시글이 없습니다. id:{}", id)));

        return new PostsResponseDto(entity);
    }

    // 트랜잭션 범위유지 and 조회기능만 유지 => 속도개선
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postRepository.delete(post);
    }
}

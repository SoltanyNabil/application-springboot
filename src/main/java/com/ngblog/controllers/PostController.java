package com.ngblog.controllers;

import com.ngblog.model.Post;
import com.ngblog.repositories.PostRepository;
import exception.GlobalException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PostController {

    PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // get all posts
    @GetMapping("/posts")
    @CrossOrigin(origins = "*")
    List<Post> getPosts(){
        List<Post> posts =  this.postRepository.findAll();
        if (posts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "There no posts!");
        }else {
            return posts;
        }
    }

    // get all posts paginated
    @GetMapping("/posts/paginated/{pageNo}/{pageSize}/{sortBy}")
    @CrossOrigin(origins = "*")
    Page<Post> getPosts(@PathVariable("pageNo") Integer pageNo,
                        @PathVariable("pageSize") Integer pageSize,
                        @PathVariable("sortBy") String sortBy){
        System.out.println("page number " + pageNo);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("publishedDate").ascending());
       Page<Post> posts =  this.postRepository.findAll(paging);
       if (posts.isEmpty()){
           throw new ResponseStatusException(HttpStatus.OK, "There no posts!");
       }else {
           return posts;
       }


    }

    // add new post
    @PostMapping("/posts")
    @CrossOrigin(origins = "*")
    void addPost(@RequestBody Post post){
        this.postRepository.save(post);
    }

    // get post by id
    @GetMapping("/posts/{id}")
    @CrossOrigin(origins = "*")
    Optional<Post> addPost(@PathVariable("id") @Min(0) Long id){
        Optional<Post> postExist = this.postRepository.findById(id);
        if (postExist.isPresent()){
            return postExist;
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Get - There is no post with this id: " + id);
        }
    }

    // update post by id
    @PutMapping("/posts/{id}")
    @CrossOrigin(origins = "*")
    public Post updatePostById(@PathVariable("id") Long id, @RequestBody Post post) {
        post.setId(id);
        if (postRepository.findById(id).isPresent()) {
            return postRepository.save(post);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Update - There is no post with this id: " + id);
        }
    }

    // delete post by id method
    @DeleteMapping("/posts/{id}")
    @CrossOrigin(origins = "*")
    public void deletePostById(@PathVariable Long id){
        if (this.postRepository.findById(id).isPresent()) {
            this.postRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delete - there no post with this id: " + id);
        }
    }

}

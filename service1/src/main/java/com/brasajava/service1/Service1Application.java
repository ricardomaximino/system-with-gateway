package com.brasajava.service1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@RequestMapping("/message")
public class Service1Application {

  private Map<Integer, String> map;

  public Service1Application() {
    this.map = new HashMap<>();
  }

  public static void main(String[] args) {
    SpringApplication.run(Service1Application.class, args);
  }

  @PostMapping
  public String create(@RequestBody String message) {
    int id = map.size();
    map.put(id, message);
    return "ID: " + id + " MESSAGE: " + message;
  }

  @PutMapping("/{id}")
  public Mono<String> update(@PathVariable Integer id, @RequestBody String message) {
    if (map.containsKey(id)) {
      map.remove(id);
      map.put(id, message);
      return Mono.just("THE MESSAGE WAS UPDATED SUCCESFULLY!!!");
    }
    return Mono.just("THE MESSAGE WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @DeleteMapping("/{id}")
  public Mono<String> delete(@PathVariable Integer id) {
    if (map.containsKey(id)) {
      map.remove(id);
      return Mono.just("THE MESSAGE WAS DELETED SUCCESFULLY!!!");
    }
    return Mono.just("THE MESSAGE WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @GetMapping("/{id}")
  public Mono<String> findById(@PathVariable Integer id) {
    if (map.containsKey(id)) {
      return Mono.just("ID: " + id + " MESSAGE: " + map.get(id));
    }
    return Mono.just("THE MESSAGE WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @GetMapping
  public Flux<String> findAll() {
    return Flux.fromIterable(map.entrySet())
        .map(e -> "ID: " + e.getKey() + " MESSAGE: " + e.getValue());
  }
}

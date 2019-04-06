package com.brasajava.service2;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@RequestMapping("/person")
public class Service2Application {

  private Map<Integer, String> map;

  public Service2Application() {
    this.map = new HashMap<>();
  }

  public static void main(String[] args) {
    SpringApplication.run(Service2Application.class, args);
  }

  @PostMapping
  public String create(@RequestBody String PERSON) {
    int id = map.size();
    map.put(id, PERSON);
    return "ID: " + id + " PERSON: " + PERSON;
  }

  @PutMapping("/{id}")
  public Mono<String> update(@PathVariable Integer id, @RequestBody String PERSON) {
    if (map.containsKey(id)) {
      map.remove(id);
      map.put(id, PERSON);
      return Mono.just("THE PERSON WAS UPDATED SUCCESFULLY!!!");
    }
    return Mono.just("THE PERSON WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @DeleteMapping("/{id}")
  public Mono<String> delete(@PathVariable Integer id) {
    if (map.containsKey(id)) {
      map.remove(id);
      return Mono.just("THE PERSON WAS DELETED SUCCESFULLY!!!");
    }
    return Mono.just("THE PERSON WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @GetMapping("/{id}")
  public Mono<String> findById(@PathVariable Integer id) {
    if (map.containsKey(id)) {
      return Mono.just("ID: " + id + " PERSON: " + map.get(id));
    }
    return Mono.just("THE PERSON WITH ID: " + id + " WAS NOT FOUND!!");
  }

  @GetMapping
  public Flux<String> findAll() {
    return Flux.fromIterable(map.entrySet())
        .map(e -> "ID: " + e.getKey() + " PERSON: " + e.getValue());
  }

  @GetMapping(headers = {"X-TestHeader"})
  public Flux<String> findAll(
      @RequestHeader(required = true, name = "X-TestHeader") String header) {
    return Flux.fromIterable(map.entrySet())
        .map(e -> "ID: " + e.getKey() + " PERSON: " + e.getValue() + " HEADER: " + header);
  }

  @GetMapping(
      headers = {"X-TestHeader1"},
      params = {"foo=fooRequest", "bar=barRequest"})
  public Flux<String> findAll(
      @RequestHeader(required = true, name = "Y-TestHeader") String header,
      @RequestParam(required = true) String foo,
      @RequestParam(required = true) String bar) {
    return Flux.fromIterable(map.entrySet())
        .map(
            e ->
                "ID: "
                    + e.getKey()
                    + " PERSON: "
                    + e.getValue()
                    + " HEADER: "
                    + header
                    + " FOO: "
                    + foo
                    + " BAR: "
                    + bar);
  }
}

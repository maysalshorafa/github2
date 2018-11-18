package com.pos.leaders.leaderspossystem.Repositorys;
import java.util.List;

/**
 * Created by Win8.1 on 11/13/2018.
 */

public interface Repository<T,K> {
    void add(T item);
    T create(T item);
    T getById(K id) throws Exception;
    void add(Iterable<T> items);
    void update(T item);
    void remove(T item);
    List<T> query(Specification specification);
}
package dao;

import java.util.List;

public interface BasicDAO<T> {
    void save(T entity);
    T getById(Long id);
    void update(T entity);
    void delete(T entity);
    List<T> getAll();
}

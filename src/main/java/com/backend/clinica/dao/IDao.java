package com.backend.clinica.dao;

import java.util.List;

public interface IDao<K, T> {
  T create(T t);

  T readOne(K k);

  List<T> readAll();

  T update(K k, T t);

  boolean delete(K k);
}

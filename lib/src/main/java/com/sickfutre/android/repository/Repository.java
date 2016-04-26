package com.sickfutre.android.repository;

import java.util.List;

public interface Repository<Entity, QuerySpec extends Repository.Specification> {
    void add(Entity item);

    void add(Iterable<Entity> items);

    void update(Entity item);

    void remove(Entity item);

    void remove(QuerySpec specification);

    List<Entity> query(QuerySpec specification);

    interface Specification {
    }
}

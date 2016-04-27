package com.sickfutre.android.repository;

import java.util.List;

public interface Repository<Entity> {
    void add(Entity item);

    void add(Iterable<Entity> items);

    void update(Entity item);

    void remove(Entity item);

    void remove(Specification specification);

    List<Entity> query(Specification specification);

    interface Specification {
    }
}

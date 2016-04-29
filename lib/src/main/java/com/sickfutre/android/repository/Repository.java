package com.sickfutre.android.repository;

import java.util.Collection;
import java.util.List;

public interface Repository<Entity> {
    void add(Entity item);

    void add(Collection<Entity> items);

    void update(Entity item);

    void remove(Entity item);

    void remove(Specification specification);

    List<Entity> query(Specification specification);

    interface Specification {
    }
}

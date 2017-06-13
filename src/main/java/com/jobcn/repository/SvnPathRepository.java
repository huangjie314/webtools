package com.jobcn.repository;

import com.jobcn.Entity.SvnPath;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by winson on 17-6-12.
 */
@Transactional
@Repository
public interface SvnPathRepository extends JpaRepository<SvnPath, Integer> {

    @Cacheable(value="svnPath", key="#p0")
    SvnPath findOne(Integer id);

    @Cacheable(value="svnPath", key="#p0")
    List<SvnPath> findAll();

    boolean exists(Integer id);

    SvnPath save(SvnPath svnPath);

}

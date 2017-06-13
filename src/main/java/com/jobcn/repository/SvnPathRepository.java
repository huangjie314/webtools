package com.jobcn.repository;

import com.jobcn.Entity.SvnPath;
import com.jobcn.Entity.SvnUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by winson on 17-6-12.
 */
@Transactional
@Repository
@RepositoryDefinition(domainClass = SvnPath.class, idClass = Integer.class)
public interface SvnPathRepository extends JpaRepository<SvnPath, Integer> {

    @Cacheable(value="svnPath", key="#p0")
    SvnPath findOne(Integer id);

    @Cacheable(value="svnPath", key="#p0")
    List<SvnPath> findAll();

    boolean exists(Integer id);

    @CacheEvict(value="svnPath", key="#p0.id")
    SvnUser save(SvnUser svnUser);

}

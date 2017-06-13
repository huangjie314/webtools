package com.jobcn.repository;

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
@RepositoryDefinition(domainClass = SvnUser.class, idClass = Integer.class)
public interface SvnUserRepository  extends JpaRepository<SvnUser, Integer> {

    @Cacheable(value="svnUser", key="#p0")
    SvnUser findOne(Integer id);

    @Cacheable(value="svnUser", key="#p0")
    List<SvnUser> findAll();

    boolean exists(Integer id);

    @CacheEvict(value="svnUser", key="#p0.id")
    SvnUser save(SvnUser svnUser);

}

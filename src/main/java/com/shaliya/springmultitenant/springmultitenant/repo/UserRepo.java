package com.shaliya.springmultitenant.springmultitenant.repo;


import com.shaliya.springmultitenant.springmultitenant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, String> {
    @Query(value = "SELECT user_id FROM user WHERE user_id like ?% ORDER BY CAST(SUBSTRING(user_id,?) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastId(String s, int i);
    @Query(value = "SELECT * FROM user WHERE email=?1", nativeQuery = true)
    public User findByEmail(String username);
}

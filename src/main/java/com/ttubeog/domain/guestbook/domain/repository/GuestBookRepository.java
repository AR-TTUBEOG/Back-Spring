package com.ttubeog.domain.guestbook.domain.repository;

import com.ttubeog.domain.guestbook.domain.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    List<GuestBook> findByStoreId(Long storeId);
}

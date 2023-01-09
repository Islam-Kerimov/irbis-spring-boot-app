package ru.plus.irbis.web.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.plus.irbis.web.app.model.entity.NewsBody;

public interface NewsBodyRepository extends JpaRepository<NewsBody, Integer> {

    @Query("" +
            "select nb " +
            "from NewsBody nb " +
            "   join nb.newsTopic nt " +
            "   join nt.newsSource ns " +
            "where ns.name = :source")
    Page<NewsBody> findBySource(String source, Pageable pageable);

    Page<NewsBody> findByTopicId(Integer id, Pageable pageable);
}

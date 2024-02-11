package com.skillbox.cryptobot.db.repository;

import com.skillbox.cryptobot.db.entity.Subscribers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscribersRepo extends JpaRepository<Subscribers, Integer> {

    @Query("select s from Subscribers s where s.TgID = :tgId")
    Subscribers findByTgID(@Param("tgId") Long tgId);

    @Query("SELECT s FROM Subscribers s WHERE s.price > :price AND s.lastTimeSendMessage < :tenMinutesAgo")
    List<Subscribers> findWherePriceLower(@Param("price") Integer price, @Param("tenMinutesAgo") LocalDateTime intervalMinutes);







}

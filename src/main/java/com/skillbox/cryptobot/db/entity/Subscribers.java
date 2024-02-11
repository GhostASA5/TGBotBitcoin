package com.skillbox.cryptobot.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Subscribers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "uuid", nullable = false)
    private String UUID;

    @Column(name = "tgid", nullable = false)
    private Long TgID;

    @Column
    private Integer price;

    @Column(name = "last_time_send_message")
    private LocalDateTime lastTimeSendMessage;

}

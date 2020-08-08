package com.generic.security.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash("session")
@Getter
@Setter
@ToString
public class Session implements Serializable {

    @Id
    private String id;

    private long userId;

    @TimeToLive
    private Long expiration;
}

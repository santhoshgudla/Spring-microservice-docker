package com.generic.security.domain;

import com.generic.security.constant.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "uuid")
    private String uuId;

    @Column(name = "username", nullable = false)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active")
    private int isActive;

    @Column(name = "is_delete")
    private int isDelete;

    @Column(name = "is_verified")
    private int isVerified;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private Role role;
}

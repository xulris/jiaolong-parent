package com.zbf.pojo.base;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseAuditable {

    @Column(name = "id")
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @LastModifiedDate
            @Column(name = "updateTime")
    Date updateTime;

    @Column(name = "createTime")
            @CreatedDate
    Date createTime;

    @Column(name = "version")
    @Version
    private Long version;
}

package com.odc19.baseEntity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@EntityListeners({AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Column(name = "createddate", updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(name = "lastedmodifieddate")
    @LastModifiedDate
    private Date lastedModifiedDate;

    @Column(name = "createdby", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "lastedmodifiedby")
    @LastModifiedBy
    private String lastedModifiedBy;

    @Column
    private boolean isDeleted;
}

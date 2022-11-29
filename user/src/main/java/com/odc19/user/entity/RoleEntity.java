package com.odc19.user.entity;

import com.odc19.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_role_id",
            parameters = @Parameter(name = "prefix", value = "role"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_role_id")
    @Column(name = "role_id")
    private String roleId;

    @Column(unique=true, nullable = false)
    private String roleName;

    private String roleDescription;
}

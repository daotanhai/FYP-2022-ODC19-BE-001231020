package com.odc19.user.entity;

import com.odc19.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "appuser")
@Getter
@Setter
public class UserEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_user_id",
            parameters = @Parameter(name = "prefix", value = "user"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_user_id")
    @Column(name = "user_id")
    private String userId;


    @Column(unique = true)
    @NotEmpty(message = "userName is required")
    private String userName;

    @NotEmpty(message = "Password is required")
    private String password;

    @Column(unique = true)
    private String identityNumber;

    @Column(unique = true)
    @NotEmpty(message = "phoneNumber is required")
    private String phoneNumber;

    @Column(unique = true)
    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "fullName is required")
    private String fullName;

    @NotEmpty(message = "fullName is required")
    private String address;

    private String streetNumber;

    private String postCode;

    private Double longitude;

    private Double latitude;

    private String identityCardUrl;

    private String randomTokenResetPassword;

    @ColumnDefault("false")
    private boolean isTokenUsed;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<RoleEntity> roleEntities;

}

package com.odc19.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
}

package com.example.Base.domain.entity;

import com.example.Base.domain.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data //Getter, Setter, toString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public RoleDTO toDTO(){
        RoleDTO roleDTO = new RoleDTO().builder()
                .name(name)
                .build();
        return roleDTO;
    }
}

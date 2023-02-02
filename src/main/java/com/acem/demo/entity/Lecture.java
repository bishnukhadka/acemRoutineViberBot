package com.acem.demo.entity;

import com.acem.demo.entity.enums.GroupEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LECTURES")
public class Lecture {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 200, nullable = false)
    private String name;

    @Column(name = "START_TIME",length = 200, nullable = false)
    private String startTime;

    @Column(name = "END_TIME",length = 200, nullable = false)
    private String endTime;

    @Column(name = "GRP", columnDefinition = "VARCHAR(50) DEFAULT 'BOTH'")
    @Enumerated(EnumType.STRING)
    private GroupEnum group;

    @Override
    public String toString() {
        return "\n" + startTime + "-" + endTime + "\t" + name + "(" + group + " Group)";
    }
}
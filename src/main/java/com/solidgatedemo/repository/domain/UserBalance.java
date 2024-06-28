package com.solidgatedemo.repository.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "user_balances")
public class UserBalance {

    @Id
    private Long id;
    private String name;
    private Long balance;
    private LocalDateTime created;
    private LocalDateTime updated;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now().withNano(0);
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = LocalDateTime.now().withNano(0);
    }

}

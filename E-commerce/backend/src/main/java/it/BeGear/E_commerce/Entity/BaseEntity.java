package it.BeGear.E_commerce.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString

public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCreazione;

    @LastModifiedDate
    @Column(insertable = false)
    @UpdateTimestamp
    private LocalDateTime dataModifica;
}

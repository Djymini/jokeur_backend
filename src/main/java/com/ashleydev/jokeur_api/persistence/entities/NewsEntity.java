package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notification", uniqueConstraints = @UniqueConstraint(columnNames = "link"))
public class NewsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(length = 2000)
  private String summary;

  private String link;

  private LocalDateTime publishedAt;

  private boolean sent;
}

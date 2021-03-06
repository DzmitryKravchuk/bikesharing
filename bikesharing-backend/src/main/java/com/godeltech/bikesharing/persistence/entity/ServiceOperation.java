package com.godeltech.bikesharing.persistence.entity;

import com.godeltech.bikesharing.persistence.entity.common.AuditableEntity;
import com.godeltech.bikesharing.persistence.entity.lookup.ServiceType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "service_operation")
@EqualsAndHashCode(of = "id", callSuper = false)
public class ServiceOperation extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "issue_description")
  private String issueDescription;

  @Column(name = "start_date")
  private LocalDate startDate = LocalDate.now();

  @Column(name = "end_date")
  private LocalDate endDate;

  private String comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipment_item_id", nullable = false)
  private EquipmentItem equipmentItem;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;
}

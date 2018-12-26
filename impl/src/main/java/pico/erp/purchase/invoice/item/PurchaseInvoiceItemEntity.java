package pico.erp.purchase.invoice.item;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.project.ProjectId;
import pico.erp.purchase.invoice.PurchaseInvoiceId;
import pico.erp.purchase.order.item.PurchaseOrderItemId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Entity(name = "PurchaseInvoiceItem")
@Table(name = "PCI_PURCHASE_INVOICE_ITEM", indexes = @Index(columnList = "INVOICE_ID"))
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseInvoiceItemEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  PurchaseInvoiceItemId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "INVOICE_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  PurchaseInvoiceId invoiceId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ORDER_ITEM_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  PurchaseOrderItemId orderItemId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "INVOICE_ITEM_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  InvoiceItemId invoiceItemId;

  @Column(precision = 19, scale = 2)
  BigDecimal quantity;

  @Column(length = TypeDefinitions.REMARK_LENGTH)
  String remark;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime createdDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "LAST_MODIFIED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "LAST_MODIFIED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  @LastModifiedBy
  Auditor lastModifiedBy;

  @LastModifiedDate
  OffsetDateTime lastModifiedDate;

}

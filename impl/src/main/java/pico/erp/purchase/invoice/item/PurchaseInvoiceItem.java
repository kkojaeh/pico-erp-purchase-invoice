package pico.erp.purchase.invoice.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.invoice.item.InvoiceItemData;
import pico.erp.purchase.invoice.PurchaseInvoice;
import pico.erp.purchase.order.item.PurchaseOrderItemData;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseInvoiceItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  PurchaseInvoiceItemId id;

  PurchaseInvoice invoice;

  PurchaseOrderItemData orderItem;

  InvoiceItemData invoiceItem;

  BigDecimal quantity;

  String remark;


  public PurchaseInvoiceItem() {

  }

  public PurchaseInvoiceItemMessages.Create.Response apply(
    PurchaseInvoiceItemMessages.Create.Request request) {
    if (!request.getInvoice().isUpdatable()) {
      throw new PurchaseInvoiceItemExceptions.CannotCreateException();
    }
    this.id = request.getId();
    this.invoice = request.getInvoice();
    this.orderItem = request.getOrderItem();
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();

    return new PurchaseInvoiceItemMessages.Create.Response(
      Arrays.asList(new PurchaseInvoiceItemEvents.CreatedEvent(this.id))
    );
  }

  public PurchaseInvoiceItemMessages.Update.Response apply(
    PurchaseInvoiceItemMessages.Update.Request request) {
    if (!this.invoice.isUpdatable()) {
      throw new PurchaseInvoiceItemExceptions.CannotUpdateException();
    }
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();
    return new PurchaseInvoiceItemMessages.Update.Response(
      Arrays.asList(new PurchaseInvoiceItemEvents.UpdatedEvent(this.id))
    );
  }

  public PurchaseInvoiceItemMessages.Delete.Response apply(
    PurchaseInvoiceItemMessages.Delete.Request request) {
    if (!this.invoice.isUpdatable()) {
      throw new PurchaseInvoiceItemExceptions.CannotDeleteException();
    }
    return new PurchaseInvoiceItemMessages.Delete.Response(
      Arrays.asList(new PurchaseInvoiceItemEvents.DeletedEvent(this.id))
    );
  }

  public PurchaseInvoiceItemMessages.Invoice.Response apply(
    PurchaseInvoiceItemMessages.Invoice.Request request) {
    if (invoiceItem != null) {
      throw new PurchaseInvoiceItemExceptions.CannotInvoiceException();
    }
    if (!request.getInvoiceItem().getInvoiceId().equals(this.invoice.getInvoice().getId())) {
      throw new PurchaseInvoiceItemExceptions.CannotInvoiceException();
    }
    this.invoiceItem = request.getInvoiceItem();
    return new PurchaseInvoiceItemMessages.Invoice.Response(
      Arrays.asList(new PurchaseInvoiceItemEvents.InvoicedEvent(this.id))
    );
  }


}

package pico.erp.purchase.invoice;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.invoice.InvoiceData;
import pico.erp.purchase.invoice.PurchaseInvoiceEvents.DeterminedEvent;
import pico.erp.purchase.order.PurchaseOrderData;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseInvoice implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  PurchaseInvoiceId id;

  PurchaseOrderData order;

  InvoiceData invoice;

  LocalDateTime dueDate;

  String remark;

  PurchaseInvoiceStatusKind status;

  public PurchaseInvoice() {

  }

  public PurchaseInvoiceMessages.Create.Response apply(
    PurchaseInvoiceMessages.Create.Request request) {
    this.id = request.getId();
    this.order = request.getOrder();
    this.dueDate = request.getDueDate();
    this.remark = request.getRemark();
    this.status = PurchaseInvoiceStatusKind.DRAFT;
    return new PurchaseInvoiceMessages.Create.Response(
      Arrays.asList(new PurchaseInvoiceEvents.CreatedEvent(this.id))
    );
  }

  public PurchaseInvoiceMessages.Update.Response apply(
    PurchaseInvoiceMessages.Update.Request request) {
    if (!isUpdatable()) {
      throw new PurchaseInvoiceExceptions.CannotUpdateException();
    }
    this.dueDate = request.getDueDate();
    this.remark = request.getRemark();
    return new PurchaseInvoiceMessages.Update.Response(
      Arrays.asList(new PurchaseInvoiceEvents.UpdatedEvent(this.id))
    );
  }

  public PurchaseInvoiceMessages.Determine.Response apply(
    PurchaseInvoiceMessages.Determine.Request request) {
    if (!isDeterminable()) {
      throw new PurchaseInvoiceExceptions.CannotDetermineException();
    }
    this.status = PurchaseInvoiceStatusKind.DETERMINED;
    return new PurchaseInvoiceMessages.Determine.Response(
      Arrays.asList(new DeterminedEvent(this.id))
    );
  }

  public PurchaseInvoiceMessages.Cancel.Response apply(
    PurchaseInvoiceMessages.Cancel.Request request) {
    if (!isCancelable()) {
      throw new PurchaseInvoiceExceptions.CannotCancelException();
    }
    this.status = PurchaseInvoiceStatusKind.CANCELED;
    return new PurchaseInvoiceMessages.Cancel.Response(
      Arrays.asList(new PurchaseInvoiceEvents.CanceledEvent(this.id))
    );
  }

  public PurchaseInvoiceMessages.Receive.Response apply(
    PurchaseInvoiceMessages.Receive.Request request) {
    if (!isReceivable()) {
      throw new PurchaseInvoiceExceptions.CannotReceiveException();
    }
    this.status = PurchaseInvoiceStatusKind.RECEIVED;
    return new PurchaseInvoiceMessages.Receive.Response(
      Arrays.asList(new PurchaseInvoiceEvents.ReceivedEvent(this.id))
    );
  }

  public PurchaseInvoiceMessages.Invoice.Response apply(
    PurchaseInvoiceMessages.Invoice.Request request) {
    if (!isInvoiceable()) {
      throw new PurchaseInvoiceExceptions.CannotInvoiceException();
    }
    this.invoice = request.getInvoice();
    return new PurchaseInvoiceMessages.Invoice.Response(
      Arrays.asList(new PurchaseInvoiceEvents.InvoicedEvent(this.id))
    );
  }


  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isReceivable() {
    return status.isReceivable();
  }

  public boolean isDeterminable() {
    return status.isDeterminable();
  }

  public boolean isUpdatable() {
    return status.isUpdatable();
  }

  public boolean isInvoiceable() {
    return status.isInvoiceable() && invoice == null;
  }


}

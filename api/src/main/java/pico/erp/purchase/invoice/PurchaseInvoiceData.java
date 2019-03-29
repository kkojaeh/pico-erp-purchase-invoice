package pico.erp.purchase.invoice;

import java.time.LocalDateTime;
import lombok.Data;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;

@Data
public class PurchaseInvoiceData {

  PurchaseInvoiceId id;

  PurchaseOrderId orderId;

  InvoiceId invoiceId;

  LocalDateTime dueDate;

  PurchaseInvoiceStatusKind status;

  String remark;

  boolean cancelable;

  boolean receivable;

  boolean determinable;

  boolean updatable;

}

package pico.erp.purchase.invoice;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.company.CompanyId;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.shared.data.Address;
import pico.erp.shared.data.Auditor;

@Data
public class PurchaseInvoiceData {

  PurchaseInvoiceId id;

  PurchaseOrderId orderId;

  InvoiceId invoiceId;

  OffsetDateTime dueDate;

  PurchaseInvoiceStatusKind status;

  String remark;

}

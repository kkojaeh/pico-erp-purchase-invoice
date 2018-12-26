package pico.erp.purchase.invoice.item;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.project.ProjectId;
import pico.erp.purchase.invoice.PurchaseInvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.purchase.order.item.PurchaseOrderItemId;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseInvoiceItemData {

  PurchaseInvoiceItemId id;

  PurchaseInvoiceId invoiceId;

  PurchaseOrderItemId orderItemId;

  InvoiceItemId invoiceItemId;

  BigDecimal quantity;

  String remark;

}

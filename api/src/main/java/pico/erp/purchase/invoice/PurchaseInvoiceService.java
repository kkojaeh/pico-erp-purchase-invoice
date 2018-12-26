package pico.erp.purchase.invoice;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.purchase.order.item.PurchaseOrderItemService;

public interface PurchaseInvoiceService {

  void cancel(@Valid @NotNull PurchaseInvoiceRequests.CancelRequest request);

  PurchaseInvoiceData create(@Valid @NotNull PurchaseInvoiceRequests.CreateRequest request);

  boolean exists(@Valid @NotNull PurchaseInvoiceId id);

  boolean exists(@Valid @NotNull InvoiceId invoiceId);

  PurchaseInvoiceData get(@Valid @NotNull PurchaseInvoiceId id);

  PurchaseInvoiceData get(@Valid @NotNull InvoiceId invoiceId);

  void update(@Valid @NotNull PurchaseInvoiceRequests.UpdateRequest request);

  void determine(@Valid @NotNull PurchaseInvoiceRequests.DetermineRequest request);

  void receive(@Valid @NotNull PurchaseInvoiceRequests.ReceiveRequest request);

  void invoice(@Valid @NotNull PurchaseInvoiceRequests.InvoiceRequest request);

  PurchaseInvoiceData generate(@Valid @NotNull PurchaseInvoiceRequests.GenerateRequest request);

  List<PurchaseInvoiceData> getAll(@Valid @NotNull PurchaseOrderId orderId);

}

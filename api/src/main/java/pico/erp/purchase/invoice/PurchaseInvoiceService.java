package pico.erp.purchase.invoice;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;

public interface PurchaseInvoiceService {

  void cancel(@Valid @NotNull PurchaseInvoiceRequests.CancelRequest request);

  PurchaseInvoiceData create(@Valid @NotNull PurchaseInvoiceRequests.CreateRequest request);

  void determine(@Valid @NotNull PurchaseInvoiceRequests.DetermineRequest request);

  boolean exists(@Valid @NotNull PurchaseInvoiceId id);

  boolean exists(@Valid @NotNull InvoiceId invoiceId);

  PurchaseInvoiceData generate(@Valid @NotNull PurchaseInvoiceRequests.GenerateRequest request);

  PurchaseInvoiceData get(@Valid @NotNull PurchaseInvoiceId id);

  PurchaseInvoiceData get(@Valid @NotNull InvoiceId invoiceId);

  List<PurchaseInvoiceData> getAll(@Valid @NotNull PurchaseOrderId orderId);

  void invoice(@Valid @NotNull PurchaseInvoiceRequests.InvoiceRequest request);

  void receive(@Valid @NotNull PurchaseInvoiceRequests.ReceiveRequest request);

  void update(@Valid @NotNull PurchaseInvoiceRequests.UpdateRequest request);

}

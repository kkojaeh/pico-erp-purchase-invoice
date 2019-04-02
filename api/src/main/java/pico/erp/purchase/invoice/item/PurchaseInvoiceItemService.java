package pico.erp.purchase.invoice.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.purchase.invoice.PurchaseInvoiceId;

public interface PurchaseInvoiceItemService {

  PurchaseInvoiceItemData create(
    @Valid @NotNull PurchaseInvoiceItemRequests.CreateRequest request);

  void delete(@Valid @NotNull PurchaseInvoiceItemRequests.DeleteRequest request);

  boolean exists(@Valid @NotNull PurchaseInvoiceItemId id);

  void generate(@Valid @NotNull PurchaseInvoiceItemRequests.GenerateRequest request);

  PurchaseInvoiceItemData get(@Valid @NotNull PurchaseInvoiceItemId id);

  List<PurchaseInvoiceItemData> getAll(PurchaseInvoiceId invoiceId);

  void invoice(@Valid @NotNull PurchaseInvoiceItemRequests.InvoiceRequest request);

  void update(@Valid @NotNull PurchaseInvoiceItemRequests.UpdateRequest request);


}

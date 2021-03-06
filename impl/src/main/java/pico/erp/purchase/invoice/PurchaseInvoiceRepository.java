package pico.erp.purchase.invoice;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;

@Repository
public interface PurchaseInvoiceRepository {

  PurchaseInvoice create(@NotNull PurchaseInvoice orderAcceptance);

  void deleteBy(@NotNull PurchaseInvoiceId id);

  boolean exists(@NotNull PurchaseInvoiceId id);

  boolean exists(@NotNull InvoiceId invoiceId);

  Stream<PurchaseInvoice> findAllBy(@NotNull PurchaseOrderId orderId);

  Optional<PurchaseInvoice> findBy(@NotNull InvoiceId invoiceId);

  Optional<PurchaseInvoice> findBy(@NotNull PurchaseInvoiceId id);

  void update(@NotNull PurchaseInvoice orderAcceptance);

}

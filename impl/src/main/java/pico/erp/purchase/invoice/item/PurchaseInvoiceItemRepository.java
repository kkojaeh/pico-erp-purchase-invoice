package pico.erp.purchase.invoice.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.purchase.invoice.PurchaseInvoiceId;

@Repository
public interface PurchaseInvoiceItemRepository {

  PurchaseInvoiceItem create(@NotNull PurchaseInvoiceItem item);

  void deleteBy(@NotNull PurchaseInvoiceItemId id);

  boolean exists(@NotNull PurchaseInvoiceItemId id);

  Stream<PurchaseInvoiceItem> findAllBy(@NotNull PurchaseInvoiceId planId);

  Optional<PurchaseInvoiceItem> findBy(@NotNull PurchaseInvoiceItemId id);

  void update(@NotNull PurchaseInvoiceItem item);

}
